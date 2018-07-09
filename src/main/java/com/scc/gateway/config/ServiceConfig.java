package com.scc.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig {
	
  @Value("${authentication.username}")
  private String username="";

  @Value("${authentication.password}")
  private String password="";

  @Value("${authentication.tokenUri}")
  private String tokenUri="";

  @Value("${authentication.scope}")
  private String scope="";

  @Value("${authentication.grant_type}")
  private String grant_type="";

  @Value("${authentication.default_password_api}")
  private String default_password_api="";

  public String getUsername() { return username;}
  public String getPassword() { return password; }
  public String getTokenUri() { return tokenUri; }
  public String getScope() { return scope; }
  public String getGrant_type() { return grant_type; }
  public String getDefault_password_api() { return default_password_api; }

}
