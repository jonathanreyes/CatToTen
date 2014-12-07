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
			, CatTo10SQLiteHelper.COLUMN_OFFENSIVENESS
			, CatTo10SQLiteHelper.COLUMN_ICON };

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
	public OffensivePhrase addPhrase(String phrase, int offensiveness, String icon) {
		//construct an entry to add
		ContentValues cvToAdd = new ContentValues();
		Log.v("PhrasesDAO", "adding content values");
		cvToAdd.put(CatTo10SQLiteHelper.COLUMN_PHRASE, phrase);
		cvToAdd.put(CatTo10SQLiteHelper.COLUMN_OFFENSIVENESS, offensiveness);
		cvToAdd.put(CatTo10SQLiteHelper.COLUMN_ICON, icon);
		Log.v("PhrasesDAO", "adding new entry in DB");
		//add the new entry to tblPhrases
		//TODO Getting an error here for insertion
		long insertId = db.insert(CatTo10SQLiteHelper.TABLE_PHRASES, null, cvToAdd);
		
		Log.v("PhrasesDAO", "query the db");
		//query the db to check that entry was done correctly
		Cursor c = db.query(CatTo10SQLiteHelper.TABLE_PHRASES
				, allColumns
				, CatTo10SQLiteHelper.COLUMN_ID + "=" + insertId
				, null, null, null, null);
		Log.v("PhrasesDAO", "return offensive phrase");
		//return the offensive phrase added
		c.moveToFirst();
		OffensivePhrase op = cursorToOffensivePhrase(c);
		c.close();
		return op;
	}
	
	public boolean contains(String phrase){
		Log.i(TAG, "Checking if DB contains " + phrase);
		Cursor c = db.rawQuery("SELECT * FROM " + CatTo10SQLiteHelper.TABLE_PHRASES + " WHERE " + CatTo10SQLiteHelper.COLUMN_PHRASE + "=?", new String[]{phrase});
		return c != null;
	}

	// clears entire table
	public void deleteAll() throws SQLException{
		Log.i(TAG, "Clearing table");
		 db.delete(CatTo10SQLiteHelper.TABLE_PHRASES, null, null);
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

	public void editPhrase(String phrase, int offensiveness, String icon, long id){
		Log.v(TAG, "edit content values");
		ContentValues cvToEdit = new ContentValues();
		
		cvToEdit.put(CatTo10SQLiteHelper.COLUMN_PHRASE, phrase);
		cvToEdit.put(CatTo10SQLiteHelper.COLUMN_OFFENSIVENESS, offensiveness);
		cvToEdit.put(CatTo10SQLiteHelper.COLUMN_ICON, icon);

		Log.v(TAG, "editing entry in DB");
		db.update(CatTo10SQLiteHelper.TABLE_PHRASES, cvToEdit, CatTo10SQLiteHelper.COLUMN_ID + "=" + id, null);
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
		return new OffensivePhrase(c.getLong(0), c.getString(1), c.getInt(2), c.getString(3));
	}

}
