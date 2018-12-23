package com.bank.project0;

public class Application {
	private String firstName;
	private String lastName;
	private String address;
	private int socialSecurityNum;
	private long phoneNumber;
	private int appID;
	
	
	
	Application(String firstName, String lastName, String address, int socialSecurityNum,
				long phoneNum){
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.socialSecurityNum = socialSecurityNum;
		this.phoneNumber = phoneNum;
		
		
		
	}

	public int getAppID() {
		return appID;
	}



	public void setAppID(int appID) {
		this.appID = appID;
	}

}
