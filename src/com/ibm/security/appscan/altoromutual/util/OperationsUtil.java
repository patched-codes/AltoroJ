package com.ibm.security.appscan.altoromutual.util;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import com.ibm.security.appscan.altoromutual.model.Account;
import com.ibm.security.appscan.altoromutual.model.User;

public class OperationsUtil {

	public static String doApiTransfer(HttpServletRequest request, long creditActId, long debitActId,
			double amount) {
		// ... (rest of the code remains the same)
	}

	public static String doServletTransfer(HttpServletRequest request, long creditActId, String accountIdString,
			double amount) {
		// ... (rest of the code remains the same)
	}

	public static String sendFeedback(String name, String email,
			String subject, String comments) {
		// ... (rest of the code remains the same)
	}

	public static User getUser(HttpServletRequest request) throws SQLException{
		// ... (rest of the code remains the same)
	}

	public static String makeRandomString() {
		SecureRandom random = new SecureRandom();
		byte[] array = new byte[7]; // length is bounded by 7
		random.nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
	 
		return generatedString;
	}
	
}
