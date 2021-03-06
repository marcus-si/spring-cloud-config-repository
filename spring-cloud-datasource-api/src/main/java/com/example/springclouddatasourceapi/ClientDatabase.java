package com.example.springclouddatasourceapi;

import java.util.Arrays;

public enum ClientDatabase {
	CLIENT_A(1, "client a"), CLIENT_B(2, "client b"), CLIENT_C(3, "client c"), CLIENT_D(4, "client d");

	private int id;
	private String name;

	private ClientDatabase(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ClientDatabase ofId(int id) {
		return Arrays.stream(ClientDatabase.values()).filter(c -> c.id == id).findFirst().orElse(null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
