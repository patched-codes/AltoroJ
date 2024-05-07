/**
 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//log in
	// Create session if there isn't one:
	HttpSession session = request.getSession(true);

	String username = null;
	
	try {
		username = request.getParameter("uid");
		if (username != null)
			username = username.trim().toLowerCase();
		
		String password = request.getParameter("passw");
		password = password.trim().toLowerCase(); //in real life the password usually is case sensitive and this cast would not be done

		if (!DBUtil.isValidUser(username, password)){
			Log4AltoroJ.getInstance().logError("Login failed >>> User: " +username + " >>> Password: " + password);
			throw new Exception("Login Failed: We're sorry, but this username or password was not found in our system. Please try again.");
		}
	}

	catch (Exception ex) {
		request.getSession(true).setAttribute("loginError", ex.getLocalizedMessage());
		response.sendRedirect("login.jsp");
		return;
	}

	//Handle the cookie using ServletUtil.establishSession(String)
	try{
		Cookie accountCookie = ServletUtil.establishSession(username, session);
		// Set 'HttpOnly' flag
		accountCookie.setHttpOnly(true);
		// Set 'secure' flag
		accountCookie.setSecure(true);
		response.addCookie(accountCookie);
		response.sendRedirect(request.getContextPath()+"/bank/main.jsp");
	}
	catch (Exception ex){
		ex.printStackTrace();
		response.sendError(500);
	}
	
	return;
}
