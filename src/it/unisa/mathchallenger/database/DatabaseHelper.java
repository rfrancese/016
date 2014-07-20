package it.unisa.mathchallenger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DB_NAME	= "mathchallenger.sqlite";
	private final static int	db_version = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, db_version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query_account = "CREATE TABLE IF NOT EXISTS account (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "username TEXT NOT NULL," + "authcode TEXT" + ")";
		db.execSQL(query_account);

		String query_amici = "CREATE TABLE IF NOT EXISTS amici (" + "account INTEGER," + "username_amico TEXT NOT NULL," + "id_amico INTEGER" + ")";
		db.execSQL(query_amici);

		String query_partite = "CREATE TABLE IF NOT EXISTS partite (" + "id_partita INTEGER NOT NULL," + "account INTEGER NOT NULL," + "id_sfidante INTEGER NOT NULL," + "username_sfidante TEXT NOT NULL," + "stato_partita INTEGER" + ")";
		db.execSQL(query_partite);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
