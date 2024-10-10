package com.ibm.security.appscan.altoromutual.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
    public AccountViewServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURL().toString().endsWith("showAccount")) {
            String accountName = request.getParameter("listAccounts");
            if (accountName == null) {
                response.sendRedirect(request.getContextPath() + "/bank/main.jsp");
                return;
            }
            
            HashMap<String, String> lookupTable = new HashMap<>();
            // Add valid accountName to resource mappings in the lookup table
            lookupTable.put("validAccountName1", "/bank/balance.jsp?acctId=" + "validAccountName1");
            lookupTable.put("validAccountName2", "/bank/balance.jsp?acctId=" + "validAccountName2");
            // Fallback if accountName is not mapped in lookup Table
            String redirectValue = lookupTable.getOrDefault(accountName, "/bank/main.jsp");

            response.sendRedirect(request.getContextPath() + redirectValue);
            return;
        }
        else if (request.getRequestURL().toString().endsWith("showTransactions")) {
            doPost(request, response);
        } else {
            super.doGet(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURL().toString().endsWith("showTransactions")) {
            String startTime = request.getParameter("startDate");
            String endTime = request.getParameter("endDate");

            HashMap<String, String> transactionsMapping = new HashMap<>();
            // Ensure any necessary mappings for redirects are defined
            transactionsMapping.put("default", "/bank/transaction.jsp");

            StringBuilder queryString = new StringBuilder(transactionsMapping.get("default"));
            if (startTime != null && endTime != null) {
                queryString.append("?startTime=" + startTime + "&endTime=" + endTime);
            }

            response.sendRedirect(request.getContextPath() + queryString.toString());
        }
    }
}