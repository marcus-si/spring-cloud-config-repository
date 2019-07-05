package com.example.springclouddatasourceapi;

import java.util.List;
import java.util.Set;

public interface DataSourceService {	
	
	Object getClientDatasourceRouter();
	
	List<DataSourcePojo> getDataSourceProps();	
	
	Boolean subscribeDataSourceUpdate(String returnUrl);
	
	Set<String> getDataSourceSubscribers();
	
	void  updateDataSources();

}
