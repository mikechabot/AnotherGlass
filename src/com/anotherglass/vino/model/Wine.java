
package com.anotherglass.vino.model;

public class Wine {
	
	private String Description;
   	private String Name;   	
   	private String Retail;
   	private String Type;
   	private String Url;
   	private String Vintage;
   	private long Id;
   	private double PriceMax;
   	private double PriceMin;
   	private double PriceRetail;

  	public String getDescription(){
		return Description;
	}
  	
	public void setDescription(String description){
		this.Description = description;
	}
	
 	public long getId(){
		return Id;
	}
 	
	public void setId(long id){
		this.Id = id;
	}
	
 	public String getName(){
		return Name;
	}
 	
	public void setName(String name){
		this.Name = name;
	}
	
 	public double getPriceMax(){
		return PriceMax;
	}
 	
	public void setPriceMax(long priceMax){
		this.PriceMax = priceMax;
	}
	
 	public double getPriceMin(){
		return PriceMin;
	}
 	
	public void setPriceMin(long priceMin){
		this.PriceMin = priceMin;
	}
	
 	public double getPriceRetail(){
		return PriceRetail;
	}
 	
	public void setPriceRetail(long priceRetail){
		this.PriceRetail = priceRetail;
	}
	
 	public String getRetail(){
		return Retail;
	}
 	
	public void setRetail(String retail){
		this.Retail = retail;
	}
	
 	public String getType(){
		return Type;
	}
 	
	public void setType(String type){
		this.Type = type;
	}
	
 	public String getUrl(){
		return Url;
	}
	public void setUrl(String url){
		this.Url = url;
	}
	
 	public String getVintage(){
		return Vintage;
	}
 	
	public void setVintage(String vintage){
		this.Vintage = vintage;
	}
}
