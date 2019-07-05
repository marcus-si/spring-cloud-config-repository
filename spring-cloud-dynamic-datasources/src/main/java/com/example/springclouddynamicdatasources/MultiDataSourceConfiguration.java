package com.example.springclouddynamicdatasources;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.example.springclouddatasourceapi.ClientDatasourceRouter;

@Configuration
@Order(2)
public class MultiDataSourceConfiguration {
	
	@Autowired
	private DataSourceProperties dataSourceProperties;
	
	@Bean
	public DataSource multiDataSource() {
		ClientDatasourceRouter clientDatasourceRouter = new ClientDatasourceRouter();
		clientDatasourceRouter.setMultiDataSources(dataSourceProperties.getDataSourceProperties(null));
		return clientDatasourceRouter;
	}
	
	
}
