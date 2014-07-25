package vino.utils;

import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import vino.model.User;

public class ShiroUtils {

	public static User getLoggedInUser() {
		Subject subject = SecurityUtils.getSubject();
		if (subject == null) return null;
		PrincipalCollection principals = subject.getPrincipals();
		if (principals == null) return null;
		Collection<User> collection = principals.byType(User.class);
		if (collection == null || collection.size() < 1) return null;
		for(User user : collection) {
			return user;
		}
		return null;
	}
	
}
