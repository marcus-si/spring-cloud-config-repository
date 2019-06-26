package com.example.springclouddynamicdatasources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ClientDatasourceRouter extends AbstractRoutingDataSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDatasourceRouter.class);

	@Override
	protected Object determineCurrentLookupKey() {		
		return ClientDatabaseContextHolder.getClientDatabase();
	}
	
	private DataSource createDataSource(DataSourcePojo dataSourcePojo) {
		@SuppressWarnings("unchecked")
		DataSourceBuilder<DataSource> builder = (DataSourceBuilder<DataSource>) DataSourceBuilder.create();
		return builder.url(dataSourcePojo.getUrl()).driverClassName(dataSourcePojo.getDriverClassName())
				.username(dataSourcePojo.getUsername()).build();
	}

	public void setMultiDataSources(@NotNull List<DataSourcePojo> dataSourcePojos) {
		LOGGER.debug(dataSourcePojos.toString());
		Map<Object, Object> targetDataSources = buildDataSourceMap(dataSourcePojos);
		super.setTargetDataSources(targetDataSources);
		super.setDefaultTargetDataSource(targetDataSources.get(ClientDatabase.ofId(1)));		
	}
	
	public Map<Object, Object> buildDataSourceMap(@NotNull List<DataSourcePojo> dataSourcePojos) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		dataSourcePojos.forEach(d -> {
			DataSource dataSource = createDataSource(d);
			targetDataSources.put(ClientDatabase.ofId(d.getId()), dataSource);
		});
		return targetDataSources;
	}

}
