package vino.model;

public class Vineyard implements SearchResult{

	public long Id;
	public String Name;
	public String Url;
	public String ImageUrl;
	public Appellation Appellation;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public Appellation getAppellation() {
		return Appellation;
	}
	public void setAppellation(Appellation appellation) {
		Appellation = appellation;
	}
	
}
