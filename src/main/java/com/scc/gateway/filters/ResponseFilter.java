package com.scc.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResponseFilter extends ZuulFilter {

	private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;

	@Override
	public String filterType() {
		return FilterUtils.POST_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		//ctx.getResponse().addHeader("scc-correlation-id", tracer.getCurrentSpan().traceIdString());
		return null;
	}
}
