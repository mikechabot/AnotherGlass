package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class UserAvatar extends Model {

	private transient User user;
	
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

	public String getContentType() {
		return getString("content_type");
	}
	
	public void setContentType(String content_type) {
		set("content_type", content_type);
	}

	public byte[] getData() {
		return getBytes("data");
	}
	
	public void setData(byte[] data) {
		set("data", data);
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
		sb.append("UserAvatar=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("user=").append(get("user_id")).append(",");
		sb.append("contentType=").append(getContentType()).append(",");
		sb.append("data=").append("...");
		sb.append("]");
		return sb.toString();
	}	
}
