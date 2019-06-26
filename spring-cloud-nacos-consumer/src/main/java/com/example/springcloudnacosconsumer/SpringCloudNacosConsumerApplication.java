package com.example.springcloudnacosconsumer;

import java.time.Duration;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.springcloudnacosfeignapi.EchoService;
import com.example.springcloudnacosfeignapi.NacosFeign;


@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableFeignClients(basePackages = {"com.example.springcloudnacosfeignapi"})
public class SpringCloudNacosConsumerApplication {
	
	@RestController
	@RefreshScope
	public class NacosConsumerController{
		@Autowired
		private LoadBalancerClient loadBalancerClient;
		@Autowired
		private RestTemplate restTemplate;
		@Value("${spring.application.name}")
		private String appName;
		@Value("${user.id}")
		private Integer userId;
		@Value("${user.name}")
		private String username;
		@Value("${user.age}")
		private Integer userAge;
		@Value("${org}")
		private String orgName;		
		@Autowired
		private NacosFeign nacosFeign;
		@Reference
		private EchoService echoService;
		@Resource
		private RedisTemplate<String, Object> redisTemplate;

		@GetMapping("/echo/app-name")
		public String echoAppName() {
			ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-producer");
			String path = String.format("http://%s:%s/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), appName);
			System.out.println("Request path:" + path);
			return restTemplate.getForObject(path, String.class);
		}
		
		@GetMapping("/echo/provider-name")
		public String getProviderName() {
			return nacosFeign.getProviderName();
		}
		
		@GetMapping("/echo/msg")
		public String getMessage() {
			return String.format("In consumer, your name is %s, and in producer %s. Your organization is %s", username, echoService.echo("Pony Ma"), orgName);
		}
		
		@GetMapping("/hello")
		public String sayHello() {
			return String.format("Hello %s, your id is %d, and your age is %d", username, userId, userAge);
		}
		
		@GetMapping("/cache")
		public String getCacheStr() {
			String key = "test:cache";
			redisTemplate.boundValueOps(key).set("This is a cached value", Duration.ofMinutes(5));
			return (String) redisTemplate.boundValueOps(key).get();
		}
	}
	

	
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringCloudNacosConsumerApplication.class, args);
	}

}
