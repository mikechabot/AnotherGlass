package vino.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import vino.database.Database;
import vino.facebook.FacebookDetails;
import vino.model.User;
import vino.model.UserFacebook;

public class FacebookRealm extends AuthorizingRealm {

	private static final Logger log = Logger.getLogger(FacebookRealm.class);

	public boolean supports(AuthenticationToken authToken) {
		if (authToken instanceof FacebookToken) {
			return true;
		}
		return false;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			 throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}
		
		String username = (String) getAvailablePrincipal(principals);	
		
		Set<String> roles = new HashSet<String>(0);		
		roles.add("user");		
		if (username.equals("admin")) {
			roles.add("admin");
		}
		
		log.debug(username+" has the following roles: "+roles);
		
        return new SimpleAuthorizationInfo(roles);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		FacebookToken facebookToken = (FacebookToken) token;		
		FacebookDetails facebookDetails = facebookToken.getFacebookDetails();
		String username = "!FB"+facebookDetails.getId();
				
		log.info("Request to authorize user ["+username+"]");
		log.info("Credentials Matcher = "+getCredentialsMatcher().getClass());
		
		User user = null;
		Database db = null;
		
		try {
			db = new Database();
			db.open();
			
			user = User.findFirst("username = ?", username);
			
			UserFacebook userFacebook = user.getUserFacebook();
			if (userFacebook == null) {
				userFacebook = new UserFacebook();
			}
			
			userFacebook.setUser(user);
			userFacebook.setAuthCode(facebookToken.getFacebookAuthCode());
			userFacebook.setAccessToken(facebookToken.getFacebookAccessToken());
			userFacebook.setExpiresIn(facebookToken.getFacebookExpiresIn());
			userFacebook.setDetails(facebookDetails);
			userFacebook.save();
			
			user.setUserFacebook(userFacebook);
			user.save();
		}
		catch (Exception e) {
			log.error("There was a SQL error while authorizing user [" + username + "]", e);
			throw new AuthorizationException("There was a SQL error while authorizing user [" + username + "]", e);
		}
		finally {
			if (db != null) {
				db.close();
			}
		}
				
        if (user == null) {
			log.info("No account found for user [" + username + "]");
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }
        
        return new FacebookAuthenticationInfo(user, getName());
	}
	
}