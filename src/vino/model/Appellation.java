package vino.model;

public class Appellation {

	private long Id;
	private long wineId;
	private String Name;
	private String Url;
	private Region Region;
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public long getWineId() {
		return wineId;
	}
	public void setWineId(long wineId) {
		this.wineId = wineId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public Region getRegion() {
		return Region;
	}
	public void setRegion(Region region) {
		this.Region = region;
	}
	
}
