package com.bank.project0.DOA;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bank.project0.Account;
import com.bank.project0.Application;
import com.bank.project0.Customer;

import java.sql.ResultSet;


public class BankDatabaseAccessObject {
	
	private static Logger logger = Logger.getLogger(BankDatabaseAccessObject.class);
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String url = "jdbc:postgresql://cjportillo89.cwrr2qklkyum.us-east-2.rds.amazonaws.com:"
			+ "5432/cjportillo89";
	private String userName = "cjportillo89";
	private String passWord = "augmaticdisport22-";
	private String sql;
	
	public BankDatabaseAccessObject() {
		PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator +
				"log4j.properties");
	}

	public Connection getConnection() {
		return connection;
	}
	//Gets the current highest Id value in table
		public int getMaxID(String table) {
			int maxId = 0;
		    sql =  "SELECT MAX(application_id) as MaxId FROM " + table;
			try {
				resultSet = statement.executeQuery(sql);
				if(resultSet.next()) {
					maxId = resultSet.getInt("MaxId");
				}
				
			} catch (SQLException e) {
				System.out.println("An error has occured with trying to execute query");
				
			}
			maxId++;
			return maxId;
		}

	public void openConnection() {
		try {
			connection = DriverManager.getConnection(url, userName, passWord);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void submitApplication(Application application) {
		
		try {
			/*statement.executeUpdate("insert into applications values ( 1, 'Carlos', 'Portillo', "
					+ "'3737 N Calle Cancion', 400125454, "
					+ "2485337, '1'');");*/
			sql = "insert into applications values (" +application.getAppID() + ", '" + application.getFirstName() + "', '" + application.getLastName()
				  +"', '" + application.getAddress() + "', " + application.getSocialSecurityNum() + ", " + application.getPhoneNumber() + ", '" +
					0 + "', '" + application.getNewCustomerAccount() + "', " +application.getSharedAccountRequestedID() + ");";
			statement.execute(sql);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<Application> getPendingApplications() {
		
		ArrayList<Application> applications = new ArrayList<Application>();
		sql = "SELECT * FROM applications where accepted = '0';";
		try {
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				//System.out.println(resultSet.getInt("application_id"));
				applications.add(new Application(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), 
						resultSet.getLong(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return applications;
		
	}
	public boolean checkIfAccountExists(int accountId) {
		sql = "SELECT FROM * accounts Where =" + accountId +";";
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				if(resultSet.getString("accountholder2")!= "") {
					System.out.println("This account already belongs to somebody!");
				}
				else{
					String selectedChoice;
					System.out.println("This account belongs to " + resultSet.getString("accountholder1") + "\n is this "
							+ "correct? Enter Y for yes, press any other key for no");
					Scanner s2 = new Scanner(System.in);
					selectedChoice = s2.nextLine().toLowerCase();
					if(selectedChoice.equals("y")) {
						s2.close();
						return true;
					}
					s2.close();
					return false;
				}			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public Customer getCustomerInformation(int customerID) {
		sql = "Select * from customers where customer_id = " + customerID + ";";
		try {
			resultSet = statement.executeQuery(sql);
			int count = 0;
			while(resultSet.next()) {
				count++;
				Customer customer;
				int accountNumber1 = resultSet.getInt(7);
				int accountNumber2 = resultSet.getInt(8);
				customer = new Customer(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
						resultSet.getInt(5), resultSet.getLong(6), null, null);
				if(accountNumber1 == 0 && accountNumber2 == 0) {
					break;
				}
				else {
					Account account1 = null;
					Account account2 = null;
					ResultSet accountResultSet = null;
					if(accountNumber1!= 0) {
						sql = "Select * from accounts where account_id = " + resultSet.getInt(7) + ";";
						accountResultSet = statement.executeQuery(sql);
						accountResultSet.next();
						account1 = new Account(accountResultSet.getInt(1), accountResultSet.getInt(2), accountResultSet.getInt(3), 
								accountResultSet.getInt(4));
					}
					if(accountNumber2!= 0) {
						sql = "Select * from accounts where account_id = " + resultSet.getInt(8) + ";";
						accountResultSet = statement.executeQuery(sql);
						account2 = new Account(accountResultSet.getInt(1), accountResultSet.getInt(2), accountResultSet.getInt(3), 
								accountResultSet.getInt(4));
					}
					accountResultSet.close();
					customer.setAccount1(account1);
					customer.setAccount2(account2);
					
				}
				
				return customer;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void createCustomerAccount(Application application, int maxID, int accountMaxID) {
		sql = "Insert into customers values (" + maxID +  ", '" + application.getFirstName() +"', '" + application.getLastName() +"', '" + application.getAddress() +
				"', "+ application.getSocialSecurityNum() + ", " + application.getPhoneNumber() + ", " + accountMaxID + ", " + 0 + ");";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createBankAccount(int maxAccountID, int accountHolder1ID, int accountHolder2ID) {
		sql = "Insert into accounts values (" + maxAccountID + ", " + accountHolder1ID + ", " + accountHolder2ID + ", " + 0 + ");";
		
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
