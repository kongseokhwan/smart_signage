package com.kulcloud.signage.cms.ui.page;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.cms.html.data.HtmlContent;
import com.kulcloud.signage.cms.html.data.HtmlContentRepository;
import com.kulcloud.signage.cms.ui.MainView;
import com.kulcloud.signage.cms.user.SignageTenant;
import com.kulcloud.signage.cms.user.SignageTenantRepository;
import com.kulcloud.signage.commons.ui.PageView;
import com.kulcloud.signage.commons.ui.UiUtils;
import com.kulcloud.signage.commons.ui.component.ContentLayout;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "html", layout = MainView.class)
@PageView(title="Html Contents", icon="GLOBE_WIRE")
@PageTitle("Html Contents")
public class HtmlPage extends ContentLayout {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private HtmlContentRepository contentRepo;
	@Autowired
	private SignageTenantRepository tenantRepo;
	
	private Grid<HtmlContent> contentGrid = new Grid<>(HtmlContent.class);
	private Binder<HtmlContent> contentBinder;
	private ComboBox<SignageTenant> tenantField;

	@PostConstruct
	public void init() {
		setContent(createContentAreaWithTenant());
		refresh();
	}
	
	private void refresh() {
		SignageTenant tenant = tenantField.getValue();
		if(tenant != null) {
			contentGrid.setItems(contentRepo.findByIdTenantId(tenant.getTenantId()));
		}
	}
	
	private VerticalLayout createContentAreaWithTenant() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidthFull();
		layout.setHeightFull();
		layout.setPadding(true);
		layout.setSpacing(false);
		
		HorizontalLayout fieldLayout = new HorizontalLayout();
		fieldLayout.setWidthFull();
		fieldLayout.setPadding(false);
		fieldLayout.setSpacing(true);
		fieldLayout.setDefaultVerticalComponentAlignment(
		        FlexComponent.Alignment.CENTER);
		
		tenantField = new ComboBox<>();
		tenantField.setPlaceholder("Tenant");
		tenantField.setItemLabelGenerator(new ItemLabelGenerator<SignageTenant>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String apply(SignageTenant tenant) {
				return tenant.getTenantId();
			}
		});
		
		List<SignageTenant> tenants = tenantRepo.findAll();
		tenantField.setItems(tenants);
		if(tenants.size() >0) {
			tenantField.setValue(tenants.get(0));
		}
		tenantField.addValueChangeListener(e -> refresh());
		
		fieldLayout.add(tenantField);
		
		layout.add(fieldLayout, createContentArea());
		
		return layout;
	}
	
	private HorizontalLayout createContentArea() {
		HorizontalLayout content = new HorizontalLayout();
		content.setPadding(false);
		content.setSpacing(true);
		content.setWidthFull();
		content.setHeightFull();
		content.add(createContentGrid(), createFormLayout());
		content.expand(contentGrid);
		
		return content;
	}
	private Grid<HtmlContent> createContentGrid() {
		contentGrid = new Grid<>(HtmlContent.class);
		contentGrid.setWidthFull();
		contentGrid.setHeight(CommonConstants.innerGridHeight);
		contentGrid.setColumns("name", "title", "url", "link");
		contentGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<HtmlContent>, HtmlContent> contentSelect = contentGrid.asSingleSelect();
		contentSelect.addValueChangeListener(e -> setForm(e.getValue()));
		contentGrid.getColumns().forEach(column -> column.setAutoWidth(true));
		return contentGrid;
	}
	
	private FormLayout createFormLayout() {
		FormLayout form = new FormLayout();
		form.setMaxWidth(CommonConstants.editorMaxWidth);
		contentBinder = new Binder<>(HtmlContent.class);
		
		TextField nameField = new TextField("Name");
		contentBinder.bind(nameField, "name");
		TextField titleField = new TextField("Title");
		contentBinder.bind(titleField, "title");
		TextArea descriptionField = new TextArea("Description");
		contentBinder.bind(descriptionField, "description");
		TextField urlField = new TextField("URL");
		contentBinder.bind(urlField, "url");
		RadioButtonGroup<Boolean> builtinField = new RadioButtonGroup<>();
		builtinField.setLabel("Builtin");
		builtinField.setItems(true, false);
		contentBinder.bind(builtinField, "builtin");
		
		RadioButtonGroup<Boolean> linkField = new RadioButtonGroup<>();
		linkField.setLabel("Link");
		linkField.setItems(true, false);
		contentBinder.bind(linkField, "link");
		
		form.add(nameField, titleField, descriptionField, urlField, builtinField, linkField);
		UiUtils.setReadOnly(form, true);
		
		return form;
	}

	@Override
	public void onClickEvent(String buttonTitle) {
		// ignore
	}

	@Override
	public String getTitle() {
		return "Html Contents";
	}

	@Override
	public String[] getToolButtonTitles() {
		return null;
	}
	
	private void setForm(HtmlContent content) {
		if(content != null) {
			contentBinder.setBean(content);
		}
	}
}
