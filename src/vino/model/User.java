package vino.model;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.javalite.activejdbc.Model;
import org.joda.time.DateTime;

import vino.utils.StringUtils;

public class User extends Model {

	public Date getCreated() {
		return (Date) get("created_at");
	}
	
	public void setCreated(Date created) {
		set("created_at", created);
	}
	
	public Date getUpdated() {
		return (Date) get("updated_at");
	}
	
	public void setUpdated(Date updated) {
		set("updated_at", updated);
	}
	
	public boolean isActive() {
		return ((Boolean) get("active")).booleanValue();
	}
	
	public void setActive(boolean active) {
		set("active", active);
	}
	
	public String getUsername() {
		return (String) get("username");
	}
	
	public void setUsername(String username) {
		set("username", username);
	}
	
	public String getEmail() {
		return (String) get("email");
	}
	
	public void setEmail(String email) {
		set("email", email);
	}
	
	public String getPassword() {
		return "******";
	}
	
    public void setPassword(String password) {
    	String salt = StringUtils.generatePseudoRandomToken();
    	set("password_salt", salt);
    	set("password_hash", DigestUtils.sha512Hex(password + salt));
     }	
    
    private String getPasswordSalt() {
    	return (String) get("password_salt");
    }

    private String getPasswordHash() {
    	return (String) get("password_hash");
    }

	public boolean authenticate(String password) {
        return (getPasswordHash().equals(DigestUtils.sha512Hex(password + getPasswordSalt())));
    }
	
	public Date getResetExpiration() {
		return (Date) get("reset_expiration");
	}
	
	public String getResetToken() {
		return (String) get("reset_token");
	}
	
	public void reset() {
        set("reset_expiration", new DateTime().plusDays(1).toDate());
		set("reset_token", StringUtils.generatePseudoRandomToken());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("active=").append(isActive()).append(",");
		sb.append("username=").append(getUsername()).append(",");
		sb.append("email=").append(getEmail()).append(",");
		sb.append("password=").append(getPassword()).append(",");
		sb.append("resetExpriation=").append(getResetExpiration()).append(",");
		sb.append("resetToken=").append(getResetToken());
		sb.append("]");
		return sb.toString();
	}
	
}
