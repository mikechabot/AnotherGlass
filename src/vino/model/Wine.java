package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class Wine extends Model {

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

	public String getDescription() {
		return getString("description");
	}
	
	public void setDescription(String description) {
		set("description", description);
	}
	
	public String getType() {
		return getString("type");
	}
	
	public void setType(String type) {
		set("type", type);
	}

	public String getVintage() {
		return getString("vintage");
	}
	
	public void setVintage(String vintage) {
		set("vintage", vintage);
	}
	
	public Region getRegion() {
		return parent(Region.class);
	}
	
	public void setRegion(Region region) {
		if (region != null) {
			set("region_id", region.getId());
		}
	}
	
	public Appellation getAppellation() {
		return parent(Appellation.class);
	}
	
	public void setAppellation(Appellation appellation) {
		if (appellation != null) {
			set("appellation_id", appellation.getId());
		}
	}
	
	public Vineyard getVineyard() {
		return parent(Vineyard.class);
	}
	
	public void setVineyard(Vineyard vineyard) {
		if (vineyard != null) {
			set("vineyard_id", vineyard.getId());
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Wine=[");
		sb.append("id=").append(getId()).append(",");
		sb.append("created=").append(getCreated()).append(",");
		sb.append("updated=").append(getUpdated()).append(",");
		sb.append("name=").append(getName()).append(",");
		sb.append("winesComId=").append(getWinesComId()).append(",");
		sb.append("description=").append(getDescription()).append(",");
		sb.append("type=").append(getType()).append(",");
		sb.append("vintage=").append(getVintage());
		sb.append("]");
		return sb.toString();
	}
	
}
