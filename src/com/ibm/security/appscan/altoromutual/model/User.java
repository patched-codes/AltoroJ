/**
This application is for demonstration use only. It contains known application security
vulnerabilities that were created expressly for demonstrating the functionality of
application security testing tools. These vulnerabilities may present risks to the
technical environment in which the application is installed. You must delete and
uninstall this demonstration application upon completion of the demonstration for
which it is intended. 

IBM DISCLAIMS ALL LIABILITY OF ANY KIND RESULTING FROM YOUR USE OF THE APPLICATION
OR YOUR FAILURE TO DELETE THE APPLICATION FROM YOUR ENVIRONMENT UPON COMPLETION OF
A DEMONSTRATION. IT IS YOUR RESPONSIBILITY TO DETERMINE IF THE PROGRAM IS APPROPRIATE
OR SAFE FOR YOUR TECHNICAL ENVIRONMENT. NEVER INSTALL THE APPLICATION IN A PRODUCTION
ENVIRONMENT. YOU ACKNOWLEDGE AND ACCEPT ALL RISKS ASSOCIATED WITH THE USE OF THE APPLICATION.

IBM AltoroJ
(c) Copyright IBM Corp. 2008, 2013 All Rights Reserved.
 */
package com.ibm.security.appscan.altoromutual.model;

import java.sql.SQLException;
import java.util.Date;

import com.ibm.security.appscan.altoromutual.util.DBUtil;

/**
 * This class models a user
 * @author Alexei
 *
 */
public class User implements java.io.Serializable{

	private static final long serialVersionUID = -4566649173574593144L;
	
	public static enum Role{User, Admin};
	
	private String username, firstName, lastName;
	private Role role = Role.User;
	
	private Date lastAccessDate = null;
	
	public User(String username, String firstName, String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		lastAccessDate = new Date();
	}
	
	public void setRole(Role role){
		this.role = role;
	}
	
	public Role getRole(){
		return role;
	}
	
	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Retrieves an array of Account objects for the current user.
	 * 
	 * This method attempts to fetch account information from the database
	 * using the DBUtil.getAccounts() method. If a SQLException occurs during
	 * the database operation, it prints the stack trace and returns null.
	 * 
	 * @return An array of Account objects if successful, or null if an error occurs
	 * @throws SQLException if a database access error occurs (caught internally)
	 */
	public Account[] getAccounts(){
		try {
			return DBUtil.getAccounts(username);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Looks up an Account object based on the provided account number.
	 * 
	 * @param accountNumber The unique identifier for the account to be retrieved
	 * @return The Account object matching the given account number, or null if no match is found
	 */
	public Account lookupAccount(Long accountNumber) {
		for (Account account : getAccounts()) {
			if (account.getAccountId() == accountNumber)
				return account;
		}
		return null;
	}
	
	/**
	 * Retrieves the credit card account number for the user.
	 * 
	 * This method iterates through the user's accounts and returns the account ID
	 * of the credit card account if found.
	 * 
	 * @return The account ID of the credit card account if found, or -1L if not found.
	 */
	public long getCreditCardNumber(){
		for (Account account: getAccounts()){
			if (DBUtil.CREDIT_CARD_ACCOUNT_NAME.equals(account.getAccountName()))
				return account.getAccountId();
		}
		return -1L;
	}
	
	/**
	 * Retrieves user transactions for specified accounts within a given date range.
	 * 
	 * @param startDate The start date of the transaction period (inclusive)
	 * @param endDate The end date of the transaction period (inclusive)
	 * @param accounts An array of Account objects to retrieve transactions for
	 * @return An array of Transaction objects matching the specified criteria
	 * @throws SQLException If a database access error occurs
	 */
	public Transaction[] getUserTransactions(String startDate, String endDate, Account[] accounts) throws SQLException {
		
		Transaction[] transactions = null;
		transactions = DBUtil.getTransactions(startDate, endDate, accounts, -1);
		return transactions; 
	}
}
