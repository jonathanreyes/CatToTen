package com.example.catto10;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PhrasesDAO {
	public static final String TAG = "PhrasesDAO";
	
	private SQLiteDatabase db;
	private CatTo10SQLiteHelper dbHelper;
	private String[] allColumns = { CatTo10SQLiteHelper.COLUMN_ID
			, CatTo10SQLiteHelper.COLUMN_PHRASE
			, CatTo10SQLiteHelper.COLUMN_OFFENSIVENESS};

	public PhrasesDAO(Context c) {
		dbHelper = new CatTo10SQLiteHelper(c);
	}
	
	//open a connection to the database
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}
	
	//close the connection to the database
	public void close() {
		dbHelper.close();
	}
	
	//add an offensive phrase to tblPhrses in offensive_phrases.db
	public OffensivePhrase addPhrase(String phrase, int offensiveness) {
		//construct an entry to add
		ContentValues cvToAdd = new ContentValues();
		cvToAdd.put(CatTo10SQLiteHelper.COLUMN_PHRASE, phrase);
		cvToAdd.put(CatTo10SQLiteHelper.COLUMN_OFFENSIVENESS, offensiveness);
		
		//add the new entry to tblPhrases
		long insertId = db.insert(CatTo10SQLiteHelper.TABLE_PHRASES, null, cvToAdd);
		
		//query the db to check that entry was done correctly
		Cursor c = db.query(CatTo10SQLiteHelper.TABLE_PHRASES
				, allColumns
				, CatTo10SQLiteHelper.COLUMN_ID + "=" + insertId
				, null, null, null, null);
		
		//return the offensive phrase added
		c.moveToFirst();
		OffensivePhrase op = cursorToOffensivePhrase(c);
		c.close();
		return op;
	}
	
	public void deletePhrase(OffensivePhrase op) {
		//get Id out of op
		long deleteId = op.getId();
		Log.i(TAG, "Deleting phrase with ID " + deleteId);
		
		//remove op from tblPhrases
		db.delete(CatTo10SQLiteHelper.TABLE_PHRASES
				, CatTo10SQLiteHelper.COLUMN_ID + " = " + deleteId
		        , null);
	}
	
	public List<OffensivePhrase> getAllPhrases() {
		//create return object
		List<OffensivePhrase> allPhrases = new ArrayList<OffensivePhrase>();
		
		//select all from tblPhrases
		Cursor c = db.query(CatTo10SQLiteHelper.TABLE_PHRASES
				,allColumns
				, null, null, null, null, null);
		
		//iterate over all query results
		c.moveToFirst();
		while(!c.isAfterLast()) {
			//turn each result into an OffensivePhrase
			OffensivePhrase op = cursorToOffensivePhrase(c);
			
			//add the OffensivePhrase to allPhrases and move to the next result
			allPhrases.add(op);
			c.moveToNext();
		}
		
		//close the cursor
		c.close();
		return allPhrases;
	}
	
	private OffensivePhrase cursorToOffensivePhrase(Cursor c) {
		return new OffensivePhrase(c.getLong(0), c.getString(1), c.getInt(2));
	}

}
