package vino.populator;

public class Region {
	
	private long Id;
	private long regionId;
	private String Name;
	private String Url;
	private String Area;
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public long getRegionId() {
		return regionId;
	}
	public void setRegionId(long regionId) {
		this.regionId = regionId;
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