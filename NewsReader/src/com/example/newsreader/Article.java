package com.example.newsreader;

public class Article {
	
	public String title;
	public String url;
	public String description;
	
	public Article(String title, String url, String description) {
		this.title = title;
		this.url = url;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return title + " " + description + "@" + url;
	}
	

}
