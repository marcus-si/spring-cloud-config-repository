package com.example.springclouddynamicdatasources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.springclouddatasourceapi", "com.example.springclouddynamicdatasources"})
//@EnableDiscoveryClient
@ServletComponentScan
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SpringCloudDynamicDatasourcesApplication {

	
	public static void main(String[] args) {
		//SpringApplication.run(SpringCloudDynamicDatasourcesApplication.class, args);
		new SpringApplicationBuilder(SpringCloudDynamicDatasourcesApplication.class).registerShutdownHook(false).run(args);
	}
	
	
}
