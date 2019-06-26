package com.example.springcloudnacosconsumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/demo")
public class ZuulDemoController {
	
	@GetMapping("/hello")
	public String hello(String name) {		
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		return String.format("Hi, %s, this is zuul api!, %s, %s", name, RequestUtils.isDispatcherServletRequest(), request.getRequestURL());
	}

}