package com.resumebuilder.auth;


import lombok.NonNull;


public class LoginRequest {
	
	@NonNull
	private String username;

	@NonNull
	private String password;
	
	public LoginRequest(@NonNull String username, @NonNull String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public LoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	  public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
