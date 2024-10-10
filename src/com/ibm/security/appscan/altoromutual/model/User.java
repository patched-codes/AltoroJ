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
	
	/**
	* Sets the role for this object.
	* 
	* @param role The Role object to be set
	*/	public void setRole(Role role){
		this.role = role;
	}
	
	/**
	* Retrieves the role associated with this object.
	*
	* @return The Role object representing the current role.
	*/
	public Role getRole(){
		return role;
	}
	
	/**
	* Retrieves the last access date.
	*
	* @return The Date object representing the last access date
	*/
	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	/**
	* Sets the last access date for this object.
	* 
	* @param lastAccessDate The Date object representing the last access date to be set
	*/
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	/**
	* Retrieves the username associated with this object.
	* 
	* @return The username as a String
	*/
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the first name of the person.
	 * 
	 * @return The first name as a String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	* Gets the last name of the person.
	* 
	* @return The last name as a String
	*/
	public String getLastName() {
		return lastName;
	}
	
	/**
	* Retrieves an array of Account objects for the current user.
	* 
	* @return An array of Account objects associated with the user, or null if an error occurs
	* @throws SQLException if there's an error accessing the database
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
	* @return The Account object if found, or null if no matching account is found
	*/
	public Account lookupAccount(Long accountNumber) {
		for (Account account : getAccounts()) {
			if (account.getAccountId() == accountNumber)
				return account;
		}
		return null;
	}
	
	/**
	* Retrieves the credit card number associated with the user's account.
	* 
	* This method iterates through all accounts of the user and returns
	* the account ID of the credit card account if found.
	* 
	* @return The account ID of the credit card account, or -1 if not found
	*/
	public long getCreditCardNumber(){
		for (Account account: getAccounts()){
			if (DBUtil.CREDIT_CARD_ACCOUNT_NAME.equals(account.getAccountName()))
				return account.getAccountId();
		}
		return -1L;
	}
	
	/**
	* Retrieves user transactions within a specified date range for given accounts.
	* 
	* @param startDate The start date of the transaction period (inclusive)
	* @param endDate The end date of the transaction period (inclusive)
	* @param accounts An array of Account objects to fetch transactions for
	* @return An array of Transaction objects matching the specified criteria
	* @throws SQLException If a database access error occurs
	*/
	public Transaction[] getUserTransactions(String startDate, String endDate, Account[] accounts) throws SQLException {
		
		Transaction[] transactions = null;
		transactions = DBUtil.getTransactions(startDate, endDate, accounts, -1);
		return transactions; 
	}
}
