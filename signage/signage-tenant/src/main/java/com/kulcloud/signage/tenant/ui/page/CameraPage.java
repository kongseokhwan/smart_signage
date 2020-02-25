package com.kulcloud.signage.tenant.ui.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.commons.ui.PageView;
import com.kulcloud.signage.commons.ui.UiUtils;
import com.kulcloud.signage.commons.ui.component.ContentLayout;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.tenant.data.entity.Camera;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;
import com.kulcloud.signage.tenant.ui.MainView;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route(value = "camera", layout = MainView.class)
@PageView(title="Camera", icon="MOVIE")
@PageTitle("IP Camera")
public class CameraPage extends ContentLayout {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DeviceServiceManager deviceService;
	
	private HorizontalLayout content;
	private Grid<Camera> cameraGrid = new Grid<>(Camera.class);
	private Binder<Camera> cameraBinder;
	private HorizontalLayout buttonLayout;
	private Button saveButton;
	private Button deleteButton;
	private DeviceField deviceField;

	public CameraPage() {
		super();
		content = new HorizontalLayout();
		content.setPadding(true);
		content.setSpacing(true);
		content.setWidthFull();
		content.setHeightFull();
		content.add(createCameraGrid(), createFormLayout());
		content.expand(cameraGrid);
		setContent(content);
	}
	
	@PostConstruct
	private void init() {
		refresh();
	}
	
	private void refresh() {
		cameraGrid.setItems(deviceService.findCameras());
		newContent();
	}
	
	private Grid<Camera> createCameraGrid() {
		cameraGrid = new Grid<>(Camera.class);
		cameraGrid.setWidthFull();
		cameraGrid.setHeight(CommonConstants.gridHeight);
		cameraGrid.setColumns("title", "macAddress", "ipAddress", "liveLink");
		cameraGrid.addComponentColumn(camera -> new Button("View", e -> {
			openViewDialog(camera);
		}));
		cameraGrid.addComponentColumn(camera -> new Button("Send", e -> {
			openSendDialog(camera);
		}));
		cameraGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<Camera>, Camera> cameraSelect = cameraGrid.asSingleSelect();
		cameraSelect.addValueChangeListener(e -> setForm(e.getValue()));
		cameraGrid.getColumns().forEach(column -> {
			if(StringUtils.isEmpty(column.getWidth())) {
				column.setAutoWidth(true);
			}
		});
		return cameraGrid;
	}
	
	private FormLayout createFormLayout() {
		FormLayout form = new FormLayout();
		form.setMaxWidth(CommonConstants.editorMaxWidth);
		cameraBinder = new Binder<>(Camera.class);
		
		TextField titleField = new TextField("Title");
		cameraBinder.bind(titleField, "title");
		TextField macField = new TextField("MAC Address");
		cameraBinder.bind(macField, "macAddress");
		TextField ipField = new TextField("IP Address");
		cameraBinder.bind(ipField, "ipAddress");
		TextField liveField = new TextField("Live Link");
		cameraBinder.bind(liveField, "liveLink");
		deviceField = new DeviceField("Device");
		cameraBinder.bind(deviceField, "device");
		buttonLayout = new HorizontalLayout();
		buttonLayout.setWidthFull();
		buttonLayout.setMargin(true);
		buttonLayout.setSpacing(true);
		saveButton = new Button("Save", e -> {
			save();
		});
		deleteButton = new Button("Delete", e -> {
			delete();
		});
		buttonLayout.addAndExpand(saveButton, deleteButton);
		form.add(titleField, macField, ipField, liveField, deviceField, buttonLayout);
		return form;
	}

	@Override
	public void onClickEvent(String buttonTitle) {
		switch(buttonTitle) {
		case "New" :
			newContent();
			break;
		}
	}

	@Override
	public String getTitle() {
		return "IP Camera";
	}

	@Override
	public String[] getToolButtonTitles() {
		return new String[] {"New"};
	}
	
	private void setForm(Camera camera) {
		if(content != null) {
			cameraBinder.setBean(camera);
		} else {
			cameraBinder.setBean(new Camera());
		}
	}
	
	private void save() {
		Camera camera = cameraBinder.getBean();
		try {
			deviceService.saveCamera(camera);
			UiUtils.notifyOk("save");
			cameraGrid.deselectAll();
		} catch (Throwable ex) {
			UiUtils.notifyFail("save");
		}
		refresh();
	}
	
	private void newContent() {
		cameraGrid.deselectAll();
		Camera camera = new Camera();
		setForm(camera);
	}

	private void delete() {
		Camera camera = cameraBinder.getBean();
		if(camera.getCameraId() != null) {
			try {
				deviceService.deleteCamera(camera);
				UiUtils.notifyOk("delete");
			} catch (Throwable ex) {
				UiUtils.notifyFail("delete");
			}
			refresh();
		}
	}
	
	private void openSelectDialog(DeviceField field, Camera camera) {
		Grid<Device> deviceGrid = createDeviceGrid();
		deviceGrid.setItems(deviceService.findDevices());
		SignageDialog dialog = new SignageDialog("Select a device", deviceGrid);
		dialog.addButtons(
		new Button("Select", e -> {
			Set<Device> devices = deviceGrid.getSelectedItems();
			if(devices.size() > 0) {
				for (Device device : devices) {
					camera.setDevice(device);
				}
				
				field.selectButtonDevice(camera.getDevice());
				dialog.close();
			} else {
				ConfirmDialog confirm = new ConfirmDialog("No Selected",
				        "Must select a device.", "OK", ok -> {
				        	ok.getSource().close();
				        });
				confirm.open();
			}
		}),
		new Button("Close", e -> dialog.close()));
		dialog.open();
	}
	
	private void openViewDialog(Camera camera) {
		VideoPlayer player = new VideoPlayer(camera.getLiveLink());
		SignageDialog dialog = new SignageDialog("Send " + camera.getTitle() + "'s streaming", player);
		dialog.addButtons(new Button("Close", e -> dialog.close()));
		dialog.open();
	}
	
	private void openSendDialog(Camera camera) {
		Grid<Device> deviceGrid = createDeviceGrid();
		deviceGrid.setItems(deviceService.findDevices());
		SignageDialog dialog = new SignageDialog("Send " + camera.getTitle() + "'s streaming", deviceGrid);
		dialog.addButtons(
		new Button("Send", e -> {
			sendStreaming(deviceGrid.getSelectedItems(), camera);
			dialog.close();
		}),
		new Button("Close", e -> dialog.close()));
		dialog.open();
	}
	
	private Grid<Device> createDeviceGrid() {
		Grid<Device> deviceGrid = new Grid<>(Device.class);
		deviceGrid.setWidthFull();
		deviceGrid.setHeight(CommonConstants.gridHeight);
		deviceGrid.setColumns("title", "serialNumber", "ipAddress", "type");
		deviceGrid.setSelectionMode(SelectionMode.SINGLE);
		return deviceGrid;
	}
	
	private boolean sendStreaming(Set<Device> target, Camera camera) {
		if(target.size() > 0) {
			for (Device device : target) {
				deviceService.asyncSendMedia(device, camera.getLiveLink());
			}
			
			return true;
		} else {
			ConfirmDialog dialog = new ConfirmDialog("No Selected",
			        "Must select a device.", "OK", e -> {
			        	e.getSource().close();
			        });
			dialog.open();
			
			return false;
		}
	}
	
	public class DeviceField extends HorizontalLayout implements HasValue<DeviceValueChangeEvent, Device> {

		private static final long serialVersionUID = 1L;

		private String label;
		private boolean required;
		private TextField deviceField;
		private Button deviceButton;
		
		private Device value;
		private List<ValueChangeListener<? super DeviceValueChangeEvent>> listeners;
		
		public DeviceField(String label) {
			this.label = label;
			this.listeners = new ArrayList<>();
			
			setWidthFull();
			setMargin(false);
			setPadding(false);
			setSpacing(true);
			deviceField = new TextField(label);
			deviceField.setReadOnly(true);
			expand(deviceField);
			deviceButton = new Button("Select");
			deviceButton.addClickListener(e -> openSelectDialog(this, cameraBinder.getBean()));
			setVerticalComponentAlignment(FlexComponent.Alignment.END, deviceButton);
			add(deviceField, deviceButton);
		}
		
		public String getLabel() {
			return label;
		}
		
		public void selectButtonDevice(Device value) {
			setValue(value, true);
		}
		
		@Override
		public void setValue(Device value) {
			setValue(value, false);
		}
		
		public void setValue(Device value, boolean fromClient) {
			Device oldValue = this.value;
			this.value = value;
			if(oldValue != value) {
				deviceField.setValue(value == null ? "" : StringUtils.trimToEmpty(value.getTitle()));
				fireEvent(oldValue, value, fromClient);
			}
		}

		private void fireEvent(Device oldValue, Device value, boolean fromClient) {
			for (ValueChangeListener<? super DeviceValueChangeEvent> listener : listeners) {
				listener.valueChanged(new DeviceValueChangeEvent(this, oldValue, value, fromClient));
			}
		}

		@Override
		public Device getValue() {
			return value;
		}

		@Override
		public Registration addValueChangeListener(ValueChangeListener<? super DeviceValueChangeEvent> listener) {
			listeners.add(listener);
			return () -> {
				listeners.remove(listener);
			};
		}

		@Override
		public void setReadOnly(boolean readOnly) {
			deviceButton.setEnabled(!readOnly);
		}

		@Override
		public boolean isReadOnly() {
			return !deviceButton.isEnabled();
		}

		@Override
		public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
			this.required = requiredIndicatorVisible;
		}

		@Override
		public boolean isRequiredIndicatorVisible() {
			return required;
		}		
	}
	
	public class DeviceValueChangeEvent implements ValueChangeEvent<Device> {

		private static final long serialVersionUID = 1L;

		private DeviceField field;
		private Device oldValue;
		private Device value;
		private boolean fromClient;
		
		public DeviceValueChangeEvent(DeviceField field, Device oldValue, Device value, boolean fromClient) {
			this.field = field;
			this.oldValue = oldValue;
			this.value = value;
			this.fromClient = fromClient;
		}
		
		@Override
		public HasValue<?, Device> getHasValue() {
			return field;
		}

		@Override
		public boolean isFromClient() {
			return fromClient;
		}

		@Override
		public Device getOldValue() {
			return oldValue;
		}

		@Override
		public Device getValue() {
			return value;
		}
		
	}
}
