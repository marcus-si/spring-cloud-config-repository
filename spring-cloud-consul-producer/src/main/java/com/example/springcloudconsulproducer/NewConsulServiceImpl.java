package com.example.springcloudconsulproducer;

import org.springframework.web.bind.annotation.RestController;

import eurekaserviceapi.NewEurekaServiceApi;

@RestController
public class NewConsulServiceImpl implements NewEurekaServiceApi{

	@Override
	public String getNewMsg() {
		// TODO Auto-generated method stub
		return "New message from new consul service";
	}
	
	

}
