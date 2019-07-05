package com.example.springclouduser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import com.example.springclouddatasourceapi.ClientDatasourceRouter;
import com.example.springclouddatasourceapi.DataSourcePojo;
import com.example.springclouddatasourceapi.DataSourceService;
import com.example.springclouddatasourceapi.DataSourceUtils;

@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "${spring.cloud.nacos.config.server-addr}", namespace = "${spring.cloud.nacos.config.namespace}"))
public class DataSourceConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);
	@Reference
	private DataSourceService dataSourceService;
	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Value("${server.port:8080}")
	private int port;

	@Bean
	public DataSource multiDatasources() {
		ClientDatasourceRouter clientDatasourceRouter = new ClientDatasourceRouter();
		List<DataSourcePojo> dataSourceProps = dataSourceService.getDataSourceProps();
		clientDatasourceRouter.setMultiDataSources(dataSourceProps);
		try {
			InetAddress address = InetAddress.getLocalHost();			
			String returnUrl = "http://" + address.getHostAddress() + ":" + port + "/updateDataSources";
			if (dataSourceService.subscribeDataSourceUpdate(returnUrl)) {
				LOGGER.info("Successfully subscribed to data source update with the return url: {}", returnUrl);
			} else {
				LOGGER.error("Unable to subscribe data source update with the return url: {}", returnUrl);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return clientDatasourceRouter;
	}

//	@NacosConfigListener(dataId = "ds.properties",groupId = "DEFAULT_GROUP" )	
//	public boolean updateDataSources(Properties props) {
//		try {
//			LOGGER.debug("Starting to update data sources in User Service");
//			ClientDatasourceRouter dataSource = defaultListableBeanFactory.getBean(ClientDatasourceRouter.class);
//			if (dataSource == null) {
//				return false;
//			}
//			List<String> dataSourcePropList = DataSourceUtils.getDataSourcePropList(props);
//			List<DataSourcePojo> dataSourceProps = DataSourceUtils.getDataSourceProperties(dataSourcePropList);
//			dataSource.setMultiDataSources(dataSourceProps);
//			dataSource.afterPropertiesSet();
//			LOGGER.info("Succeeded to update data sources in User Service: {}.", dataSourceProps);
//			return true;
//		} catch (Exception e) {
//			LOGGER.warn("Failed to update data sources in User Service.");
//			e.printStackTrace();
//			return false;
//		}
//	}

}
