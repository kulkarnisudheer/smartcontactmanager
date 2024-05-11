package com.smartcontactproject.service;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
@Service
public class Emailsevice {
	
	public boolean  sendemail(String subject,String message,String to) {
		
		boolean f = false;
		
		String from = "swpksk21@gmail.com";
		
		//vaiable for gmailhost
		
		String host = "smtp.gmail.com";
		
		
		//get the system properties
		
		Properties properties = System.getProperties();
		System.out.println(properties);
		
		//setting important information to properties object
		
		//host set
		 
		properties.put("mail.smtp.host",host );
		properties.put("mail.smtp.port","465" );
		properties.put("mail.smtp.ssl.enable","true" );
		properties.put("mail.smtp.auth","true" );
		

		//to get session object
	Session session = Session.getInstance(properties,new javax.mail.Authenticator() {

		@Override
		protected PasswordAuthentication getPasswordAuthentication() { 
			
			return new PasswordAuthentication( "swpksk21@gmail.com", "fmzh wofp kxgi jzww");
		}
		
		
	});
		
	session.setDebug(true);
	
//	compose the message (text,multi media)
		
	 MimeMessage mimeMessage = new MimeMessage(session);
	
	 
	 try {
		//from email 
	 mimeMessage.setFrom(from);
	 
	 
		//adding recipient to message
	 
	 mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		 
	 
	 // adding subject to message
	 
	 mimeMessage.setSubject(subject);
	 
	 // adding text to
//	 mimeMessage.setText(message);
	 mimeMessage.setContent(message, "text/html");
	 
	 //send 
//	 send the message using Transport class
	 
	 Transport.send(mimeMessage);
	 
	 System.out.println("sent success");
	 f = true;
	 }
	 catch (Exception e) {
	e.printStackTrace(); 
		 
	}
	 return f;
	 
	}

}
