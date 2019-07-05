package com.example.springclouddynamicdatasources;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.example.springclouddatasourceapi.ClientDatasourceRouter;
import com.example.springclouddatasourceapi.DataSourcePojo;
import com.example.springclouddatasourceapi.DataSourceService;

@Service
public class DataSourceServiceImpl implements DataSourceService, InitializingBean  {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceServiceImpl.class);
	
	@Autowired
	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final String DATA_SOURCE_SUBSCRIPTION_KEY = "DataSource:Subscription:List";
	
	private static final Set<String> DATA_SOURCE_SUBSCRIBERS = new HashSet<>();

	@Override
	public Object getClientDatasourceRouter() {
		ClientDatasourceRouter datasourceRouter = defaultListableBeanFactory.getBean(ClientDatasourceRouter.class);
		if (datasourceRouter != null) {
			JSONObject object = new JSONObject();
			object.put("dataSourceRouter", datasourceRouter);
			return object;
		}
		return null;
	}

	@Override
	public List<DataSourcePojo> getDataSourceProps() {
		DataSourceProperties dataSourceProperties = defaultListableBeanFactory.getBean(DataSourceProperties.class);
		return dataSourceProperties.getDataSourceProperties(dataSourceProperties.getConfig());
	}

	@Override
	public Boolean subscribeDataSourceUpdate(String returnUrl) {
		try {
			redisTemplate.boundSetOps(DATA_SOURCE_SUBSCRIPTION_KEY).add(returnUrl);
			DATA_SOURCE_SUBSCRIBERS.add(returnUrl);			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Set<String> getDataSourceSubscribers() {		
		return DATA_SOURCE_SUBSCRIBERS;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate.boundSetOps(DATA_SOURCE_SUBSCRIPTION_KEY).members().forEach(s->{
			DATA_SOURCE_SUBSCRIBERS.add(String.valueOf(s));			
		});		
	}

	@Override
	public void updateDataSources() {
		for (String s : DATA_SOURCE_SUBSCRIBERS) {
			try {
				restTemplate.execute(s, HttpMethod.GET, null, null);				
			} catch (Exception e) {
				LOGGER.error("Unable to update data sources with this url: {}",s);
				continue;
			}
		}		
	}

}
