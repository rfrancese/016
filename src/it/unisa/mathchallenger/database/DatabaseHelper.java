package it.unisa.mathchallenger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final static String DB_NAME="mathchallenger.sqlite";
	private final static int db_version=1;
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DB_NAME, null, db_version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query_account="CREATE TABLE IF NOT EXISTS account ("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"username TEXT NOT NULL,"+
				"authcode TEXT"+
				")";
		db.execSQL(query_account);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
