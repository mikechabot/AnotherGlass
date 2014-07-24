package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

import vino.facebook.FacebookDetails;

public class UserFacebook extends Model {

	private transient User user;
	private transient FacebookDetails details;
	
	public Date getCreated() {
		return getDate("created_at");
	}
	
	public void setCreated(Date created) {
		set("created_at", created);
	}
	
	public Date getUpdated() {
		return getDate("updated_at");
	}
	
	public void setUpdated(Date updated) {
		set("updated_at", updated);
	}
	
	public String getAuthCode() {
		return getString("auth_code");
	}
	
	public void setAuthCode(String authCode) {
		set("auth_code", authCode);
	}
	
	public String getAccessToken() {
		return getString("access_token");
	}
	
	public void setAccessToken(String accessToken) {
		set("access_token", accessToken);
	}
	
	public Long getExpiresIn() {
		return getLong("expires_in");
	}
	
	public void setExpiresIn(Long expiresIn) {
		set("expires_in", expiresIn);
	}
	
	public FacebookDetails getDetails() {
		return details;
	}
	
	public void setDetails(FacebookDetails details) {
		this.details = details;
	}
	
	public User getUser() {
		if (user == null) this.user = parent(User.class);
		return user;
	}
	
	public void setUser(User user) {
		if (user != null) {
			this.user = user;
			set("user_id", user.getId());
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UserFacebook=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("user=").append(get("user_id")).append(",");
		sb.append("authCode=").append(getAuthCode()).append(",");
		sb.append("accessToken=").append(getAccessToken()).append(",");
		sb.append("expiresIn=").append(getExpiresIn()).append(",");
		sb.append("details=").append(getDetails());
		sb.append("]");
		return sb.toString();
	}	
}
