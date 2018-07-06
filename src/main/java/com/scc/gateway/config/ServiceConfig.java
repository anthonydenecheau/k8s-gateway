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

  @Value("${authentication.urlToken}")
  private String urlToken="";
  
  public String getUsername() { return username;}
  public String getPassword() { return password; }
  public String getUrlToken() { return urlToken; }

}
