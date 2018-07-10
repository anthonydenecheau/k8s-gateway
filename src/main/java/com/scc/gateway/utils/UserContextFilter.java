package com.scc.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentracing.Scope;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

	@Autowired
	io.opentracing.Tracer tracer;

	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		UserContextHolder.getContext()
				.setAuthentificationKey(httpServletRequest.getHeader(UserContext.AUTHENTICATION_KEY));
		logger.debug("Incoming Authentification key: {}", UserContextHolder.getContext().getAuthentificationKey());

		Scope scope = tracer.scopeManager().active();
		if (scope != null) {
		    scope.span().log("Ici");
		}

		filterChain.doFilter(httpServletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
