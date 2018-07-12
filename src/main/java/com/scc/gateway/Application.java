package com.scc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


import io.jaegertracing.samplers.ConstSampler;
import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.opentracing.Tracer;


@SpringBootApplication
@EnableZuulProxy
public class Application {

	@Bean
	public Tracer jaegerTracer() {
		
		final Tracer tracer = new Configuration("gateway")
			    .withSampler(
			        new SamplerConfiguration()
			            .withType(ConstSampler.TYPE)
			            .withParam(new Float(1.0f)))
			    .withReporter(
			        new ReporterConfiguration())
			    .getTracer();
		return tracer;
			    
	}
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
