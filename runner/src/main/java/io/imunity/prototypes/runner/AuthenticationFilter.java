package io.imunity.prototypes.runner;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.http.auth.BasicUserPrincipal;
import org.eclipse.jetty.security.DefaultUserIdentity;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.server.Request;

import javax.security.auth.Subject;
import java.io.IOException;

import static java.util.Optional.ofNullable;

public class AuthenticationFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {
		Request rq = (Request) request;
		boolean authenticated = ofNullable(rq.getSession().getAttribute("authenticated"))
			.map(attribute -> (boolean) attribute)
			.orElse(false);
		if(authenticated && rq.getUserPrincipal() == null){
			UserAuthentication userAuthentication = new UserAuthentication("basic", new DefaultUserIdentity(
				new Subject(),
				new BasicUserPrincipal((String) rq.getSession().getAttribute("login")),
				new String[]{"USER"})
			);
			rq.setAuthentication(userAuthentication);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
