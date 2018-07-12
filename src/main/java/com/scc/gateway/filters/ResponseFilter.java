package com.scc.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import io.jaegertracing.SpanContext;
import io.opentracing.Scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseFilter extends ZuulFilter {

    @Autowired
    private io.opentracing.Tracer tracer;
    
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
        SpanContext c = (SpanContext) tracer.activeSpan().context();
        ctx.getResponse().addHeader("scc-correlation-id", String.valueOf(c.getTraceId()));

        return null;	        
	}
}
