package com.kulcloud.signage.tenant.ui.page;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.ui.PageView;
import com.kulcloud.signage.commons.ui.UiUtils;
import com.kulcloud.signage.commons.ui.component.ContentLayout;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.tenant.content.ContentService;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;
import com.kulcloud.signage.tenant.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "desktop", layout = MainView.class)
@PageView(title="Desktop", icon="FILE_TEXT_O")
@PageTitle("Remote Desktop")
public class DesktopPage extends ContentLayout {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ContentService contentService;
	@Autowired
	private DeviceServiceManager deviceService;
	
	private HorizontalLayout content;
	private Grid<Content> contentGrid = new Grid<>(Content.class);
	private Binder<Content> contentBinder;
	private FileBuffer uploadBuffer;
	private Upload uploadFile;
	private HorizontalLayout buttonLayout;
	private Button saveButton;
	private Button deleteButton;
	private Label builtinMessage;

	public DesktopPage() {
		super();
		content = new HorizontalLayout();
		content.setPadding(true);
		content.setSpacing(true);
		content.setWidthFull();
		content.setHeightFull();
		content.add(createContentGrid(), createFormLayout());
		content.expand(contentGrid);
		setContent(content);
	}
	
	@PostConstruct
	private void init() {
		refresh();
	}
	
	private void refresh() {
		contentGrid.setItems(contentService.getContentList());
		newContent();
	}
	
	private Grid<Content> createContentGrid() {
		contentGrid = new Grid<>(Content.class);
		contentGrid.setWidthFull();
		contentGrid.setHeight(CommonConstants.gridHeight);
		contentGrid.setColumns("name", "title", "type", "url");
		contentGrid.addComponentColumn(content -> new Button("Send", e -> {
			openSendDialog(content);
		}));
		contentGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<Content>, Content> contentSelect = contentGrid.asSingleSelect();
		contentSelect.addValueChangeListener(e -> setForm(e.getValue()));
		contentGrid.getColumns().forEach(column -> column.setAutoWidth(true));
		return contentGrid;
	}
	
	private FormLayout createFormLayout() {
		FormLayout form = new FormLayout();
		form.setMaxWidth(CommonConstants.editorMaxWidth);
		contentBinder = new Binder<>(Content.class);
		
		TextField nameField = new TextField("Name");
		contentBinder.bind(nameField, "name");
		TextField titleField = new TextField("Title");
		contentBinder.bind(titleField, "title");
		TextArea descriptionField = new TextArea("Description");
		contentBinder.bind(descriptionField, "description");
		TextField urlField = new TextField("URL");
		contentBinder.bind(urlField, "url");
		ComboBox<String> typeField = new ComboBox<>("Type");
		typeField.setItems(ContentType.names());
		typeField.addValueChangeListener(e -> {
			if(!StringUtils.isEmpty(e.getValue())) {
				switch(e.getValue()) {
				case "vod": 
					enableFileUpload(true);
					break;
				default:
					enableFileUpload(false);
					break;
				}
			} else {
				enableFileUpload(false);
			}
		});
		contentBinder.bind(typeField, "type");
		uploadFile = new Upload();
		uploadFile.setWidthFull();
		
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
		builtinMessage = new Label("This is a builtin content.");
		buttonLayout.addAndExpand(saveButton, deleteButton, builtinMessage);
		form.add(nameField, titleField, descriptionField, urlField, typeField, uploadFile, buttonLayout);
		return form;
	}

	private void enableFileUpload(boolean enable) {
		uploadFile.setVisible(enable);
		if(enable) {
			uploadBuffer = new FileBuffer();
			uploadFile.setReceiver(uploadBuffer);
		} else {
			uploadBuffer = null;
		}
	}

	@Override
	public void onClickEvent(String buttonTitle) {
		switch(buttonTitle) {
		case "New" :
			newContent();
			break;
		case "Sync" :
			if(contentService.syncContentList()) {
				UiUtils.notifyOk("sync");
				init();
			} else {
				UiUtils.notifyFail("sync");
			}
		}
	}

	@Override
	public String getTitle() {
		return "Contents";
	}

	@Override
	public String[] getToolButtonTitles() {
		return new String[] {"New", "Sync"};
	}
	
	private void setForm(Content content) {
		if(content != null) {
			contentBinder.setBean(content);
			enableFileUpload(!content.isBuiltin());
			enableFileUpload(StringUtils.equals(content.getType(), ContentType.vod.name()));
			saveButton.setVisible(!content.isBuiltin());
			deleteButton.setVisible(!StringUtils.isEmpty(content.getContentId()));
			deleteButton.setVisible(!content.isBuiltin());
			builtinMessage.setVisible(content.isBuiltin());
		}
	}
	
	private void save() {
		Content content = contentBinder.getBean();
		if(contentService.save(content, uploadBuffer)) {
			UiUtils.notifyOk("save");
		} else {
			UiUtils.notifyFail("save");
		}
		
		refresh();
	}
	
	private void newContent() {
		Content content = new Content();
		enableFileUpload(false);
		setForm(content);
	}

	private void delete() {
		Content content = contentBinder.getBean();
		if(content.isBuiltin()) {
			UiUtils.alert("No Delete", "Cannot delete a builtin content.", null);
		} else {
			if(!StringUtils.isEmpty(content.getContentId())) {
				if(contentService.deleteContent(content)) {
					UiUtils.notifyOk("delete");
					newContent();
				} else {
					UiUtils.notifyFail("delete");
				}
			}
		}
		
		refresh();
	}
	
	private void openSendDialog(Content content) {
		Grid<Device> deviceGrid = createDeviceGrid();
		deviceGrid.setItems(deviceService.findDevices());
		SignageDialog dialog = new SignageDialog("Send " + content.getTitle(), deviceGrid);
		dialog.addButtons(
		new Button("Send", e -> {
			sendContent(deviceGrid.getSelectedItems(), content);
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
		deviceGrid.setSelectionMode(SelectionMode.MULTI);
		return deviceGrid;
	}
	
	private void sendContent(Set<Device> target, Content content) {
		if(target.size() > 0) {
			for (Device device : target) {
				deviceService.asyncSend(device, content);
			}
		} else {
			ConfirmDialog dialog = new ConfirmDialog("No Selected",
			        "Must select a device.", "OK", e -> {
			        	e.getSource().close();
			        	openSendDialog(content);
			        });
			dialog.open();
		}
	}
}
