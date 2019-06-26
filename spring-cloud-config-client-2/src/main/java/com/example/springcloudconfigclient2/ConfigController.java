package com.example.springcloudconfigclient2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigController {
	
	@Value("${profile}")
	private String profile;
	@Value("${desc}")
	private String desc;
	
	@GetMapping("/desc")
	public String getDesc() {
		return String.format("The profile is %s, and the desc is %s", profile, desc);
	}

	
}
