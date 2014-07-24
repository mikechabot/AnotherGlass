package vino.shiro;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import vino.model.User;

public class FacebookAuthenticationInfo implements AuthenticationInfo {
	 
	private static final long serialVersionUID = 1L;
	
	private PrincipalCollection principalCollection;
 
	public FacebookAuthenticationInfo(User user, String realmName) {
		Collection<Object> principals = new ArrayList<Object>();
		principals.add(user.getUsername());
		principals.add(user);
		this.principalCollection = new SimplePrincipalCollection(principals, realmName);
	}
 
	@Override
	public PrincipalCollection getPrincipals() {
		return principalCollection;
	}
 
	@Override
	public Object getCredentials() {
		return null;
	}
		
}