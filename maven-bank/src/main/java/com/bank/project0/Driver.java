package com.bank.project0;


public class Driver {
	
	public static void main(String[] args) {
		Menu menu = new Menu();
		boolean cont = false;
		
		while(cont != true) {
			menu.printMenu();
			cont = menu.getChoice();
		}
		
		System.out.println("You have exited the application. Have a nice day!");
	}
	
}
