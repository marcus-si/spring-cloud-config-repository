package com.example.springclouddynamicdatasources;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.example.springclouddatasourceapi.ClientDatasourceRouter;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class DatabaseConnectionCleanupListener implements ServletContextListener{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionCleanupListener.class);

	
//    public void contextInitialized(ServletContextEvent sce) {
//    	try {
//        	for (Enumeration<Driver> drivers=DriverManager.getDrivers(); drivers.hasMoreElements();) {
//        		DriverManager.deregisterDriver(drivers.nextElement());
//        	}			
//		} catch (SQLException e) {
//			LOGGER.error("Unable to deregister SQL drivers.");
//		}
//    	AbandonedConnectionCleanupThread.checkedShutdown();
//    }
	
    public void contextDestroyed(ServletContextEvent sce) {
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
