package com.ibm.security.appscan.altoromutual.model;

/*
 * Submitted feedback bean
 */
public class Feedback {
	
	public static final long FEEDBACK_ALL = -999;
	
	private long feedbackID = -1;
	private String name = null;
	private String email = null;
	private String subject = null;
	private String message = null;

	/*
	 * Initializes feedback bean with all of the feedback attributes
	 */
	public Feedback(long feedbackId, String name, String email, String subject, String message){
		this.feedbackID = feedbackId;
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.message = message;
	}
	
	/**
	* Gets the feedback ID.
	* 
	* @return The feedback ID as a long value.
	*/
	public long getFeedbackID() {
		return feedbackID;
	}

	/**
	* Returns the name of the object.
	* 
	* @return The name of the object as a String.
	*/
	public String getName() {
		return name;
	}

	/**
	* Returns the message stored in this object.
	*
	* @return The message as a String
	*/
	public String getMessage() {
		return message;
	}

	/**
	* Gets the subject of the email or message.
	* 
	* @return The subject string of the email or message
	*/
	public String getSubject() {
		return subject;
	}

	/**
	* Retrieves the email address associated with this object.
	* 
	* @return The email address as a String
	*/
	public String getEmail() {
		return email;
	}

}
