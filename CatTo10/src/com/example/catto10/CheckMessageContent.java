package com.example.catto10;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CheckMessageContent {

	private PhrasesDAO dao;

	public CheckMessageContent(Context c){
		this.dao = new PhrasesDAO(c);
		dao.open();
	}

	public void checkMessageContent(String mMessage){

		// Check if there is any entry in the Dictionary
		if (dao == null || dao.getAllPhrases() == null){  
			Log.v("EEEEE", "after check dao.getallphrases");
			return;
		} else {
			Log.v("CHKMCON", "getting phrases");
			ArrayList <OffensivePhrase> phrases = (ArrayList<OffensivePhrase>) dao.getAllPhrases();
			
			//map of OffensivePhrases to the number of times they appear in msg
			HashMap<OffensivePhrase, Integer> foundPhrasesAndFrequencies 
				= new HashMap<OffensivePhrase, Integer>();
			int freq;
			
			//search for each OffensivePhrase in the db in msg
			for (OffensivePhrase op : phrases) {
				//if the OffensivePhrase occurs at least once, put it and its frequency in foundPhrasesAndFrequencies
				if ((freq = StringUtils.countMatches(mMessage, op.getPhrase())) > 0) {
					MainActivity.mNumAngryWords += freq;
					MainActivity.mAngryWordsFound.add(op);
					MainActivity.offensiveRating += (freq * op.getOffensiveness());
				}
			}

			/*String [] messageParsed = processWord(mMessage);

			// Check words against the db for offensive material
			for (String word: messageParsed){
				for (OffensivePhrase phrase: phrases){
					if (word.equals(phrase.getPhrase())){
						MainActivity.mNumAngryWords++;
						MainActivity.mAngryWordsFound.add(phrase);
						MainActivity.offensiveRating += phrase.getOffensiveness();
					}
				}
			}*/
		}
		//TODO Calculate offensive rating here
		dao.close();
	}

	private static String [] processWord(String x) {
		x = x.replaceAll("[^a-zA-Z ]", " ");
		return x.split(" ");
	}
}
