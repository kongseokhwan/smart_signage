package com.kulcloud.signage.commons.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class ContentLayout extends VerticalLayout implements ToolClickListener{

	private static final long serialVersionUID = 1L;
	
	private ToolLayout toolLayout;
	private VerticalLayout contentLayout;

	public ContentLayout() {
		super();
		setPadding(false);
		setSpacing(false);
		
		toolLayout = new ToolLayout();
		toolLayout.setTitle(getTitle());
		toolLayout.addButton(getToolButtonTitles());
		toolLayout.addToolClickListener(this);
		add(toolLayout);
		
		contentLayout = new VerticalLayout();
		contentLayout.setPadding(false);
		contentLayout.setSpacing(false);
		contentLayout.setSizeFull();
		add(contentLayout);
	}

	public abstract String getTitle();
	public abstract String[] getToolButtonTitles();
	
	public void setContent(Component content) {
		contentLayout.removeAll();
		contentLayout.add(content);
	}
	
	public class SignageDialog extends Dialog {

		private static final long serialVersionUID = 1L;
		
		private HorizontalLayout buttonLayout;
		
		public SignageDialog(String title, Component content) {
			this(title, content, "800px", "600px");
		}
		public SignageDialog(String title, Component content, String width, String height) {
			setWidth(width);
			setHeight(height);
			VerticalLayout dialogLayout = new VerticalLayout();
			dialogLayout.setWidthFull();
			dialogLayout.setHeightFull();
			dialogLayout.setMargin(false);
			add(dialogLayout);
			dialogLayout.add(new Label(title), new Hr());
			dialogLayout.add(content);
			buttonLayout = new HorizontalLayout();
			buttonLayout.setWidthFull();
			buttonLayout.setMargin(false);
			buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
			dialogLayout.add(buttonLayout);
			setCloseOnEsc(false);
			setCloseOnOutsideClick(false);
		}
		
		public void addButtons(Button...buttons) {
			buttonLayout.add(buttons);
		}
	}
}
