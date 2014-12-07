package com.example.catto10;

public class OffensivePhrase {
	private long id;
	private String phrase;
	private int offensiveness;
	private String icon;

	public OffensivePhrase(long l, String s, int i, String c) {
		this.id = l;
		this.phrase = s;
		this.offensiveness = i;
		this.icon = c;
	}
	
	//Setters and getters
	public long getId() {
		return this.id;
	}
	
	public void setId(long i) {
		this.id = i;
	}
	
	public String getPhrase() {
		return this.phrase;
	}
	
	public void setPhrase(String p) {
		this.phrase = p;
	}
	
	public int getOffensiveness() {
		return this.offensiveness;
	}
	
	public void setOffensiveness(int o) {
		this.offensiveness = o;
	}
	
	public String getIcon(){
		return this.icon;
	}
	
	public void setIcon(String icon){
		this.icon = icon;
	}

	@Override
	public boolean equals(Object secPhrase){
		if(this == secPhrase) return true;
		if(secPhrase != null && secPhrase instanceof OffensivePhrase){
			return this.phrase.equals(((OffensivePhrase) secPhrase).getPhrase());
		}
		
		return false;
	}
	
	@Override
	public int hashCode(){
		return Long.valueOf(phrase.hashCode() * 31 + offensiveness * 31).hashCode();
	}

	//string representation
	public String toString() {
		return "\"" + this.phrase + "\": " + this.offensiveness + "\": " + this.icon; 
	}
	
	//string for outputting contents to log
	public String toLog() {
		return this.id + "; \"" + this.phrase + "\"; " + this.offensiveness + "\": " + this.icon;
	}

}
