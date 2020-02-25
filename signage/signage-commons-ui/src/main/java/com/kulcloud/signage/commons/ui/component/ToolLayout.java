package com.kulcloud.signage.commons.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.kulcloud.signage.commons.ui.UiUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ToolLayout extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private Div titleDiv;
	private HorizontalLayout toolButtons;
	
	private List<ToolClickListener> listeners = new ArrayList<>();
	
	public ToolLayout() {
		super();
		setPadding(false);
		setSpacing(false);
		
		HorizontalLayout toolLayout = new HorizontalLayout();
		toolLayout.setMargin(false);
		toolLayout.setWidthFull();
		toolLayout.setHeight("50px");
		toolLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
		toolLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		
		titleDiv = new Div();
		titleDiv.getStyle().set("margin-left", "30px");
		toolLayout.expand(titleDiv);
		toolLayout.add(titleDiv);
		
		toolButtons = new HorizontalLayout();
		toolButtons.setSpacing(true);
		toolButtons.setPadding(true);
		toolLayout.add(toolButtons);
		
		add(toolLayout);
		add(UiUtils.createLine());
	}

	public void setTitle(String title) {
		titleDiv.setText(title);
	}
	
	public String getTitle() {
		return titleDiv.getText();
	}
	
	public void addToolClickListener(ToolClickListener listener) {
		listeners.add(listener);
	}
	
	public void removeToolClickListener(ToolClickListener listener) {
		listeners.remove(listener);
	}
	
	public void addButton(String...buttonTitles) {
		if(buttonTitles != null) {
			for (String buttonTitle : buttonTitles) {
				Button button = new Button(buttonTitle, e -> fireToolClickEvent(buttonTitle));
				toolButtons.add(button);
			}
		}
	}
	
	private void fireToolClickEvent(String buttonTitle) {
		for (ToolClickListener listener : listeners) {
			listener.onClickEvent(buttonTitle);
		}
	}
}
