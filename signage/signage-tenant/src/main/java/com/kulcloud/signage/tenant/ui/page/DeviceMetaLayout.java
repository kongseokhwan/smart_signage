package com.kulcloud.signage.tenant.ui.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.kulcloud.signage.commons.enums.ValueType;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class DeviceMetaLayout extends FormLayout {

	private static final long serialVersionUID = 1L;
	
	private static final Item[] defaultBoolean = {new Item(false, "FALSE"), new Item(true, "TRUE")};

	private Map<String, Object> values;
	@SuppressWarnings("rawtypes")
	private Map<String, AbstractField> valueFields = new HashMap<>();
	private List<DeviceMeta> metaInfo;
	private boolean editing = false;
	private List<DeviceMetaChangeListener> listeners = new ArrayList<>();
	
	public DeviceMetaLayout(List<DeviceMeta> metaInfo) {
		this.metaInfo = metaInfo;
		createPropertyFields();
	}
	
	@SuppressWarnings("rawtypes")
	private void createPropertyFields() {
		if(metaInfo != null) {
			for (DeviceMeta meta : metaInfo) {
				AbstractField field = createField(meta);
				if(field != null) {
					this.add(field);
					this.valueFields.put(meta.getName(), field);
				}
			}
		}
	}
	
	public Map<String, Object> getValues() {
		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setValues(Map<String, Object> values) {
		this.values = values;
		for (Entry<String, AbstractField> entry : valueFields.entrySet()) {
			String keyName = entry.getKey();
			if(values.get(keyName) != null) {
				entry.getValue().setValue(values.get(keyName));
			}
		}
	}

	private AbstractField<?, ?> createField(DeviceMeta meta) {
		ValueType type = ValueType.valueOf(meta.getType());
		switch(type) {
		case BOOLEAN:
			RadioButtonGroup<Item> group = new RadioButtonGroup<>();
			group.setLabel(getKeyTitle(meta));
			
			Item[] booleanItems;
			if(meta.getEnumeration() != null && meta.getEnumeration().size() == 2) {
				booleanItems = new Item[]{new Item(false, meta.getEnumeration().get(0)), new Item(true, meta.getEnumeration().get(1))};
			} else {
				booleanItems = defaultBoolean;
			}
			
			group.setItems(booleanItems);
			if(!StringUtils.isEmpty(meta.getDefaultValue())) {
				try {
					group.setValue(CommonUtils.convertToBoolean(meta.getDefaultValue()) ? booleanItems[1] : booleanItems[0]);
				} catch (Throwable ignored) {}
			}
			
			group.addValueChangeListener(e -> changeValue(meta, e.getValue().getValue()));
			return group;
		case NUMBER:
			NumberField numberField = new NumberField(getKeyTitle(meta), e -> changeValue(meta, e.getValue()));
			numberField.setWidthFull();
			
			if(!StringUtils.isEmpty(meta.getDefaultValue())) {
				try {
					numberField.setValue(Double.parseDouble(meta.getDefaultValue()));
				} catch (Throwable ignored) {}
			}
			
			numberField.addValueChangeListener(e -> changeValue(meta, e.getValue()));
			
			return numberField;
		case ENUMERATION:
			if(meta.getEnumeration() == null || meta.getEnumeration().size() == 0) {
				return null;
			} else {
				ComboBox<Item> enumCombo = new ComboBox<>(getKeyTitle(meta));
				enumCombo.setWidthFull();
				Item[] enumItems = new Item[meta.getEnumeration().size()];
				for (int i = 0; i < meta.getEnumeration().size(); i++) {
					enumItems[i] = new Item(i, meta.getEnumeration().get(i));
				}
				enumCombo.setItems(enumItems);
				
				if(!StringUtils.isEmpty(meta.getDefaultValue())) {
					try {
						int value = Integer.parseInt(meta.getDefaultValue());
						if(value > -1 && value < enumItems.length) {
							Item defaultEnum = enumItems[value];
							enumCombo.setValue(defaultEnum);
						}
					} catch (Throwable ignored) {}
				}
				
				enumCombo.addValueChangeListener(e -> changeValue(meta, e.getValue().getValue()));
				
				return enumCombo;
			}
		case STRING:
			TextField stringField = new TextField(getKeyTitle(meta), e -> changeValue(meta, e.getValue()));
			stringField.setWidthFull();
			if(!StringUtils.isEmpty(meta.getDefaultValue())) {
				stringField.setValue(meta.getDefaultValue());
			}
			
			stringField.addValueChangeListener(e -> changeValue(meta, e.getValue()));
			return stringField;
		default:
			return null;
		}
	}
	
	private void changeValue(DeviceMeta meta, Object newValue) {
		if(editing && values != null) {
			String key = meta.getName();
			Object oldValue = values.put(key, newValue);
			for (DeviceMetaChangeListener listener : listeners) {
				listener.changeValue(meta, key, oldValue, newValue);
			}
		}
	}
	
	private String getKeyTitle(DeviceMeta meta) {
		return StringUtils.isEmpty(meta.getTitle()) ? meta.getName() : meta.getTitle();
	}
	
	static class Item {
		private Object value;
		private String title;
		
		public Item(Object value, String title) {
			super();
			this.value = value;
			this.title = title;
		}
		
		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return title;
		}
		
	}
}
