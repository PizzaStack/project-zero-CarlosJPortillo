package com.bank.project0;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import com.bank.project0.DOA.*;

public class Menu {

	private Scanner s1 = new Scanner(System.in);
	private String selectedChoice;
	private BankDatabaseAccessObject bankDAO = new BankDatabaseAccessObject();
	
	public Menu() {
		bankDAO.openConnection();
	}
	
	public void printMenu() {
		System.out.println("Who are you? \n" + "1: A customer\n" + "2: An employee\n" + "3: An admin\n" + "4: Exit application \n");
	}
	public boolean getChoice() {
		selectedChoice = s1.nextLine();
		switch(selectedChoice) {
			case "1":
				System.out.println();
				printCustomerOptions();
				break;
			case "2":
				Employee employee;
				try {
					int id = (int)validateNumberInput("Please enter your employee ID number?");
					System.out.println("Please enter the password associated with this ID number");
					s1.nextLine();
					String password = s1.nextLine();
					employee = bankDAO.getEmployeeInformation(id, password);
					if(employee == null) {
						System.out.println("Sorry but that combination of ID value and password is wrong!");
					}
					else {
						printEmployeeOptions(employee);
					}
				}
				catch(ClassCastException ex){
					System.out.println("Sorry but that combination of ID value and password is wrong!");
				}
				
			
				break;
			case "3":
				validateMoneyValue();
				System.out.println();
				printAdminOptions();
				break;
			case "4":
				return true;
		
			default: 
				System.out.println("Sorry that is not a valid option!");
				break;
		}
		return false;
	}
	public void printCustomerOptions() {
		selectedChoice = "";
		int sharedAccountNumber = 0;
		System.out.println("Are you a new customer: Enter Y for Yes or N for No");
		selectedChoice = s1.nextLine().toLowerCase();
		if(selectedChoice.equals("y")) {
			System.out.println("Are you looking to create a new account with us \n Enter Y for Yes \n "
					+ " N to Return to main menu");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("N")) {
				
			}
			Application application = CreateCustomerApplication();
			int maxId = bankDAO.getMaxID("applications", "application_id");
			application.setAppID(maxId);
			System.out.println("Are you looking to start a shared account with an already prexisting account? \n Enter Y for yes"
					+ "; enter any other key to create a new account that is not prexisting");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("y")) {
				boolean accountExists = false;
				while(accountExists == false) {
					sharedAccountNumber = (int)validateNumberInput("Please enter the account number asscociated to the account you "
							+ "wish to be shared with, use numbers only");
					s1.nextLine();
					accountExists = bankDAO.checkIfAccountExists(sharedAccountNumber);
					
					if(accountExists == false) {
						System.out.println("Do you wish to try again and enter the account number you wanted to share an account with\n"
								+ "enter Y for yes; enter any other key to return back to main menu");
						selectedChoice = s1.nextLine();
						if(selectedChoice.equals("no")) {
							return;
						}
					}
					else {
						application.setNewCustomerAccount("0");
						application.setSharedAccountRequestedID(sharedAccountNumber);
					}
				}
			}
			else {
				application.setNewCustomerAccount("1");
			}
			bankDAO.submitApplication(application);
			System.out.println("Application has been submitted to the bank");
			
		}
		else if(selectedChoice.equals("n")) {
			try {
				int customerID = (int)validateNumberInput("Please enter your customer id number");
				int socialSecurityNumber = (int)validateNumberInput("Please enter your social security number");
				if(bankDAO.validateUser(customerID, socialSecurityNumber)) {
					Customer customer = bankDAO.getCustomerInformation(customerID);
					printCustomerInformation(customer);
					printCustomerAccountOptions(customer);
				}
				
			}
			catch(ClassCastException ex) {
				System.out.println("Sorry but not such Id or social security combination existis");
			}
		}
		
		
	}
	public void printEmployeeOptions(Employee employee) {
		do{
			System.out.println("Do you want to see what pending applications there are?\n"
					+ " Enter Y for yes\n"
					+ " Enter N to instead see customer information\n"
					+ " Enter anything else to return to main menu");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("y")) {
				ArrayList<Application> applications = bankDAO.getPendingApplications();
				if(applications.size() == 0) {
					System.out.println("There are no currently no pending applications");
				}
				else {
					for(Application application: applications){
						System.out.println("APPLICATION ID:: "+ application.getAppID() + " FIRST NAME:: " + application.getFirstName() + " LAST NAME:: " + 
					    application.getLastName() + "ADDRESS:: " + application.getAddress() + "SOCIAL SECURITY #:: " + application.getSocialSecurityNum());
					}
					int applicationID;
					System.out.print("Do you wish to approve or reject any of the applications? \n Enter Y for yes \n Enter any other key for return to main"
							+ "menu ");
					selectedChoice = s1.nextLine().toLowerCase();
					while(selectedChoice.equals("y") && !applications.isEmpty()) {
						applicationID = (int)validateNumberInput("Enter the application id of the application you wish to approve or reject");
						s1.nextLine();
						Application applicationChosen = null;
						for(Application application: applications) {
							if(application.getAppID() == applicationID) {
								applicationChosen = application;
								break;
							}
						}
						if(applicationChosen!= null) {
							System.out.println("Do you wish to approve to reject this application\n Enter Y for approve\n Enter anything else to reject");
							selectedChoice = s1.nextLine().toLowerCase();
							int accountMaxID = bankDAO.getMaxID("accounts", "account_id");
							if(selectedChoice.equals("y")) {
								if(applicationChosen.getNewCustomerAccount().equals("t")) {
									int maxCustomerID = bankDAO.getMaxID("customers", "customer_id");
									if(applicationChosen.getSharedAccountRequestedID() == 0) {
										bankDAO.createCustomerAccount(applicationChosen, maxCustomerID, accountMaxID);
										bankDAO.createBankAccount(accountMaxID, maxCustomerID, 0);
									}
									else {
										bankDAO.createCustomerAccount(applicationChosen, maxCustomerID, applicationChosen.getSharedAccountRequestedID());
										bankDAO.addAccountToCustomer(applicationChosen.getSharedAccountRequestedID(), maxCustomerID);						
									}
									applications.remove(applicationChosen);
								}
							}
							else {
								
							}
						}
						else {
							
						}	
						if(applications.isEmpty()) {
							System.out.println("There are no more pending applications available");
						}
						else {
							System.out.println("Do you wish to approve or reject any more of the applications?");
							selectedChoice = s1.nextLine().toLowerCase();
						}
					}
				}
				
		}else if(selectedChoice.equals("n")) {
			int customerID = (int)validateNumberInput("Please enter the customer's id number with whom you wish to see details about");
			Customer customer = bankDAO.getCustomerInformation(customerID);
			if(customer.equals(null)) {
				System.out.println("No such customer exists with that id number!");
			}
			else {
				printCustomerInformation(customer);
			}
					
		}
		}while(selectedChoice.equals("y"));
	}
	public void printAdminOptions() {
		
	}
	public Application CreateCustomerApplication() {
		String firstName;
		String lastName;
		String address;
		int socialSecurityNum;
		long phoneNumber;
		
		System.out.println("Please Enter your first name");
		firstName = s1.nextLine();
		System.out.println("Please  your last name");
		lastName = s1.nextLine();
		System.out.println("Please enter your address");
		address = s1.nextLine();
		socialSecurityNum = (int) validateNumberInput("Please enter your social security number with no dashes or spaces");
		s1.nextLine();
		phoneNumber = validateNumberInput("Please enter your phone number with no dashes or spaces");
		s1.nextLine();

	//1phoneNum = s1.nextInt();
		
		return new Application(firstName, lastName, address, socialSecurityNum, phoneNumber);
	}
	public long validateNumberInput(String stringMessage) {
		long value = 0;
		System.out.println(stringMessage);
		while(!s1.hasNextLong()) {
			System.out.println(stringMessage);
			s1.nextLine();
						
		}
		value = s1.nextLong();
		return value;		
	}
	public void printCustomerInformation(Customer customer) {
		System.out.println("FIRST NAME: " + customer.getFirstName() + " LAST NAME: " + customer.getLastName() + " ADDRESS: " + customer.getAddress()+
				" SOCIAL SECURITY #: " + customer.getSocialSecurityNum() + " PHONE NUMBER: " + customer.getPhoneNumber() );
		if(customer.getAccount1() == null && customer.getAccount2() == null) {
			System.out.println("**NO ACTIVE ACCOUNTS**");
		}
		else {
			System.out.println("ACTIVE ACCOUNTS::: ACCOUNT ID:" + customer.getAccount1().getAccountID() + " BALANCE: " + customer.getAccount1().getBalance());
			if(customer.getAccount2()!= null) {
				System.out.println("\t \t   ACCOUNT ID:" + customer.getAccount1().getAccountID() + " BALANCE: " + customer.getAccount1().getBalance());
			}
		}	
		
	}
	public void printCustomerAccountOptions(Customer customer) {
		while(true) {
			float dollarAmount = 0.00f;
			String accountChecker = "";
			System.out.println("Enter an option \n D: Deposit Money \n W: Withdraw Money \n T: Transfer Money \n C: Create New Account");
			selectedChoice = s1.nextLine().toLowerCase();
			switch(selectedChoice) {
			case "d":
				accountChecker = displayAccountsAvailable(customer);
				if(!accountChecker.equals("0 accounts")) {
					if(accountChecker.equals("1 account")) {
						depositDisplay(customer.getAccount1().getAccountID(), "You despoited " + dollarAmount + " into your first account", dollarAmount);
					}
					else if(accountChecker.equals("2 accounts")) {
						chooseBetweenAccounts();
						if(selectedChoice.equals("1")) {
							depositDisplay(customer.getAccount1().getAccountID(), "You despoited " + dollarAmount + " into your first account", dollarAmount);
						}
						else if(selectedChoice.equals("2")) {
							depositDisplay(customer.getAccount2().getAccountID(), "You despoited " + dollarAmount + " into your first account", dollarAmount);
						}		
					}
				}
				break;
			case "w":
					accountChecker = displayAccountsAvailable(customer);
					if(!accountChecker.equals("0 accounts")) {
						if(accountChecker.equals("1 accont")) {
							
						}
					}
					
				break;
			case "t":
				break;
			case "c":
				break;
			
			}
			System.out.println("Do you wish to exit this menu screen? \n Enter Y to exit and return back to the previous screen");
		}	
	}
	public float validateMoneyValue() {
		float myFloat;
		while (true) {
	        System.out.print("Enter a float: ");
	        try {
	            myFloat = s1.nextFloat();
	            if (myFloat < 0) {
	              System.out.println("Cannot have negative numbers!");
	            }
	            else {
	            	break;
	            }
	        } catch (InputMismatchException ime) {
	            System.out.println(ime.toString());
	            s1.next(); // Flush the buffer from all data
	        }
	    }
		return myFloat;
	}
	public String displayAccountsAvailable(Customer customer) {
		String s1 = "";
		if(customer.getAccount1() == null && customer.getAccount2() == null){
			System.out.println("There have no accounts that you can work with this customer account!");
			s1 = "0 accounts";
		}
		else if(customer.getAccount1()!= null && customer.getAccount2() == null) {
			s1 = "1 account";
		}
		else if(customer.getAccount1()!= null && customer.getAccount2()!= null) {
			System.out.println("Choose from either account 1 or 2 to work with");
			s1 = "2 accounts";
		}
		return s1;
		
	}
	public void chooseBetweenAccounts() {
		while(selectedChoice.equals("1") || selectedChoice.equals("2")) {
			selectedChoice = s1.nextLine();
			
		}
	}
	public void depositDisplay(int accountID, String depositMessage, float dollarAmount) {
		dollarAmount = validateMoneyValue();
		bankDAO.deposit(dollarAmount, accountID);
		System.out.println("You despoited " + dollarAmount + " into your first account");
	}
	public void withDrawDisplay(int accountID, String withDrawMessage, float dollarAmount) {
		
	}
	
	private String getSelectedChoice() {
		return this.selectedChoice;
	}
	

}
