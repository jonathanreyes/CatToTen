package com.example.catto10;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class CatTo10SQLiteHelper extends SQLiteOpenHelper {
	public static final String TAG = "CatTo10SQLiteHelper";
	
	//table words and its column names
	public static final String TABLE_PHRASES = "phrases";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PHRASE = "phrase";
	public static final String COLUMN_OFFENSIVENESS = "offensiveness";

	//database name and version
	private static final String DATABASE_NAME = "offensive_phrases.db";
	private static final int DATABASE_VERSION = 1;
	
	//string to create words table
	private static final String DATABASE_CREATE = "create table " + TABLE_PHRASES 
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PHRASE + " text not null, "
			+ COLUMN_OFFENSIVENESS + " integer not null);";

	public CatTo10SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHRASES);
		onCreate(db);
	}

}
