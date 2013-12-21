package vino.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

public class VinoModel extends Model {
	
	public Long getId() {
		return (Long) get("id");
	}
	
	public Date getCreated() {
		return (Date) get("created_at");
	}
	
	public void setCreated(Date created) {
		set("created_at", created);
	}
	
	public Date getUpdated() {
		return (Date) get("updated_at");
	}
	
	public void setUpdated(Date updated) {
		set("updated_at", updated);
	}
	
}
