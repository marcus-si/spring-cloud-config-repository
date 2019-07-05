package com.example.springclouddynamicdatasources;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.Builder;
import org.springframework.cloud.alibaba.nacos.refresh.NacosContextRefresher;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.context.annotation.EnableNacos;
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent;
import com.alibaba.nacos.spring.core.env.NacosPropertySource;
import com.example.springclouddatasourceapi.ClientDatabase;
import com.example.springclouddatasourceapi.ClientDatasourceRouter;
import com.example.springclouddatasourceapi.DataSourcePojo;
import com.example.springclouddatasourceapi.DataSourceService;
import com.example.springclouddatasourceapi.DataSourceUtils;

@Configuration
@EnableNacos(globalProperties = @NacosProperties(serverAddr = "${spring.cloud.nacos.config.server-addr}", namespace = "${spring.cloud.nacos.config.namespace}"))
//@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
public class DataSourceProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceProperties.class);

	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Autowired
	private DataSourceService dataSourceService;

	@Value("${spring.cloud.nacos.config.group: DEFAULT_GROUP}")
	private String group;

	@NacosInjected
	private ConfigService configService;

	private List<String> config;

	public List<String> getConfig() {
		if (this.config != null) {
			return this.config;
		}
		try {
			String dsConfig = configService.getConfig("ds.properties", group, 10000);
			Properties props = new Properties();
			StringReader reader = new StringReader(dsConfig);
			props.load(reader);
			List<String> dataSourcePropList = DataSourceUtils.getDataSourcePropList(props);
			this.config = dataSourcePropList;
			return dataSourcePropList;
		} catch (NacosException | IOException e) {
			throw new RuntimeException("Cannot load data source properties.");
		}
	}

	public void setConfig(List<String> config) {
		this.config = config;
	}

	public List<DataSourcePojo> getDataSourceProperties(List<String> dsConfig) {
		if (null == dsConfig) {
			dsConfig = this.getConfig();
		}
		final List<DataSourcePojo> list = new ArrayList<>();
		dsConfig.forEach(c -> {
			list.add(JSON.parseObject(c, DataSourcePojo.class));
		});
		return list;
	}

	@NacosConfigListener(dataId = "ds.properties")
	public void updateDataSources(@NotNull Properties properties) {
		final List<String> propsList = DataSourceUtils.getDataSourcePropList(properties);
		if ( !isConfigUpdated(propsList)) {
			return;
		}
		DataSourceUtils.cleanUpDataSourceConnections();
		ClientDatasourceRouter dataSource = defaultListableBeanFactory.getBean(ClientDatasourceRouter.class);
		dataSource.setMultiDataSources(getDataSourceProperties(propsList));
		dataSource.afterPropertiesSet();
		this.config = propsList;
		LOGGER.info("data sources updated:{}", properties);
		dataSourceService.updateDataSources();
	}

	private void createDataSourceBean(List<DataSourcePojo> dataSourcePojos) {
		ClientDatasourceRouter datasource = new ClientDatasourceRouter();
		datasource.setMultiDataSources(dataSourcePojos);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(ClientDatasourceRouter.class);
		beanDefinitionBuilder.addPropertyValue("targetDataSources", datasource.getTargetDataSources());
		beanDefinitionBuilder.addPropertyValue("defaultTargetDataSource",
				datasource.getTargetDataSources().get(ClientDatabase.ofId(1)));
		defaultListableBeanFactory.registerBeanDefinition("multiDataSource",
				beanDefinitionBuilder.getRawBeanDefinition());
	}
	
	private boolean isConfigUpdated(@NotNull List<String> newConfigs) {
		if (this.config.size() != newConfigs.size()) {
			return true;
		}
		for (String s : newConfigs) {
			if ( !this.config.contains(s)) {
				return true;
			}
		}
		return false;
	}
	
}
