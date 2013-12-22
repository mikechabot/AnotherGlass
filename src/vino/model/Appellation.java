package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class Appellation extends Model {

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

	public String getName() {
		return (String) get("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public Long getWinesComId() {
		return (Long) get("wines_com_id");
	}
	
	public void setWinesComId(long winesComId) {
		set("wines_com_id", winesComId);
	}
	
	public Region getRegion() {
		return Region.findById(get("region_id"));
	}
	
	public void setRegion(Region region) {
		set("region_id", region.getId());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Appelation=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("name=").append(getName()).append(",");
		sb.append("winesComId=").append(getWinesComId()).append(",");
		sb.append("region=").append(getRegion());
		sb.append("]");
		return sb.toString();
	}
	
}
