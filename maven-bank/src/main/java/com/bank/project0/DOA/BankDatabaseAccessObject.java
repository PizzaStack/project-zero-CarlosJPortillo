package com.bank.project0.DOA;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bank.project0.Application;

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
	private String SQL;
	
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
		    SQL =  "SELECT MAX(application_id) as MaxId FROM " + table;
			try {
				resultSet = statement.executeQuery(SQL);
				if(resultSet.next()) {
					maxId = resultSet.getInt("MaxId");
				}
				
			} catch (SQLException e) {
				System.out.println("An error has occured with trying to execute query");
				maxId = 0;
			}
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
			statement.executeUpdate("insert into applications values ( 1, 'Carlos', 'Portillo', "
					+ "'3737 N Calle Cancion', 400125454, "
					+ "2485337, '1'');");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getPendingApplications() {
		int maxID = getMaxID("Applications");
		
	}
	public boolean checkIfAccountExists(int accountId) {
		SQL = "SELECT FROM * accounts Where =" + accountId +";";
		try {
			resultSet = statement.executeQuery(SQL);
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
						return true;
					}
					s2.close();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
