package com.example.springclouddatasourceapi;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class DataSourceUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtils.class);

	public static List<String> getDataSourcePropList(@NotNull Properties props){
		final List<String> propsList = new ArrayList<>();
		props.forEach((k, v) -> {
			if (String.valueOf(k).matches("ds.config\\[\\d+\\]")) {
				propsList.add(String.valueOf(v));
			}
		});
		return propsList;
	}
	
	public static List<DataSourcePojo> getDataSourceProperties(@NotNull List<String> dsConfig) {
		final List<DataSourcePojo> list = new ArrayList<>();
		dsConfig.forEach(c -> {
			list.add(JSON.parseObject(c, DataSourcePojo.class));
		});
		return list;
	}
	
	public static void cleanUpDataSourceConnections() {
    	try {
        	for (Enumeration<Driver> drivers=DriverManager.getDrivers(); drivers.hasMoreElements();) {
        		DriverManager.deregisterDriver(drivers.nextElement());
        	}			
		} catch (SQLException e) {
			LOGGER.error("Unable to deregister SQL drivers.");
		}
    	AbandonedConnectionCleanupThread.checkedShutdown();		
	}
}
