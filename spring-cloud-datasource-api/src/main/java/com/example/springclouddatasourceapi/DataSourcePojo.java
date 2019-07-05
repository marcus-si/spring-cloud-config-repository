package com.example.springclouddatasourceapi;

import java.io.Serializable;

public class DataSourcePojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965573461174221672L;

	private Integer id;
	private String url;
	private String driverClassName;
	private String username;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "DataSource [url=" + url + ", driverClassName=" + driverClassName + ", username=" + username + "]";
	}

}
