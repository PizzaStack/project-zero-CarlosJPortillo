package com.bank.project0;

import java.util.ArrayList;
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
				System.out.println();
				printEmployeeOptions();
				break;
			case "3":
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
			int maxId = bankDAO.getMaxID("applications");
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
						application.setNewAccount("1");
						application.setSharedAccountRequestedID(sharedAccountNumber);
					}
				}
			}
			else {
				application.setNewAccount("0");
			}
			bankDAO.submitApplication(application);
			
		}
		
		
}
	public void printEmployeeOptions() {
		do{
			System.out.println("Do you want to see what pending applications there are?\n"
					+ " Enter Y for yes\n"
					+ " Enter N to instead see customer information\n"
					+ " Enter anything else to return to main menu");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("y")) {
				ArrayList<Application> applications = bankDAO.getPendingApplications();
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
					try {
						Application application;
						application = applications.get(applicationID -1 );
						System.out.println("Do you wish to approve to reject this application\n Enter Y for approve\n Enter anything else to reject");
						if(selectedChoice.equals("Y")) {
							
						}
						else {
							
						}
						
					}
					catch(IndexOutOfBoundsException ex){
						System.out.println("Sorry, but that number is not part of any of the application id values currently pending");
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
	private String getSelectedChoice() {
		return this.selectedChoice;
	}

}
