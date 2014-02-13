package vino.model;

import java.util.Date;
import java.util.List;

import org.javalite.activejdbc.Model;

public class Appellation extends Model {

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
	
	public Region getRegion() {
		return parent(Region.class);
	}
	
	public void setRegion(Region region) {
		if (region != null) {
			set("region_id", region.getId());
		}
	}
	
	public long getWineCount() {
		return Wine.count("appellation_id = ?", getId());
	}
	
	public List<Wine> getWines() {
		 return getAll(Wine.class);
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
