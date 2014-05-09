package it.unisa.mathchallenger.status;

import it.unisa.mathchallenger.communication.ThreadPing;
import it.unisa.mathchallenger.database.DBAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class Status {
	private static Status status;
	
	private AccountUser utente;
	private ArrayList<Partita> partite;
	private ArrayList<Account> amici;
	private DBAdapter database;
	
	private ThreadPing t_ping;
	
	private long last_update_game;
	
	public static Status getInstance(){
		return status;
	}
	public static Status getInstance(Context c){
		if(status==null){
			status=new Status(c);
		}
		return status;
	}
	public Status(Context c){
		partite=new ArrayList<Partita>();
		amici=new ArrayList<Account>();
		database=new DBAdapter(c);
		database.open();
		AccountUser user=database.selezionaAccount();
		setAccount(user);
		if(user!=null){
			ArrayList<Partita> partite=database.selezionaPartiteInCorso(user.getID());
			for(Partita p: partite)
				aggiungiPartita(p);
			
			ArrayList<Account> amici=database.selezionaAmici(user.getID());
			for(Account a:amici)
				aggiungiAmico(a);
		}
		//database.close();
		t_ping=ThreadPing.getInstance();
	}
	private void setAccount(AccountUser u){
		utente=u;
	}
	public void login(AccountUser u){
		setAccount(u);
		Log.d("", "User is null: "+(u==null));
		Log.d("", "Username: "+u.getUsername());
		//database.open();
		database.inserisciAccount(u.getID(), u.getUsername(), u.getAuthCode());
		//database.close();
		//t_ping.start();
	}
	public void loginAuth(AccountUser u){
		setAccount(u);
		//t_ping.start();
	}
	public AccountUser getUtente(){
		return utente;
	}
	public void logout(){
		utente=null;
		partite.clear();
		amici.clear();
		database.rimuoviAccount();
	}
	public void aggiungiPartita(Partita p){
		for(int i=0;i<partite.size();i++){
			Partita partita=partite.get(i);
			if(partita.getIDPartita()==p.getIDPartita()){
				partita.setStatoPartita(p.getStatoPartita());
				return;
			}
		}
		partite.add(p);
		//database.open();
		database.inserisciPartita(p.getIDPartita(), getUtente().getID(), p.getUtenteSfidato().getID(), p.getUtenteSfidato().getUsername(), p.getStatoPartita());
		//database.close();
	}
	public Partita getPartitaByID(int id){
		for(int i=0;i<partite.size();i++){
			if(partite.get(i).getIDPartita()==id)
				return partite.get(i);
		}
		return null;
	}
	public Partita rimuoviPartita(int id){
		for(int i=0;i<partite.size();i++){
			if(partite.get(i).getIDPartita()==id)
				return partite.remove(i);
		}
		return null;
	}
	public ArrayList<Partita> getElencoPartite() {
		return partite;
	}
	public Account getAmico(int i){
		if(i>=0 && i<amici.size())
			return amici.get(i);
		else
			return null;
	}
	public void aggiungiAmico(Account a){
		for(int i=0;i<amici.size();i++){
			if(amici.get(i).getID()==a.getID())
				return;
		}
		amici.add(a);
		//database.open();
		database.inserisciAmico(getUtente().getID(), a.getID(), a.getUsername());
		//database.close();
	}
	public Account rimuoviAmico(int id){
		for(int i=0;i<amici.size();i++){
			if(amici.get(i).getID()==id)
				return amici.remove(i);
		}
		return null;
	}
	public long getLastUpdateGames(){
		return last_update_game;
	}
	public void setLastUpdateGames(long u){
		last_update_game=u;
	}
	public void setLastUpdateGamesNow(){
		last_update_game=System.currentTimeMillis();
	}
}
