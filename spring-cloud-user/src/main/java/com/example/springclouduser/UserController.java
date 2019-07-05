package com.example.springclouduser;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.springclouddatasourceapi.ClientDatabase;
import com.example.springclouddatasourceapi.ClientDatabaseContextHolder;
import com.example.springclouddatasourceapi.ClientDatasourceRouter;
import com.example.springclouddatasourceapi.DataSourcePojo;
import com.example.springclouddatasourceapi.DataSourceService;
import com.example.springclouddatasourceapi.DataSourceUtils;

@RestController
@RefreshScope
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Reference(timeout = 10000)
	private DataSourceService dataSourceService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@GetMapping("/save/{id}")
	public String saveUser(@PathVariable @NotBlank Integer id) {
		// save to database A
		ClientDatabase clientDatabase = ClientDatabase.ofId(id);
		if (!ClientDatasourceRouter.isExistDataSource(clientDatabase)) {
			return String.format("The data source with id %d does not exist", id);
		}
		ClientDatabaseContextHolder.set(clientDatabase);
		User user = new User();
		user.setName("user " + clientDatabase.getName());
		try {
			userRepository.save(user);
			return String.format("[UserController] Succeeded. User %s has been save to Database %s", user.getName(),
					clientDatabase);
		} catch (Exception e) {
			return String.format("[UserController] Failed to save User %s to Database %s", user.getName(),
					clientDatabase);
		}
	}

	@GetMapping("/updateDataSources")
	public void updateDataSources() {
		try {
			LOGGER.debug("Starting to update data sources in User Service");
			ClientDatasourceRouter dataSource = defaultListableBeanFactory.getBean(ClientDatasourceRouter.class);
			if (dataSource == null) {
				return;
			}
			DataSourceUtils.cleanUpDataSourceConnections();
			List<DataSourcePojo> dataSourceProps = dataSourceService.getDataSourceProps();
			dataSource.setMultiDataSources(dataSourceProps);
			dataSource.afterPropertiesSet();
			LOGGER.info("Succeeded to update data sources in User Service: {}.", dataSourceProps);
		} catch (Exception e) {
			LOGGER.warn("Failed to update data sources in User Service.");
			e.printStackTrace();
		}
	}

	@GetMapping("/getds")
	public Object getDs() {
		Object clientDatasourceRouter = dataSourceService.getDataSourceProps();
		return clientDatasourceRouter;

//		return null;
	}

}
