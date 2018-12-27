package com.bank.project0;

public class Account {
	
	private int accountID;
	private int accountHolder1;
	private int accountHolder2;
	private float balance;
	
	public Account(int accountID, float balance, int accountHolder1, int accountHolder2) {
		this.accountID = accountID;
		this.accountHolder1 = accountHolder1;
		this.accountHolder2 = accountHolder2;
		this.balance = balance;
	}
	
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public int getAccountHolder1() {
		return accountHolder1;
	}
	public void setAccountHolder1(int accountHolder1) {
		this.accountHolder1 = accountHolder1;
	}
	public int getAccountHolder2() {
		return accountHolder2;
	}
	public void setAccountHolder2(int accountHolder2) {
		this.accountHolder2 = accountHolder2;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}

}
