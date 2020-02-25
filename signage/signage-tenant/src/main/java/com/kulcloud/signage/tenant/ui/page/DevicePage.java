package com.kulcloud.signage.tenant.ui.page;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.commons.ui.PageView;
import com.kulcloud.signage.commons.ui.UiUtils;
import com.kulcloud.signage.commons.ui.component.ContentLayout;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.tenant.content.ContentService;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceChangedListener;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.kulcloud.signage.tenant.device.DeviceService;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;
import com.kulcloud.signage.tenant.ui.MainView;
import com.kulcloud.signage.tenant.ui.component.player.DevicePlayController;
import com.kulcloud.signage.tenant.ui.component.player.DevicePlayListener;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "", layout = MainView.class)
@RouteAlias("device")
@PageView(title="Device", icon="SITEMAP")
@PageTitle("Signage Devices")
public class DevicePage extends ContentLayout implements DevicePlayListener, DeviceChangedListener {

	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private HorizontalLayout content;
	private Grid<Device> deviceGrid = new Grid<>(Device.class);
	private Binder<Device> deviceBinder;
	private FormLayout configEditor;
	private DeviceConfigDiv configDiv;
	private Div controlDiv;
	private DevicePlayController player;
	private Button saveButton;
	private Button deleteButton;
	
	private ContentService contentService;
	private DeviceServiceManager deviceService;
	
	@Autowired
	public DevicePage(DeviceServiceManager deviceService, ContentService contentService) {
		super();
		this.deviceService = deviceService;
		this.contentService = contentService;
		
		content = new HorizontalLayout();
		content.setPadding(true);
		content.setSpacing(true);
		content.setWidthFull();
		content.setHeightFull();
		content.add(createDeviceGrid(), createEditComponent());
		content.expand(deviceGrid);
		setContent(content);
	}
	
	@PostConstruct
	private void init() {
		deviceService.addDeviceChangedListener(this);
		refresh();
	}
	
	@PreDestroy
	private void destroy() {
		deviceService.removeDeviceChangedListener(this);
	}
	
	private Grid<Device> createDeviceGrid() {
		deviceGrid = new Grid<>(Device.class);
		deviceGrid.setWidthFull();
		deviceGrid.setHeight(CommonConstants.gridHeight);
		deviceGrid.setColumns("title", "serialNumber", "ipAddress", "type", "health");
		deviceGrid.addComponentColumn(device -> new Button("Send", e -> {
			openSendDialog(device);
		}));
		deviceGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<Device>, Device> deviceSelect = deviceGrid.asSingleSelect();
		deviceSelect.addValueChangeListener(e -> setForm(e.getValue()));
		deviceGrid.getColumns().forEach(column -> column.setAutoWidth(true));
		return deviceGrid;
	}
	
	private Component createEditComponent() {
		VerticalLayout editor = new VerticalLayout();
		editor.setMaxWidth(CommonConstants.editorMaxWidth);
		Tab editorTab = new Tab("Config");
		Tab controlTab = new Tab("Control");
		Tabs tabs = new Tabs(editorTab, controlTab);
		tabs.setWidthFull();
		tabs.setFlexGrowForEnclosedTabs(1);
		Div tabPages = new Div();
		tabPages.setWidthFull();
		configEditor = createConfigFormLayout();
		controlDiv= new Div();
		controlDiv.setWidthFull();
		controlDiv.setVisible(false);
		player = new DevicePlayController();
		player.addDevicePlayListener(this);
		controlDiv.add(player);
		tabPages.add(configEditor, controlDiv);
		tabs.addSelectedChangeListener(e -> {
			configEditor.setVisible(tabs.getSelectedTab() == editorTab);
			controlDiv.setVisible(tabs.getSelectedTab() == controlTab);
		});
		editor.add(tabs, tabPages);
		return editor;
	}
	
	private FormLayout createConfigFormLayout() {
		FormLayout form = new FormLayout();
		form.setWidthFull();
		deviceBinder = new Binder<>(Device.class);
		
		TextField titleField = new TextField("Title");
		deviceBinder.bind(titleField, "title");
		TextField serialField = new TextField("Serial No.");
		deviceBinder.bind(serialField, "serialNumber");
		TextField ipField = new TextField("IP Address");
		deviceBinder.bind(ipField, "ipAddress");
		RadioButtonGroup<String> typeField = new RadioButtonGroup<>();
		typeField.setLabel("Type");
		typeField.setItems(DeviceType.names());
		deviceBinder.bind(typeField, "type");
		configDiv = new DeviceConfigDiv();
		typeField.addValueChangeListener(e -> {
			configDiv.setConfig(e.getValue(), deviceBinder.getBean().getConfig());
		});
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
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
		form.add(titleField, serialField, ipField, typeField, configDiv, buttonLayout);
		return form;
	}

	@Override
	public void onClickEvent(String buttonTitle) {
		switch(buttonTitle) {
		case "Discovery" :
			discovery();
			break;
		case "New" :
			newDevice();
			break;
		}
	}

	@Override
	public String getTitle() {
		return "Device";
	}

	@Override
	public String[] getToolButtonTitles() {
		return new String[] {"Discovery", "New"};
	}

	private void newDevice() {
		Device device = new Device();
		setForm(device);
		deviceGrid.deselectAll();
	}
	
	private void setForm(Device device) {
		if(device != null) {
			deviceBinder.setBean(device);
			configDiv.setConfig(device.getType(), device.getConfig());
			player.setEnabled(device.getDeviceId() != null);
			player.setData(device.getControls());
		}
	}
	
	private void save() {
		Device device = deviceBinder.getBean();
		try {
			deviceService.saveDevice(device);
			UiUtils.notifyOk("save");
		
		} catch (Throwable ex) {
			UiUtils.notifyFail("save");
		}
		refresh();
	}
	
	private void delete() {
		Device device = deviceBinder.getBean();
		if(device.getDeviceId() != null) {
			try {
				deviceService.deleteDevice(device);
				UiUtils.notifyOk("delete");
			} catch (Throwable ex) {
				UiUtils.notifyFail("delete");
			}
			refresh();
		}
	}
	
	private void discovery() {
		final UI ui = UI.getCurrent();
		if(ui != null) {
			deviceService.startDiscovery();
			Notification.show("Discovering", 15000, Position.MIDDLE);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					deviceService.stopDiscoery();
					ui.access(() -> {
						refresh();
					});
				}
				
			}, 10000);
		}
	}
	
	private void refresh() {
		newDevice();
		deviceGrid.setItems(deviceService.findDevices());
	}
	
	private void openSendDialog(Device device) {
		Grid<Content> contentGrid = createContentGrid();
		contentGrid.setItems(contentService.getContentList());
		SignageDialog dialog = new SignageDialog("Send to " + device.getTitle(), contentGrid);
		dialog.addButtons(
		new Button("Send", e -> {
			sendContent(contentGrid.getSelectedItems(), device);
			dialog.close();
		}),
		new Button("Close", e -> dialog.close()));
		dialog.open();
	}
	
	private Grid<Content> createContentGrid() {
		Grid<Content> contentGrid = new Grid<>(Content.class);
		contentGrid.setWidthFull();
		contentGrid.setHeight(CommonConstants.gridHeight);
		contentGrid.setColumns("name", "title", "type", "url");
		contentGrid.setSelectionMode(SelectionMode.SINGLE);
		return contentGrid;
	}
	
	private void sendContent(Set<Content> contents, Device target) {
		if(contents.size() > 0) {
			for (Content content : contents) {
				deviceService.asyncSend(target, content);
			}
		} else {
			ConfirmDialog dialog = new ConfirmDialog("No Selected",
			        "Must select a content.", "OK", e -> {
			        	e.getSource().close();
			        	openSendDialog(target);
			        });
			dialog.open();
		}
	}
	
	class DeviceConfigDiv extends Div {

		private static final long serialVersionUID = 1L;

		public DeviceConfigDiv() {
			super();
			setWidthFull();
		}
		
		public void setConfig(String type, Map<String, Object> config) {
			List<DeviceMeta> configInfo = deviceService.getDeviceMeta(DeviceType.valueOf(type), "config");
			DeviceMetaLayout configLayout = new DeviceMetaLayout(configInfo);
			configLayout.setValues(config);
		}
	}
	
	@Override
	public void play() {
		DeviceController controller = getDeviceController();
		if(controller != null) {
			controller.control("play", true);
		}
	}

	@Override
	public void pause() {
		DeviceController controller = getDeviceController();
		if(controller != null) {
			controller.control("pause", true);
		}
	}

	@Override
	public void stop() {
		DeviceController controller = getDeviceController();
		if(controller != null) {
			controller.control("play", false);
		}
	}

	@Override
	public void changeVolume(double volume) {
		DeviceController controller = getDeviceController();
		if(controller != null) {
			controller.control("volume", volume);
		}
	}

	@Override
	public void changeMute(boolean mute) {
		DeviceController controller = getDeviceController();
		if(controller != null) {
			controller.control("mute", mute);
		}
	}

	private DeviceController getDeviceController() {
		Device device = deviceBinder.getBean();
		if(device == null || device.getDeviceId() != null) {
			DeviceType type = DeviceType.valueOf(device.getType());
			if(type != null) {
				DeviceService service = deviceService.getDeviceService(type);
				return service.getDeviceController(device);
			}
		}

		return null;
	}

	@Override
	public void changeDeviceStatus(Device device) {
		if(getUI().isPresent()) {
			try {
				getUI().get().access(() -> {
					DataProvider<Device, ?> provider = deviceGrid.getDataProvider();
					provider.refreshItem(device);
					Device bindDevice = deviceBinder.getBean();
					if(bindDevice != null && bindDevice.getDeviceId() != null && 
							bindDevice.getDeviceId().longValue() == device.getDeviceId().longValue()) {
						setForm(device);
					}
				});
			} catch (Throwable ex) {
				logger.warn(ex.getMessage());
			}
		}
	}
}
