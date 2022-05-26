package io.imunity.prototypes.adds;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./styles/view/adds-text-field.css")
public class AddsComponent extends HorizontalLayout {
	public AddsComponent() {
		add(new TextField("Adds Text Field"));
	}
}
