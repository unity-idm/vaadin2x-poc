package io.imunity.prototypes.runner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class JarGetter {
	static List<String> vaadinJars = List.of(
		"flute",
		"gwt-elemental",
		"atmosphere-runtime",
		"vaadin-slf4j-jdk14",
		"atmosphere-runtime",
		"gentyref",
		"flow-client",
		"flow-data",
		"flow-dnd",
		"flow-html-components",
		"flow-lit-template",
		"flow-polymer-template",
		"flow-push",
		"flow-server",
		"license-checker",
		"vaadin-accordion-flow",
		"vaadin-app-layout-flow",
		"vaadin-avatar-flow",
		"vaadin-button-flow",
		"vaadin-checkbox-flow",
		"vaadin-client-compiled",
		"vaadin-combo-box-flow",
		"vaadin-context-menu-flow",
		"vaadin-core",
		"vaadin-custom-field-flow",
		"vaadin-date-picker-flow",
		"vaadin-date-time-picker-flow",
		"vaadin-details-flow",
		"vaadin-dev-server",
		"vaadin-dialog-flow",
		"vaadin-form-layout-flow",
		"vaadin-grid-flow",
		"vaadin-icons-flow",
		"vaadin-iron-list-flow",
		"vaadin-list-box-flow",
		"vaadin-login-flow",
		"vaadin-lumo-theme",
		"vaadin-material-theme",
		"vaadin-menu-bar-flow",
		"vaadin-messages-flow",
		"vaadin-notification-flow",
		"vaadin-ordered-layout-flow",
		"vaadin-progress-bar-flow",
		"vaadin-push",
		"vaadin-radio-button-flow",
		"vaadin-renderer-flow",
		"vaadin-sass-compiler",
		"vaadin-select-flow",
		"vaadin-server",
		"vaadin-shared",
		"vaadin-split-layout-flow",
		"vaadin-tabs-flow",
		"vaadin-text-field-flow",
		"vaadin-themes",
		"vaadin-time-picker-flow",
		"vaadin-upload-flow",
		"vaadin-virtual-list-flow"
	);

	static String getJarsRegex(Set<String> classPathElements){
		return vaadinJars.stream().collect(Collectors.joining(".*|.*", "(.*", ".*|")) +
			classPathElements.stream().collect(Collectors.joining("|", "", ")"));
	}
}
