/*
 * Copyright (c) 2013 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE.txt file for licensing information.
 */
package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import eu.unicore.util.configuration.ConfigurationException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.unity.JsonUtil;
import pl.edu.icm.unity.MessageSource;
import pl.edu.icm.unity.base.utils.Log;
import pl.edu.icm.unity.engine.api.authn.AbstractCredentialRetrieval;
import pl.edu.icm.unity.engine.api.authn.AbstractCredentialRetrievalFactory;
import pl.edu.icm.unity.engine.api.authn.AuthenticationException;
import pl.edu.icm.unity.engine.api.authn.AuthenticationResult;
import pl.edu.icm.unity.engine.api.authn.AuthenticationResult.Status;
import pl.edu.icm.unity.engine.api.authn.AuthenticationSubject;
import pl.edu.icm.unity.engine.api.authn.LocalAuthenticationResult;
import pl.edu.icm.unity.engine.api.utils.PrototypeComponent;
import pl.edu.icm.unity.stdext.credential.pass.PasswordCredentialResetSettings;
import pl.edu.icm.unity.stdext.credential.pass.PasswordExchange;
import pl.edu.icm.unity.stdext.credential.pass.PasswordVerificator;
import pl.edu.icm.unity.stdext.identity.EmailIdentity;
import pl.edu.icm.unity.stdext.identity.UsernameIdentity;
import pl.edu.icm.unity.types.I18nString;
import pl.edu.icm.unity.types.basic.Entity;
import pl.edu.icm.unity.types.basic.Identity;
import pl.edu.icm.unity.webui.authn.CredentialResetLauncher;
import pl.edu.icm.unity.webui.authn.credreset.password.PasswordCredentialResetController;
import pl.edu.icm.unity.webui.authn.extensions.PasswordRetrievalProperties;
import pl.edu.icm.unity.webui.common.NotificationPopup;
import pl.edu.icm.unity.webui.common.Styles;
import pl.edu.icm.unity.webui.common.credentials.CredentialEditor;
import pl.edu.icm.unity.webui.common.credentials.CredentialEditorRegistry;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@PrototypeComponent
public class PasswordRetrieval23 extends AbstractCredentialRetrieval<PasswordExchange> implements Vaadin23Authentication
{
	public static final String NAME = "web-password23";
	public static final String DESC = "WebPasswordRetrievalFactory.desc";

	private final Logger log = Log.getLogger(Log.U_SERVER_WEB, PasswordRetrieval23.class);
	private final MessageSource msg;
	private I18nString name;
	private String registrationFormForUnknown;
	private boolean enableAssociation;
	private final CredentialEditorRegistry credEditorReg;
	private String configuration;

	@Autowired
	public PasswordRetrieval23(MessageSource msg, CredentialEditorRegistry credEditorReg)
	{
		super(Vaadin23Authentication.NAME);
		this.msg = msg;
		this.credEditorReg = credEditorReg;
		name = new I18nString("WebPasswordRetrieval.password", msg);
	}

	@Override
	public String getSerializedConfiguration()
	{
		return configuration;
	}

	@Override
	public void setSerializedConfiguration(String configuration)
	{
		this.configuration = configuration;
		try
		{
			Properties properties = new Properties();
			properties.load(new StringReader(configuration));
			PasswordRetrievalProperties config = new PasswordRetrievalProperties(properties);
			name = config.getLocalizedString(msg, PasswordRetrievalProperties.NAME);
			if (name.isEmpty())
				name = new I18nString("WebPasswordRetrieval.password", msg);
			registrationFormForUnknown = config.getValue(
					PasswordRetrievalProperties.REGISTRATION_FORM_FOR_UNKNOWN);
			enableAssociation = config.getBooleanValue(PasswordRetrievalProperties.ENABLE_ASSOCIATION);
		} catch (Exception e)
		{
			throw new ConfigurationException("The configuration of the web-" +
					"based password retrieval can not be parsed or is invalid", e);
		}
	}

	@Override
	public Collection<Vaadin23AuthenticationUI> createUIInstance(Context context)
	{
		return Collections.singleton(
				new PasswordRetrievalUI23(credEditorReg.getEditor(PasswordVerificator.NAME)));
	}

	private class PasswordRetrievalComponent extends VerticalLayout
	{
		private final CredentialEditor credEditor;
		private AuthenticationCallback callback;
		private String presetAuthenticatedIdentity;
		
		private TextField usernameField;
		private PasswordField passwordField;
		private Button reset;
		private CredentialResetLauncher credResetLauncher;

		public PasswordRetrievalComponent(CredentialEditor credEditor)
		{
			this.credEditor = credEditor;
			initUI();
		}

		private void initUI()
		{
			VerticalLayout ret = new VerticalLayout();
			ret.setMargin(false);
			
			usernameField = new TextField();
			usernameField.setWidth(100, Unit.PERCENTAGE);
			usernameField.setPlaceholder(msg.getMessage("AuthenticationUI.username"));
			usernameField.addClassName("u-authnTextField");
			usernameField.addClassName("u-passwordUsernameField");
			ret.add(usernameField);
			
			String label = name.getValue(msg);
			passwordField = new PasswordField();
			passwordField.setWidth(100, Unit.PERCENTAGE);
			passwordField.setPlaceholder(label);
			passwordField.addClassName("u-authnTextField");
			passwordField.addClassName("u-passwordField");
			ret.add(passwordField);
			
			
			Button authenticateButton = new Button(msg.getMessage("AuthenticationUI.authnenticateButton"));
			authenticateButton.addClassName(Styles.signInButton.toString());
			authenticateButton.addClassName("u-passwordSignInButton");
			authenticateButton.addClickListener(event -> triggerAuthentication());
			ret.add(authenticateButton);

			passwordField.addFocusShortcut(Key.ENTER);

			PasswordCredentialResetSettings settings = new PasswordCredentialResetSettings(
					JsonUtil.parse(credentialExchange
							.getCredentialResetBackend()
							.getSettings()));
			if (settings.isEnabled())
			{
				reset = new Button(msg.getMessage("WebPasswordRetrieval.forgottenPassword"));
				VerticalLayout resetWrapper = new VerticalLayout(reset);
				resetWrapper.setAlignItems(FlexComponent.Alignment.END);
				resetWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
				resetWrapper.add("u-authn-forgotPassword");
				ret.add(resetWrapper);
				reset.addClickListener(event -> showResetDialog());
			}
			add(ret);
		}

		private void triggerAuthentication()
		{
			String username = presetAuthenticatedIdentity == null ? usernameField.getValue() : 
				presetAuthenticatedIdentity;
			String password = passwordField.getValue();

			if (password.equals(""))
			{
				NotificationPopup.showErrorAutoClosing(msg.getMessage("AuthenticationUI.authnErrorTitle"), 
						msg.getMessage("WebPasswordRetrieval.noPassword"));
			} else if (username.equals(""))
			{
				NotificationPopup.showErrorAutoClosing(msg.getMessage("AuthenticationUI.authnErrorTitle"), 
						msg.getMessage("WebPasswordRetrieval.noUser"));
			} else 
			{
				callback.onStartedAuthentication();
				AuthenticationResult authenticationResult = getAuthenticationResult(username, password);
				callback.onCompletedAuthentication(authenticationResult);
			}
		}
		
		private AuthenticationResult getAuthenticationResult(String username, String password)
		{
			if (username.equals("") && password.equals(""))
			{
				return LocalAuthenticationResult.notApplicable();
			}

			
			AuthenticationResult authenticationResult;
			try
			{
				authenticationResult = credentialExchange.checkPassword(
						username, password,  
						registrationFormForUnknown, enableAssociation, 
						callback.getTriggeringContext());
			} catch (AuthenticationException e)
			{
				log.info("Authentication error during password checking", e);
				authenticationResult = e.getResult();
			} catch (Exception e)
			{
				log.error("Runtime error during password checking", e);
				authenticationResult = LocalAuthenticationResult.failed(e);
			}
			if (authenticationResult.getStatus() == Status.success){
				Long entityId = authenticationResult.getSuccessResult().authenticatedEntity.getEntityId();
				WrappedSession session = VaadinSession.getCurrent().getSession();
				session.setAttribute("login", String.valueOf(entityId));
				session.setAttribute("authenticated", true);
			}
			if (authenticationResult.getStatus() == Status.success || 
					authenticationResult.getStatus() == Status.unknownRemotePrincipal)
			{
				clear();
			} else
			{
				setError();
			}
			return authenticationResult;
		}
		
		private void setError()
		{
			passwordField.setValue("");
			usernameField.setValue("");
		}
		
		private void showResetDialog()
		{
			PasswordCredentialResetController passReset = new PasswordCredentialResetController(msg, 
					credentialExchange.getCredentialResetBackend(), credEditor, 
					credResetLauncher.getConfiguration());
			AuthenticationSubject subject = presetAuthenticatedIdentity == null ? 
					null : AuthenticationSubject.identityBased(presetAuthenticatedIdentity);
			credResetLauncher.startCredentialReset(passReset.getInitialUI(Optional.ofNullable(subject)));
		}

		public void setCallback(AuthenticationCallback callback)
		{
			this.callback = callback;
		}

		public void setAuthenticatedIdentity(String authenticatedIdentity)
		{
			this.presetAuthenticatedIdentity = authenticatedIdentity;
			usernameField.setVisible(false);
		}

		public void clear()
		{
			passwordField.setValue("");
			usernameField.setValue("");
		}

		public void setCredentialResetLauncher(CredentialResetLauncher credResetLauncher)
		{
			this.credResetLauncher = credResetLauncher;
		}
	}
	
	private class PasswordRetrievalUI23 implements Vaadin23AuthenticationUI
	{
		private final PasswordRetrievalComponent theComponent;


		public PasswordRetrievalUI23(CredentialEditor credEditor)
		{
			this.theComponent = new PasswordRetrievalComponent(credEditor);
		}

		@Override
		public void setAuthenticationCallback(AuthenticationCallback callback)
		{
			theComponent.setCallback(callback);
		}

		@Override
		public Component getComponent()
		{
			return theComponent;
		}


		@Override
		public void clear()
		{
			theComponent.clear();
		}


		@Override
		public String getId()
		{
			return "password";
		}

		@Override
		public void presetEntity(Entity authenticatedEntity)
		{
			List<Identity> ids = authenticatedEntity.getIdentities();
			for (Identity id: ids)
				if (id.getTypeId().equals(UsernameIdentity.ID) || 
						id.getTypeId().equals(EmailIdentity.ID))
				{
					theComponent.setAuthenticatedIdentity(id.getValue());
					return;
				}
		}

		@Override
		public void setCredentialResetLauncher(CredentialResetLauncher credResetLauncher)
		{
			theComponent.setCredentialResetLauncher(credResetLauncher);
		}
	}
	
	
	@org.springframework.stereotype.Component
	public static class Factory extends AbstractCredentialRetrievalFactory<PasswordRetrieval23>
	{
		@Autowired
		public Factory(ObjectFactory<PasswordRetrieval23> factory)
		{
			super(NAME, DESC, Vaadin23Authentication.NAME, factory, PasswordExchange.ID);
		}
	}
}


