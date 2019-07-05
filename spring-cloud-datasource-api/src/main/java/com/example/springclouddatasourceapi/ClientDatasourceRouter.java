package com.example.springclouddatasourceapi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ClientDatasourceRouter extends AbstractRoutingDataSource implements Serializable{
	private static final long serialVersionUID = -8380706363717320211L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDatasourceRouter.class);
	private static Map<Object, Object> targetDataSources = new HashMap<>();

	@Override
	protected Object determineCurrentLookupKey() {		
		return ClientDatabaseContextHolder.getClientDatabase();
	}
	
	private DataSource createDataSource(DataSourcePojo dataSourcePojo) {
		@SuppressWarnings("unchecked")
		DataSourceBuilder<DataSource> builder = (DataSourceBuilder<DataSource>) DataSourceBuilder.create();
		DataSource dataSource = builder.url(dataSourcePojo.getUrl()).driverClassName(dataSourcePojo.getDriverClassName())
		.username(dataSourcePojo.getUsername()).password(dataSourcePojo.getPassword()).build();
		try {
			dataSource.getConnection();	
			LOGGER.info("Data source {} connected.",dataSourcePojo.getUrl());			
			return dataSource;
		} catch (SQLException e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			LOGGER.error(stringWriter.toString());
			return null;
		}		
	}

	public void setMultiDataSources(@NotNull List<DataSourcePojo> dataSourcePojos) {
		LOGGER.debug(dataSourcePojos.toString());
		buildDataSourceMap(dataSourcePojos);
		super.setTargetDataSources(targetDataSources);
		super.setDefaultTargetDataSource(targetDataSources.get(ClientDatabase.ofId(1)));		
	}
	
	private void buildDataSourceMap(@NotNull List<DataSourcePojo> dataSourcePojos) {
		targetDataSources.clear();
		dataSourcePojos.forEach(d -> {
			DataSource dataSource = createDataSource(d);
			if ( null != dataSource ) {
				targetDataSources.put(ClientDatabase.ofId(d.getId()), dataSource);	
			}			
		});
	}

	public static boolean isExistDataSource(ClientDatabase key) {
		return ClientDatasourceRouter.targetDataSources.containsKey(key);
	}
	
	public Map<Object, Object> getTargetDataSources(){
		return targetDataSources;
	}
}
