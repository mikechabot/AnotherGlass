package vino.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class FacebookCredentialsMatcher implements CredentialsMatcher {
	
	private static final Logger log = Logger.getLogger(FacebookCredentialsMatcher.class);
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authToken, AuthenticationInfo authInfo) {		
		if(authToken instanceof FacebookToken && authInfo instanceof FacebookAuthenticationInfo){
			FacebookToken facebookToken = (FacebookToken) authToken;
			FacebookAuthenticationInfo facebookAuthInfo = (FacebookAuthenticationInfo) authInfo;
			if (facebookToken.getPrincipal().equals(facebookAuthInfo.getPrincipals().getPrimaryPrincipal())) {
				log.info("login success via facebook auth for ["+facebookToken.getPrincipal()+"]");
				return true;
			}
		}
		
		log.info("facebook based login failure");
		return false;		
	}
	
}
