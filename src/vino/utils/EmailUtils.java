package vino.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import vino.Configuration;
import vino.Configuration.EmailServerConfiguration;

public class EmailUtils {

	private static final Logger log = Logger.getLogger(EmailUtils.class);
	
	public static void sendSimpleMessage(String to, String subject, String body) {
        Configuration configuration = Configuration.getInstance();
        
		EmailServerConfiguration emailServer = configuration.getEmailServer();
		if (emailServer == null) {
			throw new IllegalArgumentException("There is no email server configured!");
		}
        
		log.debug("Sending a Simple Email to "+to+", re: "+subject);
		
		try {
			Email email = new SimpleEmail();
	        email.setHostName(emailServer.getAddress());
	        email.setSmtpPort(emailServer.getPort());
	        
	        if (emailServer.getUsername() != null) {
	        	email.setAuthenticator(new DefaultAuthenticator(emailServer.getUsername(), emailServer.getPassword()));
	        }
	                
	        email.setSSLOnConnect(emailServer.isSsl());
	        email.setFrom(configuration.getAdminEmail());
	        email.setSubject(subject);
	        email.setMsg(body);
	        email.addTo(to);
	        
	        email.send();
		}
		catch (EmailException e) {
			log.error("Unable to send email", e);			
		}
	}
	
}
