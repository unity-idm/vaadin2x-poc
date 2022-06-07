package io.imunity.prototypes.runner;

import org.apache.http.auth.BasicUserPrincipal;
import org.eclipse.jetty.security.DefaultUserIdentity;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;

import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.*;

public class AuthenticationFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {
		Request rq = (Request) request;

		boolean authenticated = ofNullable(rq.getSession().getAttribute("authenticated"))
			.map(attribute -> (boolean) attribute)
			.orElse(false);
		if(!authenticated)
			((HttpServletResponse)response).addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
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
