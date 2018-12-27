package com.bank.project0;

public class Employee extends Person {
	private String password;
	private String adminPriveleges;
	
	public Employee(int id, String firstName, String lastName, String password, String adminPrivileges) {
		
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		this.password = password;
		this.adminPriveleges = adminPrivileges;
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdminPriveleges() {
		return adminPriveleges;
	}

	public void setAdminPriveleges(String adminPriveleges) {
		this.adminPriveleges = adminPriveleges;
	}
	

}
