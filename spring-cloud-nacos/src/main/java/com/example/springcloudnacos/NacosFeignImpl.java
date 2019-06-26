package com.example.springcloudnacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcloudnacosfeignapi.NacosFeign;



@RestController
public class NacosFeignImpl implements NacosFeign{

	@Value("${spring.application.name}")
	private String providerName;
	
	@Override
	public String getProviderName() {		
		return this.getClass().getName() + ":" + providerName;
	}

}
