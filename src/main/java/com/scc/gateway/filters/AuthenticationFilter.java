package com.scc.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.scc.gateway.config.ServiceConfig;
import com.scc.gateway.model.UserToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

@Component
public class AuthenticationFilter extends ZuulFilter {

	private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    ServiceConfig config;
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String filterType() {
        return filterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

	private boolean isAuthentificationKeyIsPresent() {
		if (filterUtils.getAuthentificationKey() != null) {
			return true;
		}

		return false;
	}
	
    private boolean isAuthTokenPresent() {
        if (filterUtils.getAuthToken() !=null){
            return true;
        }

        return false;
    }

    HttpHeaders createHeaders(String username, String password){
	   return new HttpHeaders() {{
	         String auth = username + ":" + password;
	         byte[] encodedAuth = Base64.encodeBase64( 
	            auth.getBytes(Charset.forName("US-ASCII")) );
	         String authHeader = "Basic " + new String( encodedAuth );
	         set( "Authorization", authHeader );
	         setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	      }}
	   ;
	}
   
    private String getToken(String authentification_key)  {
       
    	ResponseEntity<UserToken> restExchange = null;
        String token = null;
        
        try {

        	logger.debug("call getToken {}, {}, {}", authentification_key, config.getUsername(), config.getPassword());

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("username", authentification_key);
            map.add("password", config.getDefault_password_api());
            map.add("scope", config.getScope());
            map.add("grant_type", config.getGrant_type());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,  createHeaders(config.getUsername(), config.getPassword()));
        	
        	restExchange =
                restTemplate.postForEntity(
                		config.getTokenUri()
                		, request
                        , UserToken.class
            );

        	token = restExchange.getBody().getAccess_token(); 
        	logger.debug("response getToken {}", token);
        }
        catch(HttpClientErrorException ex){
            logger.error("HttpClientErrorException {}",ex.getLocalizedMessage());
            if (ex.getStatusCode()==HttpStatus.UNAUTHORIZED) {
                //return null;
            }
        }
    	return token;
    }

    @Override
    public Object run() {

    	String token = null;
    	RequestContext ctx = RequestContext.getCurrentContext();

    	// Search authentication rule
    	// . by key header == X-SCC-authentification
    	// . by token == Bearer
    	
        // Check if X-SCC-authentification is present
		if (isAuthentificationKeyIsPresent()) {
			logger.debug("Authentication key found in tracking filter: {}. ", filterUtils.getAuthentificationKey());
			// Check if token is present
	        if (isAuthTokenPresent()){
	            logger.debug("Authentication token is present.");
	        } else {
	        	// Retrieve token
	        	token = getToken(filterUtils.getAuthentificationKey());
	        	if (!"".equals(token) && token != null) {
	        		logger.debug("Add authentication token {}", token);
        			filterUtils.setAuthToken("Bearer " + token);
	        	} else {
	        		// Wrong key header
		            logger.debug("Authentication token is not present.");
		            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
		            ctx.setSendZuulResponse(false);
	        	}
	        }
		} else {
			logger.debug("Authentication key not found");
	        if (isAuthTokenPresent()){
	            logger.debug("Authentication token is present.");
	            filterUtils.setAuthToken(filterUtils.getAuthToken());
	         }else{
	             logger.debug("Authentication token is not present.");
	             ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
	             ctx.setSendZuulResponse(false);
	         }
		}

        return null;

    }
}
