package it.unisa.mathchallenger.database;

import java.util.ArrayList;

import it.unisa.mathchallenger.status.Account;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Partita;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter implements Database {

	private Context		context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private boolean		open = false;

	public DBAdapter(Context c) {
		context = c;
	}

	public DBAdapter open() throws SQLException {
		if (!open) {
			dbHelper = new DatabaseHelper(context);
			database = dbHelper.getWritableDatabase();
		}
		open = true;
		return this;
	}

	public void close() {
		dbHelper.close();
		open = false;
	}

	private ContentValues cv_Account(int id, String username, String authcode) {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("username", username);
		values.put("authcode", authcode);
		return values;
	}

	private ContentValues cv_Amico(int account_id, int id_amico, String friend_user) {
		ContentValues values = new ContentValues();
		values.put("account", account_id);
		values.put("id_amico", id_amico);
		values.put("username_amico", friend_user);
		return values;
	}

	private ContentValues cv_Partita(int idPartita, int account, int id_sfidato, String sfidato_user, int stato) {
		ContentValues val = new ContentValues();
		val.put("id_partita", idPartita);
		val.put("account", account);
		val.put("id_sfidante", id_sfidato);
		val.put("username_sfidante", sfidato_user);
		val.put("stato_partita", stato);
		return val;
	}

	public void inserisciAccount(int id, String username, String authcode) throws SQLException {
		deleteAll("account");
		database.insertOrThrow("account", null, cv_Account(id, username, authcode));
	}

	public AccountUser selezionaAccount() {
		Cursor res = database.query("account", null, null, null, null, null, null);
		AccountUser acc = null;
		if (res.moveToNext()) {
			int id = res.getInt(res.getColumnIndex("id"));
			String username = res.getString(res.getColumnIndex("username"));
			String authcode = res.getString(res.getColumnIndex("authcode"));
			acc = new AccountUser(id, authcode);
			acc.setUsername(username);
		}
		res.close();
		return acc;
	}

	public void rimuoviAccount() {
		deleteAll("account");
	}

	public void inserisciAmico(int account_id, int id_amico, String friend_user) {
		database.insertOrThrow("amici", null, cv_Amico(account_id, id_amico, friend_user));
	}

	public void rimuoviAmico(int account, int id_amico) {
		database.delete("amici", "id_amico=" + id_amico + " AND account=" + account, null);
	}

	public ArrayList<Account> selezionaAmici(int account) {
		Cursor res = database.query("amici", null, "account=" + account, null, null, null, "username_amico ASC");
		ArrayList<Account> amici = new ArrayList<Account>();
		while (res.moveToNext()) {
			String user = res.getString(res.getColumnIndex("username_amico"));
			int id_sfidante = res.getInt(res.getColumnIndex("id_amico"));
			Account acc = new Account(id_sfidante);
			acc.setUsername(user);
			amici.add(acc);
		}
		res.close();
		return amici;
	}

	public void inserisciPartita(int idPartita, int account, int id_sfidato, String sfidato_user, int stato) {
		database.insert("partite", null, cv_Partita(idPartita, account, id_sfidato, sfidato_user, stato));
	}

	public void aggiornaPartita(int idPartita, int account, int id_sfidato, String sfidato_user, int newStato) {
		ContentValues cv = cv_Partita(idPartita, account, id_sfidato, sfidato_user, newStato);
		database.update("partite", cv, "id_partita=" + idPartita, null);
	}

	public void rimuoviPartita(int idPartita) {
		database.delete("partite", "id_partita=" + idPartita, null);
	}

	public ArrayList<Partita> selezionaPartiteInCorso(int account) {
		Cursor res = database.query("partite", null, "account=" + account + " AND stato_partita<=" + Partita.INIZIATA, null, null, null, null);
		ArrayList<Partita> p = new ArrayList<Partita>();
		while (res.moveToNext()) {
			int id_partita = res.getInt(res.getColumnIndex("id_partita"));
			int stato_part = res.getInt(res.getColumnIndex("stato_partita"));
			int id_sfidante = res.getInt(res.getColumnIndex("id_sfidante"));
			String nome_sfidante = res.getString(res.getColumnIndex("username_sfidante"));
			Account sfidante = new Account(id_sfidante);
			sfidante.setUsername(nome_sfidante);
			Partita partita = new Partita();
			partita.setUtenteSfidato(sfidante);
			partita.setIDPartita(id_partita);
			partita.setStatoPartita(stato_part);
			p.add(partita);
		}
		res.close();
		return p;
	}

	public ArrayList<Partita> selezionaPartiteTerminate(int account) {
		Cursor res = database.query("partite", null, "account=" + account + " AND stato_partita>" + Partita.INIZIATA, null, null, null, null);
		ArrayList<Partita> p = new ArrayList<Partita>();
		while (res.moveToNext()) {
			int id_partita = res.getInt(res.getColumnIndex("id_partita"));
			int stato_part = res.getInt(res.getColumnIndex("stato_partita"));
			int id_sfidante = res.getInt(res.getColumnIndex("id_sfidante"));
			String nome_sfidante = res.getString(res.getColumnIndex("username_sfidante"));
			Account sfidante = new Account(id_sfidante);
			sfidante.setUsername(nome_sfidante);
			Partita partita = new Partita();
			partita.setUtenteSfidato(sfidante);
			partita.setIDPartita(id_partita);
			partita.setStatoPartita(stato_part);
			p.add(partita);
		}
		res.close();
		return p;
	}

	public void executeUpdate(String query) {
		database.execSQL(query);
	}

	private void deleteAll(String table) {
		database.delete(table, null, null);
	}
}
