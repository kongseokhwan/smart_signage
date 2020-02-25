package com.kulcloud.signage.commons.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

public class UiUtils {

	public static Div createLine() {
		Div line = new Div();
		line.setWidthFull();
		line.setHeight("1px");
		line.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
		
		return line;
	}
	
	public static void alert(String title, String message, ComponentEventListener<ConfirmEvent> confirmListener) {
		ConfirmDialog dialog = new ConfirmDialog(title, message, "OK", confirmListener);
		dialog.open();
	}
	
	public static void notifyOk(String taskName) {
		Notification.show(String.format("Successful %s!", taskName), 2000, Position.MIDDLE);
	}
	
	public static void notifyFail(String taskName) {
		Notification.show(String.format("Cannot %s.", taskName), 2000, Position.MIDDLE);
	}
	
	public static void setReadOnly(Component component, boolean readOnly) {
		component.getChildren().forEach(action -> {
			if(action instanceof HasValue) {
				((HasValue) action).setReadOnly(readOnly);
			}
			
			setReadOnly(action, readOnly);
		});
	}
}
