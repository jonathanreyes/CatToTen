package com.example.catto10;

import android.graphics.Bitmap;

public class WordEntry {
	protected String word;
	protected int level;
	protected Bitmap flameIcon;
	
	public WordEntry(String word, int level, Bitmap flameIcon){
		this.word = word;
		this.level = level;
		this.flameIcon = flameIcon;
	}
	
	public void setWord(String word){
		this.word = word;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void setFlame(Bitmap icon){
		this.flameIcon = icon;
	}
	
	public String getWord(){
		return word;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Bitmap getFlame(){
		return flameIcon;
	}
	
	public boolean equals(WordEntry item){
		return word.equals(item.getWord());
	}
}
