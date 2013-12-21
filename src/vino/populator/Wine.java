
package vino.populator;

public class Wine {
	
	private String Description;
	private String Name;   	
	private String Retail;
	private String Type;
	private String Url;
	private String Vintage;
	private Appellation Appellation;
	private Vineyard Vineyard;
	private long Id;
	private double PriceMax;
	private double PriceMin;
	private double PriceRetail;
   	
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getRetail() {
		return Retail;
	}
	public void setRetail(String retail) {
		Retail = retail;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getVintage() {
		return Vintage;
	}
	public void setVintage(String vintage) {
		Vintage = vintage;
	}
	public Appellation getAppellation() {
		return Appellation;
	}
	public void setAppellation(Appellation appellation) {
		this.Appellation = appellation;
	}
	public Vineyard getVineyard() {
		return Vineyard;
	}
	public void setVineyard(Vineyard vineyard) {
		this.Vineyard = vineyard;
	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public double getPriceMax() {
		return PriceMax;
	}
	public void setPriceMax(double priceMax) {
		PriceMax = priceMax;
	}
	public double getPriceMin() {
		return PriceMin;
	}
	public void setPriceMin(double priceMin) {
		PriceMin = priceMin;
	}
	public double getPriceRetail() {
		return PriceRetail;
	}
	public void setPriceRetail(double priceRetail) {
		PriceRetail = priceRetail;
	}
  	
}
