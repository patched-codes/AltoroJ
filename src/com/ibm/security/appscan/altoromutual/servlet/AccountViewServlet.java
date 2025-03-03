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
package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet allows the users to view account and transaction information.
 * Servlet implementation class AccountServlet
 * @author Alexei
 *
 */
public class AccountViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountViewServlet() {
        super();
    }

    private static final Map<String, String> ALLOWED_PATHS = new HashMap<>();
    static {
        ALLOWED_PATHS.put("balance", "/bank/balance.jsp");
        ALLOWED_PATHS.put("transaction", "/bank/transaction.jsp");
    }

    private boolean isValidAccountId(String accountId) {
        // Implement validation logic (e.g., alphanumeric check, length limits)
        return accountId != null && accountId.matches("^[A-Za-z0-9]+$");
    }

    private boolean isValidDateFormat(String date) {
        // Implement date format validation
        if (date == null) return true; // null is acceptable
        // Add proper date format validation (e.g., yyyy-MM-dd)
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    private String sanitizeParameter(String param) {
        if (param == null) return null;
        // Implement proper parameter encoding/escaping
        return param.replaceAll("[^A-Za-z0-9\\-]", "");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//show account balance for a particular account
		if (request.getRequestURL().toString().endsWith("showAccount")){
			String accountName = request.getParameter("listAccounts");
			if (accountName == null){
				response.sendRedirect(request.getContextPath()+"/bank/main.jsp");
				return;
			}
			
			// Validate accountName parameter
			if (!isValidAccountId(accountName)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account ID");
				return;
			}
			
			String sanitizedAccountName = sanitizeParameter(accountName);
			String dispatchPath = ALLOWED_PATHS.get("balance");
			if (dispatchPath == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath + "?acctId=" + sanitizedAccountName);
			dispatcher.forward(request, response);
			return;
		}
		//this shouldn't happen
		else if (request.getRequestURL().toString().endsWith("showTransactions"))
			doPost(request,response);
		else
			super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//show transactions within the specified date range (if any)
		if (request.getRequestURL().toString().endsWith("showTransactions")){
			String startTime = request.getParameter("startDate");
			String endTime = request.getParameter("endDate");
			
			// Validate date parameters
			if (!isValidDateFormat(startTime) || !isValidDateFormat(endTime)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
				return;
			}
			
			String sanitizedStartTime = sanitizeParameter(startTime);
			String sanitizedEndTime = sanitizeParameter(endTime);
			String dispatchPath = ALLOWED_PATHS.get("transaction");
			if (dispatchPath == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			StringBuilder queryString = new StringBuilder("?");
			if (sanitizedStartTime != null) queryString.append("&startTime=").append(sanitizedStartTime);
			if (sanitizedEndTime != null) queryString.append("&endTime=").append(sanitizedEndTime);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath + queryString.toString());
			dispatcher.forward(request, response);
		}
	}
}
