package com.example.springcloudconsulconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "eurekaserviceapi")
public class SpringCloudConsulConsumer1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudConsulConsumer1Application.class, args);
	}

}
