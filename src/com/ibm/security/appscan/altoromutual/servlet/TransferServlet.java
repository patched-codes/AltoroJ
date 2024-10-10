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

import com.ibm.security.appscan.altoromutual.util.OperationsUtil;
import com.ibm.security.appscan.altoromutual.util.ServletUtil;

/**
 * This servlet allows to transfer funds between existing accounts
 * Servlet implementation class TransverServlet
 * 
 * @author Alexei
 */
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Handles HTTP GET requests by delegating to the doPost method.
	 * This method overrides the default behavior of HttpServlet.
	 *
	 * @param req the HttpServletRequest object that contains the request the client made of the servlet
	 * @param resp the HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws ServletException if the request for the GET could not be handled
	 * @throws IOException if an input or output error is detected when the servlet handles the GET request
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	/**
	 * Handles HTTP POST requests for a fund transfer operation.
	 * 
	 * This method processes a fund transfer between accounts. It first checks if the user is logged in,
	 * redirecting to the login page if not. It then retrieves the necessary parameters from the request,
	 * performs the transfer operation, and forwards the result to the transfer.jsp page.
	 * 
	 * @param request The HttpServletRequest object containing the client's request
	 * @param response The HttpServletResponse object for sending the response
	 * @throws ServletException If the request could not be handled
	 * @throws IOException If an input or output error occurs while the servlet is handling the HTTP request
	 */	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if(!ServletUtil.isLoggedin(request)){
			response.sendRedirect("login.jsp");
			return ;
		}
		String accountIdString = request.getParameter("fromAccount");
		long creditActId = Long.parseLong(request.getParameter("toAccount"));
		double amount = Double.valueOf(request.getParameter("transferAmount"));
		
		
		String message = OperationsUtil.doServletTransfer(request,creditActId,accountIdString,amount);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("transfer.jsp");
		request.setAttribute("message", message);
		dispatcher.forward(request, response);	
	}

}
