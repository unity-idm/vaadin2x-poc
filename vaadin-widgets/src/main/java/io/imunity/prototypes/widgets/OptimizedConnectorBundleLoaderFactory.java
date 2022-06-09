/*
 * Copyright (c) 2015 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE.txt file for licensing information.
 */
package io.imunity.prototypes.widgets;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

import java.util.HashSet;
import java.util.Set;

public class OptimizedConnectorBundleLoaderFactory extends ConnectorBundleLoaderFactory {

	private Set<String> eagerConnectors = new HashSet<>();
	{
		eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.combobox.ComboBoxConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.textfield.TextFieldConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.panel.PanelConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.passwordfield.PasswordFieldConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.gridlayout.GridLayoutConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.label.LabelConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.connectors.grid.GridConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.customcomponent.CustomComponentConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.formlayout.FormLayoutConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.connectors.grid.TextRendererConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.connectors.ImageRendererConnector.class.getName());
		eagerConnectors.add(com.vaadin.client.ui.checkbox.CheckBoxConnector.class.getName());
	}

	@Override
	protected LoadStyle getLoadStyle(JClassType connectorType) {
		if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
			return LoadStyle.EAGER;
		} else {
			return LoadStyle.LAZY;
		}
	}
}
