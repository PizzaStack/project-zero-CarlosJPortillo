package com.bank.project0;

public class Application  {
	private String firstName;
	private String lastName;
	private String address;
	private int socialSecurityNum;
	private long phoneNumber;
	private int appID;
	private String newCustomerAccount;
	private int sharedAccountRequestedID = 0;
	private String accepted;
	
	
	public Application(String firstName, String lastName, String address, int socialSecurityNum,
				long phoneNum, String newCustomerAccount){
		
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setAddress(address);
		this.setSocialSecurityNum(socialSecurityNum);
		this.setPhoneNumber(phoneNum);
		this.newCustomerAccount = newCustomerAccount;
		
		
		
	}
	public Application(int appID, String firstName, String lastName, String address, int socialSecurityNum,
			long phoneNum, String accepted, String newAccount, int sharedAccountRequestedId  ){
		this.appID = appID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.socialSecurityNum = socialSecurityNum;
		this.phoneNumber = phoneNum;
		this.accepted = accepted;
		this.newCustomerAccount = newAccount;
		this.sharedAccountRequestedID = sharedAccountRequestedId;
		
	}

	public int getAppID() {
		return appID;
	}



	public void setAppID(int appID) {
		this.appID = appID;
	}
	public void setNewCustomerAccount(String val) {
		newCustomerAccount = val;
	}
	public String getNewCustomerAccount() {
		return newCustomerAccount;
	}

	public int getSharedAccountRequestedID() {
		return sharedAccountRequestedID;
	}

	public void setSharedAccountRequestedID(int sharedAccountRequestedId) {
		this.sharedAccountRequestedID = sharedAccountRequestedId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSocialSecurityNum() {
		return socialSecurityNum;
	}

	public void setSocialSecurityNum(int socialSecurityNum) {
		this.socialSecurityNum = socialSecurityNum;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
}
