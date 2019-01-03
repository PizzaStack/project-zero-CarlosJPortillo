package com.bank.project0;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.bank.project0.DOA.*;

public class Menu {

	private Scanner s1 = new Scanner(System.in);
	private String selectedChoice;
	private BankDatabaseAccessObject bankDAO = new BankDatabaseAccessObject();
	
	public Menu() {
		bankDAO.openConnection();
	}
	
	public void printMenu() {
		System.out.println("Who are you? \n" + "1: A customer\n" + "2: An employee\n" + "3: Exit application \n");
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
					int id = (int)validateNumberInput("Please enter your employee ID number, use only whole numbers?");
					System.out.println("Please enter the password associated with this ID number");
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
				bankDAO.closeConnection();
				return true;
		
			default: 
				System.out.println("Sorry that is not a valid option!");
				break;
		}
		return false;
	}
	public void printCustomerOptions() {
		selectedChoice = "";
		System.out.println("Are you a new customer: Enter Y for Yes or N for No");
		selectedChoice = s1.nextLine().toLowerCase();
		if(selectedChoice.equals("y")) {
			System.out.println("Are you looking to create a new account with us \n Enter Y for Yes \n "
					+ " N to Return to main menu");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("n")) {
				return;
			}
			Application application = CreateCustomerApplication();
			if(application!= null) {
				processApplicationMenu(application);
			}
							
		}
		else if(selectedChoice.equals("n")) {
			try {
				int customerID = (int)validateNumberInput("Please enter your customer id number");
				int socialSecurityNumber = (int)validateNumberInput("Please enter your social security number");
				if(bankDAO.validateUser(customerID, socialSecurityNumber, false)) {
					Customer customer = bankDAO.getCustomerInformation(customerID);
					printCustomerInformation(customer);
					printCustomerAdminAccountOptions(customer, false);
				}
				
			}
			catch(ClassCastException ex) {
				System.out.println("Sorry but not such Id or social security combination exists");
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
						System.out.print("APPLICATION ID:: "+ application.getAppID() + " FIRST NAME:: " + application.getFirstName() + " LAST NAME:: " + 
					    application.getLastName() + " ADDRESS:: " + application.getAddress() + "SOCIAL SECURITY #:: " + application.getSocialSecurityNum());
						if(application.getSharedAccountRequestedID()!= 0) {
							System.out.print(" REQUESTED SHARED ACCOUNT ID:: " + application.getSharedAccountRequestedID());
						}
						if(application.getNewCustomerAccount().equals("t")) {
							System.out.print("***New Customer Request***");
						}
						System.out.println();
					}
					int applicationID;
					System.out.print("Do you wish to approve or reject any of the applications? \n Enter Y for yes \n Enter any other key for return to main"
							+ "menu ");
					selectedChoice = s1.nextLine().toLowerCase();
					while(selectedChoice.equals("y") && !applications.isEmpty()) {
						applicationID = (int)validateNumberInput("Enter the application id of the application you wish to approve or reject");
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
										bankDAO.addCustomerToAccount(applicationChosen.getSharedAccountRequestedID(), maxCustomerID);						
									}
									applications.remove(applicationChosen);
									bankDAO.callableDeleteApplication(applicationChosen.getAppID());
									
								}
								else if(applicationChosen.getNewCustomerAccount().equals("f")) {
									int customerID = bankDAO.getCustomerId(applicationChosen.getSocialSecurityNum());
									boolean activeAccount= bankDAO.checkActiveAccount(customerID);
									if(applicationChosen.getSharedAccountRequestedID() == 0) {
										bankDAO.createBankAccount(accountMaxID, customerID, 0);
										bankDAO.addAccountToCustomer(customerID, accountMaxID, activeAccount);
									}
									else {
										bankDAO.addAccountToCustomer(customerID, applicationChosen.getSharedAccountRequestedID(), activeAccount);
										bankDAO.addCustomerToAccount(applicationChosen.getSharedAccountRequestedID(), customerID);
										
									}
									applications.remove(applicationChosen);
									bankDAO.callableDeleteApplication(applicationChosen.getAppID());
								}
							}
							else {
								bankDAO.callableDeleteApplication(applicationChosen.getAppID());
							}
						}
						else {
							System.out.println("Not a valid application id number");
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
			if(customer == null) {
				System.out.println("No such customer exists with that id number!");
			}
			else {
				printCustomerInformation(customer);
				if(employee.getAdminPriveleges().equals("t")) {
					printCustomerAdminAccountOptions(customer, true);
				}
				else {
					System.out.println("Do you wish to exit this menu screen? \n Enter Y to stay on this menu \n Enter any other key to return back to the "
							+ "main menu screen");
					selectedChoice = s1.nextLine().toLowerCase();
				}
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
		boolean flag = bankDAO.validateUser(0, socialSecurityNum, true);
		if(flag == true) {
			System.out.println("Sorry, it appears that social security number belongs to somebody else in the database");
			return null;
		}
		phoneNumber = validateNumberInput("Please enter your phone number with no dashes or spaces");
		

	//1phoneNum = s1.nextInt();
		
		return new Application(firstName, lastName, address, socialSecurityNum, phoneNumber, "1");
	}
	public long validateNumberInput(String stringMessage) {
		long value = 0;
		System.out.println(stringMessage);
		while(!s1.hasNextLong()) {
			System.out.println(stringMessage);
			s1.nextLine();
						
		}
		value = s1.nextLong();
		s1.nextLine();
		return value;		
	}
	public void printCustomerInformation(Customer customer) {
		
		System.out.println("FIRST NAME: " + customer.getFirstName() + " LAST NAME: " + customer.getLastName() + " ADDRESS: " + customer.getAddress()+
				" SOCIAL SECURITY #: " + customer.getSocialSecurityNum() + " PHONE NUMBER: " + customer.getPhoneNumber() );
		if(customer.getAccount1() == null && customer.getAccount2() == null) {
			System.out.println("**NO ACTIVE ACCOUNTS**");
		}
		else {
			String balanceString1 = String.format("%.2f", customer.getAccount1().getBalance());
			System.out.println("ACTIVE ACCOUNTS::: ACCOUNT ID:" + customer.getAccount1().getAccountID() + " BALANCE: " + balanceString1);
			if(customer.getAccount2()!= null) {
				String balanceString2 = String.format("%.2f", customer.getAccount2().getBalance());
				System.out.println("\t \t   ACCOUNT ID:" + customer.getAccount2().getAccountID() + " BALANCE: " + balanceString2);
			}
		}	
		
	}
	public void printCustomerAdminAccountOptions(Customer customer, boolean admin) {
		do{
			float dollarAmount = 0.00f;
			String accountChecker = "";
			System.out.println("Enter an option \n D: Deposit Money \n W: Withdraw Money \n T: Transfer Money");
			if(admin == false) {
				System.out.println(" S: Submit new Application");
			}
			else if(admin == true) {
				System.out.println(" C: Cancel an account");
			}
			selectedChoice = s1.nextLine().toLowerCase();
			switch(selectedChoice) {
			case "d":
				accountChecker = displayAccountsAvailable(customer);
				if(!accountChecker.equals("0 accounts")) {
					if(accountChecker.equals("1 account")) {
						float newBalance = depositDisplay(customer.getAccount1().getAccountID(), "first account", customer.getAccount1().getBalance(), dollarAmount, false);
						customer.getAccount1().setBalance(newBalance);
					}
					else if(accountChecker.equals("2 accounts")) {
						chooseBetweenAccounts();
						if(selectedChoice.equals("1")) {
							float newBalance = depositDisplay(customer.getAccount1().getAccountID(), "first account", customer.getAccount1().getBalance(), dollarAmount, false);
							customer.getAccount1().setBalance(newBalance);
						}
						else if(selectedChoice.equals("2")) {
							float newBalance = depositDisplay(customer.getAccount2().getAccountID(), "second account", customer.getAccount2().getBalance(), dollarAmount, false);
							customer.getAccount2().setBalance(newBalance);
						}		
					}
				}
				break;
			case "w":
					accountChecker = displayAccountsAvailable(customer);
					if(!accountChecker.equals("0 accounts")) {
						if(accountChecker.equals("1 account")) {
							float newBalance = withdrawDisplay(customer.getAccount1().getAccountID(), "first account", dollarAmount,
									customer.getAccount1().getBalance(), false);
							customer.getAccount1().setBalance(newBalance);
							
						}
						else if(accountChecker.equals("2 accounts")) {
							chooseBetweenAccounts();
							if(selectedChoice.equals("1")) {
								float newBalance = withdrawDisplay(customer.getAccount1().getAccountID(), "first account", dollarAmount,
										customer.getAccount1().getBalance(), false);
								customer.getAccount1().setBalance(newBalance);
							}
							else if(selectedChoice.equals("2")) {
								float newBalance = withdrawDisplay(customer.getAccount2().getAccountID(), "second account", dollarAmount,
										customer.getAccount2().getBalance(), false);
								customer.getAccount2().setBalance(newBalance);
							}		
						}
					}	
				break;
			case "t":
				if(customer.getAccount1()== null || customer.getAccount2()== null) {
					System.out.println("Sorry but you don't have 2 accounts to transfer money between ");
				}
				else {
					System.out.println("Please enter what accont you want to pull money from to deposit into the other");
					chooseBetweenAccounts();
					String dollarAmountString;
					if(selectedChoice.equals("1")) {
						System.out.println("Enter how much you want to take out of acccount 1 ");
						dollarAmount = validateMoneyValue();
						float newBalance1 = withdrawDisplay(customer.getAccount1().getAccountID(), "", dollarAmount, customer.getAccount1().getBalance(), true);
						customer.getAccount1().setBalance(newBalance1);
						float newBalance2 = depositDisplay(customer.getAccount2().getAccountID(), "second account", customer.getAccount2().getBalance(), dollarAmount, true);
						customer.getAccount2().setBalance(newBalance2);
						String newBalanceString1 = String.format("%.2f", newBalance1);
						String newBalanceString2 = String.format("%.2f", newBalance2);
						System.out.println("Fist Account Current balance " + newBalanceString1);
						System.out.println("Second Account Current balance " + newBalanceString2);
						
					}
					else {
						System.out.println("Enter how much you want to take out of acccount 2 ");
						dollarAmount = validateMoneyValue();
						float newBalance1 = withdrawDisplay(customer.getAccount2().getAccountID(), "", dollarAmount, customer.getAccount2().getBalance(), true);
						customer.getAccount2().setBalance(newBalance1);
						float newBalance2 = depositDisplay(customer.getAccount1().getAccountID(), "first account", customer.getAccount1().getBalance(), dollarAmount, true);
						customer.getAccount1().setBalance(newBalance2);
						dollarAmountString = String.format("%.2f", dollarAmount);
						String newBalanceString1 = String.format("%.2f", newBalance1);
						String newBalanceString2 = String.format("%.2f", newBalance2);
						System.out.println("Second Account Current balance " + newBalanceString1);
						System.out.println("First Account Current balance " + newBalanceString2);

					}
				}
				break;
			case "s":
				if(admin == false) {
					if(customer.getAccount2()== null) {
						Application application = new Application(customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getSocialSecurityNum(),
								customer.getPhoneNumber(), "0");
						processApplicationMenu(application);
					}
					else {
						System.out.println("You already have 2 active acccounts associated with this customer, cannot submit another application to create another account");
					}
				}
				break;
			case "c":
				if(admin == true) {
					if(customer.getAccount1()== null && customer.getAccount2()== null) {
						System.out.println("There are no active accouts associated with this customer");
					}
					else {
						if(customer.getAccount1()!= null && customer.getAccount2() == null) {
							System.out.println("Do you want to delete the only account active with this customer? \n Enter Y for yes \n Enter anything else for no");
							selectedChoice = s1.nextLine().toLowerCase();
							if(selectedChoice.equals("y")) {
								bankDAO.callableDeleteAccount(customer.getAccount1().getAccountID());
								bankDAO.callableUpdateCustomerAfterDeletedAccount(customer.getAccount1().getAccountID());
								System.out.println("Bank account: " + customer.getAccount1().getAccountID() + " has been terminated");
								customer.setAccount1(null);
							}
						}
						else {
							System.out.println("Please enter what account you want to delete from \n Enter either 1 or 2 as your response");
							chooseBetweenAccounts();
							if(selectedChoice.equals("1")) {
								bankDAO.callableDeleteAccount(customer.getAccount1().getAccountID());
								bankDAO.callableUpdateCustomerAfterDeletedAccount(customer.getAccount1().getAccountID());
								System.out.println("Bank account: " + customer.getAccount1().getAccountID() + " has been terminated");
								customer.setAccount1(customer.getAccount2());
								customer.setAccount2(null);
								
							}
							else if(selectedChoice.equals("2")) {
								bankDAO.callableDeleteAccount(customer.getAccount2().getAccountID());
								bankDAO.callableUpdateCustomerAfterDeletedAccount(customer.getAccount2().getAccountID());
								System.out.println("Bank account: " + customer.getAccount2().getAccountID() + " has been terminated");
								customer.setAccount2(null);
							}
						}
						}
					}
				}
				System.out.println("Do you wish to exit this menu screen? \n Enter Y to stay on this menu \n Enter any other key to return back to the "
						+ "main menu screen");
				selectedChoice = s1.nextLine().toLowerCase();
			}while(selectedChoice.equals("y"));

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
	            	s1.nextLine();
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
		do {
			selectedChoice = s1.nextLine();
		}while(!selectedChoice.equals("1") && !selectedChoice.equals("2"));
	}
	public float depositDisplay(int accountID, String accountString, float balanceAmount, float dollarAmount, boolean transferOption) {
		if(!transferOption) {
			dollarAmount = validateMoneyValue();
		}
		bankDAO.deposit(dollarAmount, accountID);
		String dollarAmountString = String.format("%.2f", dollarAmount);
		if(transferOption == false) {
			
			String currentBalanceString = String.format("%.2f", balanceAmount +dollarAmount);
			System.out.println("You despoited " + dollarAmount + " into your " + accountString + "\n Current Balance: " + currentBalanceString);
		}
		else {
			if(accountString.equals("second account")) {
				System.out.println("You transfered $" + dollarAmountString + " from the first account to the second account ");	
			}
			else {
				System.out.println("You transfered $" + dollarAmountString + " from the second account to the first account ");
			}
		}
		return (balanceAmount + dollarAmount);
	}
	public float withdrawDisplay(int accountID, String accountString, float dollarAmount, float balanceAmount, boolean transferOption) {
		if(!transferOption)
		{
			dollarAmount = validateMoneyValue();
		}
		if(balanceAmount - dollarAmount < 0) {
			System.out.println("The amount requested to draw exceeds more than the money currently available in this account!");
		}
		else {
			bankDAO.withdraw((balanceAmount - dollarAmount), accountID);
			if(transferOption == false) {
				String dollarAmountString = String.format("%.2f", dollarAmount);
				String currentBalanceString = String.format("%.2f", balanceAmount- dollarAmount);
				System.out.println("You withdrew $" + dollarAmountString + " from your " + accountString + "\n Current Balance: " + currentBalanceString);
			}
			
		}
		return (balanceAmount - dollarAmount);
		
	}
	public void processApplicationMenu(Application application) {
		int sharedAccountNumber = 0;
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
					application.setSharedAccountRequestedID(sharedAccountNumber);
				}
			}
		}
		bankDAO.submitApplication(application);
		System.out.println("Application has been submitted to the bank");
	}
	

}
