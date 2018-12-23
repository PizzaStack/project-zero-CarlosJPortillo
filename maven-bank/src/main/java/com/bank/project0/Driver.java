package com.bank.project0;

import java.util.Scanner;

public class Driver {
	
	public static void main(String[] args) {
		Menu menu = new Menu();
		boolean cont = true;
		
		while(!false) {
			menu.printMenu();
			cont = menu.getChoice();
		}
		
		
	}
	
}
