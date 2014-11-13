package com.example.catto10;

public class OffensivePhrase {
	private long id;
	private String phrase;
	private int offensiveness;

	public OffensivePhrase(long l, String s, int i) {
		this.id = l;
		this.phrase = s;
		this.offensiveness = i;
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
	
	//string representation
	public String toString() {
		return "\"" + this.phrase + "\": " + this.offensiveness; 
	}
	
	//string for outputting contents to log
	public String toLog() {
		return this.id + "; \"" + this.phrase + "\"; " + this.offensiveness;
	}

}
