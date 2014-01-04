package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class Vineyard extends Model {
	
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
	
	public String getName() {
		return getString("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public Long getWinesComId() {
		return getLong("wines_com_id");
	}
	
	public void setWinesComId(long winesComId) {
		set("wines_com_id", winesComId);
	}
		
	public String getImageUrl() {
		return getString("image_url");
	}
	
	public void setImageUrl(String image_url) {
		set("image_url", image_url);
	}

	public Appellation getAppellation() {
		return parent(Appellation.class);
	}
	
	public void setAppellation(Appellation appellation) {
		if (appellation != null) {
			set("appellation_id", appellation.getId());
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vineyard=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("name=").append(getName()).append(",");
		sb.append("winesComId=").append(getWinesComId()).append(",");
		sb.append("image_url=").append(getImageUrl()).append(",");
		sb.append("appellation=").append(getAppellation());
		sb.append("]");
		return sb.toString();
	}
	
}
