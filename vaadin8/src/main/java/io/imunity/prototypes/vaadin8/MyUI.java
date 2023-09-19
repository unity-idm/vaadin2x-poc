package io.imunity.prototypes.vaadin8;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.risto.stepper.IntStepper;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@Widgetset("io.imunity.prototypes.widgets.customWidgetset")
public class MyUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		Button button = new Button("Click me");
		button.addClickListener(x -> Notification.show("This is the caption",
			"This is the description",
			Notification.Type.HUMANIZED_MESSAGE));

		HorizontalLayout main = new HorizontalLayout(button, new IntStepper());
		main.setSizeFull();

		layout.addComponents(main);
		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
	public static class MyUIServlet extends VaadinServlet {
		@Override
		protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
			VaadinServletService service = new VaadinServletServiceWithoutAtmosphere(this, deploymentConfiguration);
			service.init();
			return service;
		}
	}
}
