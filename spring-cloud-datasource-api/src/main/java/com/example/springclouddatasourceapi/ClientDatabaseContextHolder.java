package com.example.springclouddatasourceapi;


import org.springframework.util.Assert;

public class ClientDatabaseContextHolder {

	private static ThreadLocal<ClientDatabase> context = new ThreadLocal<>();
	
	public static void set(ClientDatabase clientDatabase) {
		Assert.notNull(clientDatabase, "clientDatabase cannot be null");
		context.set(clientDatabase);
	}
	
	public static ClientDatabase getClientDatabase() {		
		return context.get();
	}
	
	public static void clear() {
		context.remove();
	}
}
