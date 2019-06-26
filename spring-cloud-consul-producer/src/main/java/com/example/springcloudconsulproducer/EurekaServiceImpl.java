package com.example.springcloudconsulproducer;

import org.springframework.web.bind.annotation.RestController;

import eurekaserviceapi.EurekaServiceApi;

@RestController
public class EurekaServiceImpl implements EurekaServiceApi {

	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return "Consul Message";
	}

}
