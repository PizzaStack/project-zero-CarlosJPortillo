package com.bank.project0;

import java.util.Scanner;
import java.io.IOException;
import com.bank.project0.DOA.*;

public class Menu {

	private Scanner s1 = new Scanner(System.in);
	private String selectedChoice;
	
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
			BankDatabaseAccessObject bankDAO = new BankDatabaseAccessObject();
			bankDAO.openConnection();
			int maxId = bankDAO.getMaxID("applications");
			application.setAppID(maxId);
			System.out.println("Are you looking to start a shared account with an already prexisting account?");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("y")) {
				sharedAccountNumber = (int)validateNumberInput("Please enter the account number asscociated to the account you "
						+ "wish to be shared with, use numbers only");
				bankDAO.checkIfAccountExists(sharedAccountNumber);	
			}
			bankDAO.submitApplication(application);
			
		}
		
		
}
	public void printEmployeeOptions() {
		do{
			System.out.println("Do you want to see what pending applications there are?\n"
					+ " Enter Y for yes\n"
					+ " Enter anything to return to main menu");
			selectedChoice = s1.nextLine().toLowerCase();
			if(selectedChoice.equals("y")) {
				//uses Datbase object to connect to server to display pending applications in datbase
				//give him the option to edit ones in table
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
			if(s1.hasNextLong()) {
				value = s1.nextLong();
			}
			else {
				System.out.println(stringMessage);
				s1.nextLine();
				
			}			
		}
		return value;		
	}
	private String getSelectedChoice() {
		return this.selectedChoice;
	}

}
