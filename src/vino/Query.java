package vino;

import java.util.List;

import vino.model.SearchResult;

public class Query {

	private String query;
	private String type;
	private List<SearchResult> results;
	
	public Query(String query, String type) {
		this.query = query;
		this.type = type;
	}
	
	public String getQuery() {
		return query;
	}
	public String getType() {
		return type;
	}
	public List<SearchResult> getResults() {
		return results;
	}
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}
}
