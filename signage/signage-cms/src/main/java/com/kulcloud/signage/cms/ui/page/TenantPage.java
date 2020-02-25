package com.kulcloud.signage.cms.ui.page;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.cms.security.ApiKeyService;
import com.kulcloud.signage.cms.ui.MainView;
import com.kulcloud.signage.cms.user.SignageTenant;
import com.kulcloud.signage.cms.user.SignageTenantRepository;
import com.kulcloud.signage.commons.dto.KeyValue;
import com.kulcloud.signage.commons.ui.PageView;
import com.kulcloud.signage.commons.ui.UiUtils;
import com.kulcloud.signage.commons.ui.component.ContentLayout;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import io.jsonwebtoken.Claims;

@Route(value = "", layout = MainView.class)
@RouteAlias("tenant")
@PageView(title="Tenant", icon="USER")
@PageTitle("Signage CMS-Tenant")
public class TenantPage extends ContentLayout {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SignageTenantRepository tenantRepo;
	@Autowired
	private ApiKeyService keyService;
	
	private HorizontalLayout content;
	private Grid<SignageTenant> tenantGrid = new Grid<>(SignageTenant.class);
	private Binder<SignageTenant> tenantBinder;
	private Grid<KeyValue> claimsGrid;
	private SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public TenantPage() {
		super();
		content = new HorizontalLayout();
		content.setPadding(true);
		content.setSpacing(true);
		content.setWidthFull();
		content.setHeightFull();
		content.add(createTenantGrid(), createFormLayout());
		content.expand(tenantGrid);
		setContent(content);
	}
	
	@PostConstruct
	private void init() {
		refresh();
	}
	
	private void refresh() {
		tenantGrid.setItems(tenantRepo.findAll());
	}
	
	private Grid<SignageTenant> createTenantGrid() {
		tenantGrid = new Grid<>(SignageTenant.class);
		tenantGrid.setWidthFull();
		tenantGrid.setHeight(CommonConstants.gridHeight);
		tenantGrid.setColumns("tenantId", "ipAddress", "apiKey");
		tenantGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<SignageTenant>, SignageTenant> tenantSelect = tenantGrid.asSingleSelect();
		tenantSelect.addValueChangeListener(e -> setForm(e.getValue()));
		tenantGrid.getColumns().forEach(column -> column.setAutoWidth(true));
		return tenantGrid;
	}
	
	private FormLayout createFormLayout() {
		FormLayout form = new FormLayout();
		form.setMaxWidth("600px");
		tenantBinder = new Binder<>(SignageTenant.class);
		TextField idField = new TextField("Tenant ID");
		tenantBinder.bind(idField, "tenantId");
		TextField ipField = new TextField("IP Address");
		tenantBinder.bind(ipField, "ipAddress");
		TextArea keyField = new TextArea("API Key");
		tenantBinder.bind(keyField, "apiKey");
		
		claimsGrid = new Grid<>(KeyValue.class);
		claimsGrid.setWidthFull();
		claimsGrid.setHeight("202px");
		claimsGrid.setColumns("key", "value");
		claimsGrid.setSelectionMode(SelectionMode.SINGLE);
		claimsGrid.getColumns().forEach(column -> {
			column.setAutoWidth(true);
			column.setSortable(false);
		});
		
		form.add(idField, ipField, keyField, claimsGrid);
		UiUtils.setReadOnly(form, true);
		
		return form;
	}

	@Override
	public String getTitle() {
		return "Tenants";
	}

	@Override
	public String[] getToolButtonTitles() {
		return null;
	}
	
	private void setForm(SignageTenant tenant) {
		if(tenant != null) {
			tenantBinder.setBean(tenant);
			Claims claims = keyService.getClaims(tenant);
			if(claims == null) {
				claimsGrid.setItems(Collections.emptyList());
			} else {
				List<KeyValue> keyValues = CommonUtils.convertMapToKeyValues(claims);
				keyValues.add(new KeyValue("expire date", dtf.format(claims.getExpiration())));
				claimsGrid.setItems(keyValues);
			}
		}
	}

	@Override
	public void onClickEvent(String buttonTitle) {
		// ignore
	}
}
