package vino.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import vino.Configuration;
import vino.model.User;

public class VinoCredentialsMatcher implements CredentialsMatcher {
	
	private static final Logger log = Logger.getLogger(VinoCredentialsMatcher.class);
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authToken, AuthenticationInfo authInfo) {			
		String username = (String) authToken.getPrincipal();
		String password = new String((char[]) authToken.getCredentials());
		
		boolean match = false;
		
		if (username.startsWith("!")) {
			match = false;
		}
		else if (username.equals("admin")) {
			match = password.equals(Configuration.getInstance().getAdminPassword());
		}
		else {
			User user = (User) authInfo.getCredentials();
			match = user.authenticate(password);
		}
		
		log.debug("credentials verification for ["+username+"] was "+match);
		
		return match;
	}
	
}
