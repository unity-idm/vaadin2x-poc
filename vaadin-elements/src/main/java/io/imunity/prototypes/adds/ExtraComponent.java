package io.imunity.prototypes.adds;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./styles/view/extra-text-field.css")
public class ExtraComponent extends HorizontalLayout {
	public ExtraComponent() {
		add(new TextField("Adds Text Field"));
	}
}
