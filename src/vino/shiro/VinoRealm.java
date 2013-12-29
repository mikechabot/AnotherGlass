package vino.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import vino.database.Database;
import vino.model.User;

public class VinoRealm extends AuthorizingRealm {

	private static final Logger log = Logger.getLogger(VinoRealm.class);
		
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
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		
		if (username == null) {
			log.debug("Null usernames are not allowed by this realm.");
			throw new AccountException("Null usernames are not allowed by this realm.");
		}
		
		log.debug("Request to authorize user ["+username+"]");
		log.debug("Credentials Matcher = "+getCredentialsMatcher().getClass());
		
		if("admin".equalsIgnoreCase(username)) {
			return new SimpleAuthenticationInfo(username, "admin", getName());
		}

		User user = null;
		Database db = null;
		
		try {
			db = new Database();
			db.open();
			
			user = User.findFirst("username = ?", username);		
		}
		catch (Exception e) {
			log.debug("There was a SQL error while authorizing user [" + username + "]", e);
			throw new AuthorizationException("There was a SQL error while authorizing user [" + username + "]", e);
		}
		finally {
			if (db != null) {
				db.close();
			}
		}
				
        if (user == null) {
			log.debug("No account found for user [" + username + "]");
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }
        
        return new SimpleAuthenticationInfo(username, user, getName());
	}
	
}
