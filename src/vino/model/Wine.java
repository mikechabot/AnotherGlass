package vino.model;

public class Wine extends VinoModel {
	
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

	public String getDescription() {
		return (String) get("description");
	}
	
	public void setDesription(String description) {
		set("description", description);
	}
	
	public String getType() {
		return (String) get("type");
	}
	
	public void setType(String type) {
		set("type", type);
	}

	public String getVintage() {
		return (String) get("vintage");
	}
	
	public void setVintage(String vintage) {
		set("vintage", vintage);
	}
	
	public Region getRegion() {
		return Region.findById(get("region_id"));
	}
	
	public void setRegion(Region region) {
		set("region_id", region.getId());
	}
	
	public Appellation getAppellation() {
		return Appellation.findById(get("appellation_id"));
	}
	
	public void setAppellation(Appellation appellation) {
		set("appellation_id", appellation.getId());
	}
	
	public Vineyard getVineyard() {
		return Vineyard.findById(get("vineyard_id"));
	}
	
	public void setVineyard(Vineyard vineyard) {
		set("vineyard_id", vineyard.getId());
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
		sb.append("vintage=").append(getVintage()).append(",");
		sb.append("region=").append(getRegion()).append(",");
		sb.append("appellation=").append(getAppellation()).append(",");
		sb.append("vineyard=").append(getVineyard());
		sb.append("]");
		return sb.toString();
	}
	
}
