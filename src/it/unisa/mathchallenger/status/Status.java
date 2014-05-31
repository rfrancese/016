package it.unisa.mathchallenger.status;

import it.unisa.mathchallenger.database.DBAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class Status {

	private static Status	  status;

	private AccountUser		utente;
	private ArrayList<Partita> partite;
	private ArrayList<Account> amici;
	private DBAdapter		  database;

	private boolean			friendUpdated = false;
	private boolean 		isValidVersion=true;
	public static int CURRENT_VERSION = 1;

	public static Status getInstance() {
		return status;
	}

	public static Status getInstance(Context c) {
		if (status == null) {
			status = new Status(c);
		}
		return status;
	}

	private Status(Context c) {
		partite = new ArrayList<Partita>();
		amici = new ArrayList<Account>();
		database = new DBAdapter(c);
		database.open();
		try {
			int version=c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
			CURRENT_VERSION=version;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		AccountUser user = database.selezionaAccount();
		setAccount(user);
	}

	private void setAccount(AccountUser u) {
		utente = u;
	}

	public void login(AccountUser u) {
		setAccount(u);
		database.inserisciAccount(u.getID(), u.getUsername(), u.getAuthCode());
		initUser();
	}

	private void initUser() {
		Account user = getUtente();
		if (user != null) {
			ArrayList<Partita> partite = database.selezionaPartiteInCorso(user.getID());
			for (Partita p : partite)
				aggiungiPartita(p);

			ArrayList<Partita> partite_concluse = database.selezionaPartiteTerminate(user.getID());
			for (Partita p : partite_concluse)
				aggiungiPartita(p);

			ArrayList<Account> amici = database.selezionaAmici(user.getID());
			for (Account a : amici)
				aggiungiAmico(a);
		}
	}

	public void loginAuth(AccountUser u) {
		setAccount(u);
		initUser();
	}

	public AccountUser getUtente() {
		return utente;
	}

	public void logout() {
		utente = null;
		partite.clear();
		amici.clear();
		database.rimuoviAccount();
	}

	public void aggiungiPartita(Partita p) {
		for (int i = 0; i < partite.size(); i++) {
			Partita partita = partite.get(i);
			if (partita.getIDPartita() == p.getIDPartita()) {
				partita.setStatoPartita(p.getStatoPartita());
				partita.setInAttesa(p.isInAttesa());
				database.aggiornaPartita(partita.getIDPartita(), getUtente().getID(), partita.getUtenteSfidato().getID(), partita.getUtenteSfidato().getUsername(), partita.getStatoPartita());
				return;
			}
		}
		partite.add(0, p);
		database.inserisciPartita(p.getIDPartita(), getUtente().getID(), p.getUtenteSfidato().getID(), p.getUtenteSfidato().getUsername(), p.getStatoPartita());
	}

	public void aggiornaPartita(Partita p) {
		database.aggiornaPartita(p.getIDPartita(), getUtente().getID(), p.getUtenteSfidato().getID(), p.getUtenteSfidato().getUsername(), p.getStatoPartita());
	}

	public Partita getPartitaByID(int id) {
		for (int i = 0; i < partite.size(); i++) {
			if (partite.get(i).getIDPartita() == id)
				return partite.get(i);
		}
		return null;
	}

	public Partita rimuoviPartita(int id) {
		for (int i = 0; i < partite.size(); i++) {
			if (partite.get(i).getIDPartita() == id) {
				database.rimuoviPartita(id);
				return partite.remove(i);
			}
		}
		return null;
	}

	public ArrayList<Partita> getElencoPartite() {
		return partite;
	}

	public Account getAmico(int i) {
		if (i >= 0 && i < amici.size())
			return amici.get(i);
		else
			return null;
	}

	public ArrayList<Account> getElencoAmici() {
		return amici;
	}

	public void aggiungiAmico(Account a) {
		for (int i = 0; i < amici.size(); i++) {
			if (amici.get(i).getID() == a.getID())
				return;
		}
		amici.add(a);
		database.inserisciAmico(getUtente().getID(), a.getID(), a.getUsername());
	}

	public Account rimuoviAmico(int id) {
		for (int i = 0; i < amici.size(); i++) {
			if (amici.get(i).getID() == id) {
				database.rimuoviAmico(getUtente().getID(), id);
				return amici.remove(i);
			}
		}
		return null;
	}

	public void closeDB() {
		database.close();
	}

	public boolean isFriendUpdated() {
		return friendUpdated;
	}

	public void setFriendUpdated(boolean b) {
		friendUpdated = b;
	}
	public boolean isValidVersion(){
		return isValidVersion;
	}
	public void setValidVersion(boolean b){
		isValidVersion=b;
	}
}
