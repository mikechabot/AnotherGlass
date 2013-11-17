package com.anotherglass.vino.model;

import java.util.List;

public class Products{
   	private List<Wine> List;
   	private String Url;
   	private long Offset;
   	private long Total;

 	public List<Wine> getList(){
		return this.List;
	}
 	
	public void setList(List<Wine> list){
		this.List = list;
	}
	
 	public long getOffset(){
		return this.Offset;
	}
 	
	public void setOffset(long offset){
		this.Offset = offset;
	}
	
 	public long getTotal(){
		return this.Total;
	}
 	
	public void setTotal(long total){
		this.Total = total;
	}
	
 	public String getUrl(){
		return this.Url;
	}
 	
	public void setUrl(String url){
		this.Url = url;
	}
}
