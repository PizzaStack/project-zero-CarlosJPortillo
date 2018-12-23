package com.bank.project0;
import java.util.ArrayList;

public class Customer extends Person implements Depositable, Withdrawable {

	private ArrayList<Account> activeAccounts;
	private int totalActiveAccounts;
	private String address;
	private int socialSecurityNum;
	
	

	public void deposit() {
		// TODO Auto-generated method stub
		
		
		
	}


	public void withdraw() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Account> getActiveAccounts() {
		return activeAccounts;
	}

	public void setActiveAccounts(ArrayList<Account> activeAccounts) {
		this.activeAccounts = activeAccounts;
	}

	public int getTotalActiveAccounts() {
		return totalActiveAccounts;
	}

	public void setTotalActiveAccounts(int totalActiveAccounts) {
		this.totalActiveAccounts = totalActiveAccounts;
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

}
