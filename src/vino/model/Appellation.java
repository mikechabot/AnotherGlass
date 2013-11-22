package vino.model;

public class Appellation {

	private long Id;
	private long wineId;
	private String Name;
	private String Url;
	private String Area;
	
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
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	
}
