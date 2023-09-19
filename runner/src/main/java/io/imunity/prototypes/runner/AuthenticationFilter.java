package io.imunity.prototypes.runner;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.http.auth.BasicUserPrincipal;
import org.eclipse.jetty.ee10.servlet.ServletApiRequest;
import org.eclipse.jetty.security.AuthenticationState;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.security.internal.DefaultUserIdentity;

import javax.security.auth.Subject;
import java.io.IOException;

import static java.util.Optional.ofNullable;

public class AuthenticationFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig)
	{

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {
		ServletApiRequest rq = (ServletApiRequest) request;
		boolean authenticated = ofNullable(rq.getSession(true).getAttribute("authenticated"))
			.map(attribute -> (boolean) attribute)
			.orElse(false);
		if(authenticated && rq.getUserPrincipal() == null){
			AuthenticationState.setAuthenticationState(rq.getRequest(), new LoginAuthenticator.UserAuthenticationSucceeded(
				"basic", new DefaultUserIdentity(
					new Subject(), new BasicUserPrincipal((String) rq.getSession().getAttribute("login")), new String[]{"USER"}
			)
			));
		}
		chain.doFilter(request, response);
	}

}
