package com.example.springcloudnacos;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import com.example.springcloudnacosfeignapi.EchoService;

@Service
public class EchoServiceImpl implements EchoService{
	
	@Value("${user.name}")
	private String username;

	@Override
	public String echo(String message) {
		return "[echo] Hello, " + message + ", your name is " + username;
	}

}
