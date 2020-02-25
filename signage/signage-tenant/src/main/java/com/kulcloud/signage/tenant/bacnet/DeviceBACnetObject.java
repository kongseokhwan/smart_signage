package com.kulcloud.signage.tenant.bacnet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kulcloud.signage.commons.enums.ValueType;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceChangedListener;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;

public class DeviceBACnetObject extends BACnetObject implements DeviceChangedListener {
	
	public static final String nameFormat = "%s(%s) - %s";
	
	private long deviceId;
	private String key;
	private ValueType valueType;
	
	private final List<DeviceBACnetObjectListener> listeners = new CopyOnWriteArrayList<>();

    private static ObjectIdentifier createObjectIdentifier(LocalDevice localDevice, DeviceMeta meta) {
    	ValueType valueType = ValueType.valueOf(meta.getType());
    	
    	switch(valueType) {
		case BOOLEAN:
			return new ObjectIdentifier(ObjectType.binaryOutput, localDevice.getNextInstanceObjectNumber(ObjectType.binaryOutput));
		case NUMBER:
		case ENUMERATION:
			return new ObjectIdentifier(ObjectType.analogOutput, localDevice.getNextInstanceObjectNumber(ObjectType.analogOutput));
		case STRING:
			return new ObjectIdentifier(ObjectType.characterstringValue, localDevice.getNextInstanceObjectNumber(ObjectType.characterstringValue));
		default:
			return null;
		}
    }
    
	public DeviceBACnetObject(LocalDevice localDevice, Device device, DeviceMeta meta) {
		super(localDevice, createObjectIdentifier(localDevice, meta), String.format(nameFormat, device.getTitle(), device.getDeviceId(), meta.getName()));
		this.deviceId = device.getDeviceId();
		this.key = meta.getName();
		this.valueType = ValueType.valueOf(meta.getType());
		
		Object value = device.getControls().get(meta.getName());
		setValue(value);
		
		addListener((PropertyIdentifier pid, Encodable oldEncodable, Encodable newEncodable) -> {
			Object oldValue = convertEncodableToPrimitive(oldEncodable);
			Object newValue = convertEncodableToPrimitive(newEncodable);
			for (final DeviceBACnetObjectListener l : listeners)
                l.propertyChange(this, oldValue, newValue);
		});
	}

	public long getDeviceId() {
		return deviceId;
	}

	public String getKey() {
		return key;
	}
	
	public void setValue(Object value) {
		Encodable encodable = convertPrimitiveToEncodable(value);
		if(encodable != null) {
			writePropertyInternal(PropertyIdentifier.presentValue, encodable);
		}
	}
	
	public Object getValue() {
		try {
			Encodable presentValue = readProperty(PropertyIdentifier.presentValue);
			return convertEncodableToPrimitive(presentValue);
		} catch (BACnetServiceException e) {
			return null;
		}
		
	}
	
	private Object currentValue() {
		return convertEncodableToPrimitive(get(PropertyIdentifier.presentValue));
	}
	
	public void addListener(final DeviceBACnetObjectListener l) {
        listeners.add(l);
    }

    public void removeListener(final DeviceBACnetObjectListener l) {
        listeners.remove(l);
    }
    
    private Object convertEncodableToPrimitive(Encodable encodable) {
    	if(encodable == null || encodable instanceof Null) {
    		return null;
    	}
    	
    	switch(valueType) {
		case BOOLEAN:
			Boolean encodableBoolean = (Boolean) encodable;
			return encodableBoolean.booleanValue();
		case NUMBER:
		case ENUMERATION:
			Real encodableReal = (Real) encodable;
			return encodableReal.floatValue();
		case STRING:
			CharacterString encodableString = (CharacterString) encodable;
			return encodableString.getValue();
		default:
			return null;
		}
    }
    
    private Encodable convertPrimitiveToEncodable(Object value) {
    	if(value == null) {
    		return new Null();
    	}
    	
    	switch(valueType) {
		case BOOLEAN:
			return Boolean.valueOf(java.lang.Boolean.parseBoolean(value.toString()));
		case NUMBER:
		case ENUMERATION:
			return new Real(Float.valueOf(Float.parseFloat(value.toString())));
		case STRING:
			return new CharacterString(value.toString());
		default:
			return new Null();
		}
    }

	@Override
	public void changeDeviceStatus(Device device) {
		if(deviceId == device.getDeviceId().longValue()) {
			Object current = currentValue();
			Object value = device.getControls().get(key);
			if(current == null && value == null) {
				return;
			}
			
			setValue(value);
		}
	}

}
