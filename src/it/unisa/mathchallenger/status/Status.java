package it.unisa.mathchallenger.status;

import java.util.ArrayList;

public class Status {
	private static Status status;
	
	private AccountUser utente;
	private ArrayList<Partita> partite;
	private ArrayList<Account> amici;
	
	private long last_update_game;
	
	public static Status getInstance(){
		if(status==null)
			status=new Status();
		return status;
	}
	public Status(){
		partite=new ArrayList<Partita>();
		amici=new ArrayList<Account>();
	}
	
	public void login(AccountUser u){
		utente=u;
	}
	public AccountUser getUtente(){
		return utente;
	}
	public void logout(){
		utente=null;
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
