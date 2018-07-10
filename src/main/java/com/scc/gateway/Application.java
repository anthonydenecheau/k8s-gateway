package com.scc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;


@SpringBootApplication
@EnableZuulProxy
public class Application {

//	@Bean
//	public io.opentracing.Tracer tracer() {
//		// tracer instance of your choice (Zipkin, Jaeger, LightStep)
//		Reporter reporter = new InMemoryReporter();
//		Sampler sampler = new ConstSampler(true);
//		Tracer tracer = new JaegerTracer.Builder("gateway")
//		  .registerInjector(Format.Builtin.HTTP_HEADERS, new B3TextMapCodec())
//		  .registerExtractor(Format.Builtin.HTTP_HEADERS, new B3TextMapCodec())				
//		  .withReporter(reporter)
//		  .withSampler(sampler)
//		  .build();
//		return tracer;
//	}

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		return new Configuration("gateway", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
				new Configuration.ReporterConfiguration())
				.getTracer();
	}
	
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
