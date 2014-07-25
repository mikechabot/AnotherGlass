package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class Activity extends Model {

	private transient User user;
	private transient Wine wine;
	private transient Vineyard vineyard;
	
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

	public void setType(ActivityType type) {
		if (type != null) {
			set("type", type.getMnemonic());
		}
	}
		
	public ActivityType getType() {
		String str = getString("type");
		if (str != null) {
			ActivityType type = ActivityType.lookup(str);
			if (type != null) {
				return type;
			}
		}
		return null;
	}
	
	public boolean isActive() {
		return getBoolean("active");
	}
	
	public void setActive(boolean active) {
		set("active", active);
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
	
	public Wine getWine() {
		if (wine == null) this.wine = parent(Wine.class);
		return wine;
	}
	
	public void setWine(Wine wine) {		
		if (wine != null) {
			this.wine = wine;
			set("wine_id", wine.getId());
		}
	}
	
	public Vineyard getVineyard() {
		if (vineyard == null) this.vineyard = parent(Vineyard.class);
		return vineyard;
	}
	
	public void setVineyard(Vineyard vineyard) {		
		if (vineyard != null) {
			this.vineyard = vineyard;
			set("vineyard_id", vineyard.getId());
		}
	}		
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Activity=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("active=").append(isActive()).append(",");
		sb.append("type=").append(getType()).append(",");	
		sb.append("user=").append(get("user_id")).append(",");
		sb.append("wine=").append(get("wine_id")).append(",");
		sb.append("vineyard=").append(get("vineyard_id"));		
		sb.append("]");
		return sb.toString();
	}	
}
