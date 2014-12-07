package com.example.catto10;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Toast;

public class CheckMessageContent {

	//TODO Implement the code to check against the database
	// and to launch a dialog window for CatTo10 if offensive
	// May want this defined in new class because EditMessage.class
	// will need to call it again when sending the users edited message
	// to check for offensive content.

	// Jonathan, if you have some different idea for who you wanted to use your classes
	// by all means, please go ahead and do that instead or let me know what you were thinking :)
	public static void checkMessageContent(String mMessage){
		
		//PhrasesDAO temp = new PhrasesDAO(getApplicationContext());

		//TODO This method is triggering an error at the moment
		//temp.addPhrase("poop", 3);
		// TODO fix above to use the following method and get a list
		//	ArrayList <OffensivePhrase> phrases = temp.getAllPhrases();

		//Create our own list of Offensive phrases for the moment
		ArrayList <OffensivePhrase> phrases = new ArrayList<OffensivePhrase>();
		OffensivePhrase one = new OffensivePhrase(1, "poop", 3, "level_flame_three");
		phrases.add(one);
		mMessage = mMessage.replaceAll("\n", "  ");
		String [] messageParsed = mMessage.split(" ");

		// check against the db for offensive material
		for (String word: messageParsed){
			for (OffensivePhrase phrase: phrases){
				
				if (word.equals(phrase.getPhrase())){
					MainActivity.mNumAngryWords++;
					MainActivity.mAngryWordsFound.add(phrase);
					//TODO I don't think this is how we determine the offensive score based on what
					// I read in project mock up
					MainActivity.offensiveRating += phrase.getOffensiveness();
				}
			}
		}
		// offensiveRating /= count of words found?
	}
}
