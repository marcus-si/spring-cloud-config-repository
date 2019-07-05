package com.example.springclouddynamicdatasources;



import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.springclouddatasourceapi.ClientDatabase;
import com.example.springclouddatasourceapi.ClientDatabaseContextHolder;
import com.example.springclouddatasourceapi.ClientDatasourceRouter;

@RestController
public class DataSourceController {
	private static final Logger LOGGER =LoggerFactory.getLogger(DataSourceController.class);
	@Value("${spring.cloud.nacos.config.namespace}")	
	private String nameString;	
	@Autowired
	private UserRepository userRepository;
//	@Autowired
//	private DataSourceProperties dataSourceProperties;	
	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
//	@GetMapping(value = "/save/{dataSourceId}")
//	public String saveUser(@PathVariable @NotBlank Integer dataSourceId) {
//		//save to database A		
//		ClientDatabase clientDatabase = ClientDatabase.ofId(dataSourceId);		
//		if (!ClientDatasourceRouter.isExistDataSource(clientDatabase)) {
//			return String.format("The data source with id %d does not exist", dataSourceId); 
//		}
//		ClientDatabaseContextHolder.set(clientDatabase);
//		redisTemplate.boundValueOps("test").set("test controller");
//		User user = new User();
//		user.setName(clientDatabase.getName());
//		try {
//			userRepository.save(user);
//			return String.format("Succeeded. User %s has been save to Database %s", user.getName(), clientDatabase);			
//		} catch (Exception e) {
//			return String.format("Failed to save User %s to Database %s", user.getName(), clientDatabase);
//		}	
//
//		
//	}
	
	@GetMapping("/update")
	public String updateDatasources() {
		try {
			DataSource bean3 = defaultListableBeanFactory.getBean(DataSource.class);		
			return "update data sources successfully." + nameString;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to update data sources.";
		}
		
		
	}
	


}
