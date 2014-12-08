package com.example.catto10;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

public class SMSInterceptor {
	public static final String TAG = "SMSInterceptor";
	//if overall anger score is greater than this, go to cat pics
	public static final double ANGER_THRESHOLD = 3.5; 
	
	private PhrasesDAO dao;
	
	public SMSInterceptor(Context c) {
		this.dao = new PhrasesDAO(c);
	}
	
	private HashMap<OffensivePhrase, Integer> findOffensivePhrases(String msg) {
		//start with all phrases in the db
		ArrayList<OffensivePhrase> allPhrases = (ArrayList<OffensivePhrase>) dao.getAllPhrases();
		
		//map of OffensivePhrases to the number of times they appear in msg
		HashMap<OffensivePhrase, Integer> foundPhrasesAndFrequencies 
			= new HashMap<OffensivePhrase, Integer>();
		int freq;
		
		//search for each OffensivePhrase in the db in msg
		for (OffensivePhrase op : allPhrases) {
			//if the OffensivePhrase occurs at least once, put it and its frequency in foundPhrasesAndFrequencies
			if ((freq = StringUtils.countMatches(msg, op.getPhrase())) > 0) {
				foundPhrasesAndFrequencies.put(op, freq);
			}
		}
		
		return foundPhrasesAndFrequencies;
	}
	
	private double calculateAverageAngerScore(String msg) {
		//search the message for offensive phrases in the db
		HashMap<OffensivePhrase, Integer> foundPhrasesAndFrequencies = findOffensivePhrases(msg);
		double numOffensivePhrases = 0, cumulativeAngerScore = 0, currFreq;
		
		for (OffensivePhrase op : foundPhrasesAndFrequencies.keySet()) {
			currFreq = foundPhrasesAndFrequencies.get(op);
			numOffensivePhrases += currFreq;
	//		cumulativeAngerScore += op.getAngerScore() * currFreq;
		}
		
		return (cumulativeAngerScore / numOffensivePhrases);
	}

	public boolean isMessageHurtful(String msg) {
		return calculateAverageAngerScore(msg) > ANGER_THRESHOLD;
	}
}
