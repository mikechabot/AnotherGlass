package vino.model;

public class Appellation extends VinoModel {
		
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
