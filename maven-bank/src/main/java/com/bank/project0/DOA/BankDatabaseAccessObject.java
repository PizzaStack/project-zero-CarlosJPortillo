package com.bank.project0.DOA;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bank.project0.Account;
import com.bank.project0.Application;
import com.bank.project0.Customer;
import com.bank.project0.Employee;

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
		public int getMaxID(String table, String columnValue) {
			int maxId = 0;
		    sql =  "SELECT MAX( " + columnValue + ") as MaxId FROM " + table;
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
			logger.debug("Connection made to database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Failed to connect to database");
		}
	}
	public void submitApplication(Application application) {
		
		try {
			sql = "insert into applications values (" +application.getAppID() + ", '" + application.getFirstName() + "', '" + application.getLastName()
				  +"', '" + application.getAddress() + "', " + application.getSocialSecurityNum() + ", " + application.getPhoneNumber() + ", '" +
					0 + "', '" + application.getNewCustomerAccount() + "', " +application.getSharedAccountRequestedID() + ");";
			statement.execute(sql);
			logger.debug("application was submitted");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("failed to submited application to database");
		}
		
	}
	public ArrayList<Application> getPendingApplications() {
		
		ArrayList<Application> applications = new ArrayList<Application>();
		sql = "SELECT * FROM applications where accepted = '0' order by application_id;";
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
		sql = "Select customers.firstname, customers.lastname, accounts.accountholder1, accounts.accountholder2  from"
				+ " accounts inner join customers on \r\n" + "customers.account1_id = accounts.account_id  \r\n" + 
				"where account_id = " + accountId +";";
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				if(resultSet.getInt(3)!= 0 && resultSet.getInt(4)!= 0) {
					System.out.println("This account already belongs to somebody!");
				}
				else{
					String selectedChoice;
					System.out.println("This account belongs to " + resultSet.getString(1) + " " + resultSet.getString(2)+" is this "
							+ "correct?\n Enter Y for yes, press any other key for no");
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
		sql = "Select * from customers where customer_id = " + customerID + " order by customer_id;";
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
				resultSet.close();
				if(accountNumber1 == 0 && accountNumber2 == 0) {
					break;
				}
				else {
					Account account1 = null;
					Account account2 = null;
					ResultSet accountResultSet = null;
					if(accountNumber1!= 0) {
						sql = "Select * from accounts where account_id = " + accountNumber1 + ";";
						accountResultSet = statement.executeQuery(sql);
						accountResultSet.next();
						account1 = new Account(accountResultSet.getInt(1), accountResultSet.getFloat(2), accountResultSet.getInt(3), 
								accountResultSet.getInt(4));
						accountResultSet.close();
						accountResultSet = null;
					}
					if(accountNumber2!= 0) {
						
						sql = "Select * from accounts where account_id = " + accountNumber2 + ";";
						accountResultSet = statement.executeQuery(sql);
						accountResultSet.next();
						account2 = new Account(accountResultSet.getInt(1), accountResultSet.getFloat(2), accountResultSet.getInt(3), 
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
	
	public void createCustomerAccount(Application application, int maxID, int accountID) {
		sql = "Insert into customers values (" + maxID +  ", '" + application.getFirstName() +"', '" + application.getLastName() +"', '" + application.getAddress() +
				"', "+ application.getSocialSecurityNum() + ", " + application.getPhoneNumber() + ", " + accountID + ", " + 0 + ");";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createBankAccount(int maxAccountID, int accountHolder1ID, int accountHolder2ID) {
		sql = "Insert into accounts values (" + maxAccountID + ", " + 0.0 + "," + accountHolder1ID + ", " + accountHolder2ID + ");";
		
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Employee getEmployeeInformation(int employee_id, String password) {
		sql = "Select * from employees where employee_id = " + employee_id + " and  password = '" + password + "';";
		try {
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
				return employee;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeConnection() {
		try {
			connection.close();
			logger.debug("connection closed successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("failed to close connection");
		}
	}

	public void addCustomerToAccount(int sharedAccountRequestedID, int newAccountHolder) {
		// TODO Auto-generated method stub
		sql = "Update accounts SET accountholder2 = " + newAccountHolder  + " where account_id = " + sharedAccountRequestedID + ";";
		try {
			statement.executeUpdate(sql);
			logger.debug("Added new account holder to account: " + sharedAccountRequestedID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean validateUser(int customerID, int socialSecurityNumber ) {
		sql = "Select * from customers where customer_id = " + customerID + " and socialnumber = " + socialSecurityNumber + ";";
		try {
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public void deposit(float dollarAmount, int id) {
		sql = "update accounts set balance = balance + " + dollarAmount + " where account_id =  "  + id + ";";
		try {
			statement.executeUpdate(sql);
			logger.debug("deposited " + dollarAmount + " into account: " + id);
		} catch (SQLException e) {
			logger.error("failed to deposit money into account: " + id);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void withdraw(float dollarAmount, int id) {
		sql = "update accounts set balance = " + dollarAmount + " where account_id = " + id + ";";
		try {
			statement.executeUpdate(sql);
			logger.debug("withdrew money into account");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int getCustomerId(int socialSecurityNum) {
		sql = "select customer_id from customers where socialnumber = " + socialSecurityNum + ";";
		try {
			resultSet = statement.executeQuery(sql);
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			logger.error("Failed to retrieve customer ID :" + socialSecurityNum);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public void addAccountToCustomer(int customerID, int accountMaxID) {
		sql = "update customers set account2_id = " + accountMaxID + "where customer_id = " + customerID + ";";
		try {
			statement.executeUpdate(sql);
			logger.debug("Added a bank account to customer: " + customerID);
		} catch (SQLException e) {
			logger.error("Failed to bank account to customer: " + customerID);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void callableDeleteApplication(int applicationID) {
		try {
			CallableStatement stmt = connection.prepareCall("{call deleteapplication(?)}");
			stmt.setInt(1, applicationID);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
