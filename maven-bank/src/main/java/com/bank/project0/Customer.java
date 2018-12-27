package com.bank.project0;
import java.util.ArrayList;

public class Customer extends Person implements Depositable, Withdrawable {

	private Account account1;
	private Account account2;
	private int totalActiveAccounts;
	private String address;
	private int socialSecurityNum;
	private long phoneNumber;
	
	public Customer(int id, String firstName, String lastName, String address, int socialSecurityNumber, long phoneNumber, Account account1,
			Account account2) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		this.address = address;
		this.socialSecurityNum = socialSecurityNumber;
		this.phoneNumber = phoneNumber;
		this.account1 = account1;
		this.account2 = account2;
		
	}

	public void deposit() {
		// TODO Auto-generated method stub
		
		
		
	}


	public void withdraw() {
		// TODO Auto-generated method stub
		
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



	public long getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Account getAccount1() {
		return account1;
	}


	public void setAccount1(Account account1) {
		this.account1 = account1;
	}


	public Account getAccount2() {
		return account2;
	}


	public void setAccount2(Account account2) {
		this.account2 = account2;
	}



}
