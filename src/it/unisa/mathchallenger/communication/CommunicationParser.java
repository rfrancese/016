package it.unisa.mathchallenger.communication;

import java.util.ArrayList;

import it.unisa.mathchallenger.status.Account;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Partita;

public class CommunicationParser {
	private static CommunicationParser sing;
	
	public static CommunicationParser getInstance(){
		if(sing==null)
			sing=new CommunicationParser();
		return sing;
	}
	private CommunicationParser() {}
	
	public AccountUser parseLogin(Messaggio res){
		String[] properties=res.getResponse().split(";");
		AccountUser acc=null;
		int id = -1;
		String authcode = null;
		boolean loginOK=false;
		for(int i=0;i<properties.length;i++){
			String[] kv=properties[i].split("=");
			switch(kv[0]){
			case "login":
				if(kv[1].compareTo("OK")==0)
					loginOK=true;
				break;
			case "message":
				res.setErrorMessage(kv[1]);
				break;
			case "id":
				id=Integer.parseInt(kv[1]);
				break;
			case "authcode":
				authcode=kv[1];
				break;
			}
		}
		if(loginOK){
			acc=new AccountUser(id, authcode);
		}
		return acc;
	}
	public AccountUser parseRegister(Messaggio res){
		AccountUser acc=null;
		String[] properties=res.getResponse().split(";");
		int id = -1;
		String authcode = null;
		boolean registerOK=false;
		for(int i=0;i<properties.length;i++){
			String[] kv=properties[i].split("=");
			switch(kv[0]){
			case "register":
				if(kv[1].compareTo("OK")==0)
					registerOK=true;
				break;
			case "message":
				res.setErrorMessage(kv[1]);
				break;
			case "id":
				id=Integer.parseInt(kv[1]);
				break;
			case "authcode":
				authcode=kv[1];
				break;
			}
		}
		if(registerOK){
			acc=new AccountUser(id, authcode);
		}
		return acc;
	}
	public boolean parseResetPassword(Messaggio m){
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
			case "reset-psw":
				if(kv[1].compareTo("OK")==0)
					return true;
				break;
			case "message":
				m.setErrorMessage(kv[1]);
				break;
			}
		}
		return false;
	}
	public boolean parseExit(Messaggio m){
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "exit":
					if(kv[1].compareTo("OK")==0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}
	public boolean parseLoginAuthcode(Messaggio m){
		String prop[]=m.getResponse().split(";");
		boolean loginOK=false;
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "login":
					if(kv[1].compareTo("OK")==0)
						loginOK=true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return loginOK;
	}
	public boolean parseLogout(Messaggio m){
		boolean logoutOK=false;
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "logout":
					if(kv[1].compareTo("OK")==0)
						logoutOK=true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return logoutOK;
	}
	public boolean parseChangePassword(Messaggio m){
		String[] prop=m.getResponse().split(";");
		boolean changeOK=false;
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "change-psw":
					if(kv[1].compareTo("OK")==0)
						changeOK=true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return changeOK;
	}
	public Partita parseNewGame(Messaggio m){
		String[] prop=m.getResponse().split(";");
		Partita partita=null;
		int id = 0;
		boolean partitaOK=false;
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "newgame":
					if(kv[1].compareTo("OK")==0)
						partitaOK=true;
					break;
				case "id":
					id=Integer.parseInt(kv[1]);
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		if(partitaOK){
			partita=new Partita();
			partita.setIDPartita(id);
		}
		return partita;
	}
	public ArrayList<Account> parseSearchUser(Messaggio m){
		ArrayList<Account> trovati=null;
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
			case "trovati":
				int trovati_n=Integer.parseInt(kv[1]);
				if(trovati_n>0)
					trovati=new ArrayList<Account>(trovati_n);
				break;
			case "search-user":
				if(kv[1].compareTo("OK")!=0)
					return null;
				break;
			case "account":
				String[] acc=kv[1].split(",");
				int id=Integer.parseInt(acc[1]);
				Account a=new Account(id);
				a.setUsername(acc[0]);
				trovati.add(a);
				break;
			case "message":
				m.setErrorMessage(kv[1]);
				break;
			}
		}
		return trovati;
	}
	public ArrayList<Partita> parseGetPartiteInCorso(Messaggio m){
		String[] prop=m.getResponse().split(";");
		ArrayList<Partita> partite=null;
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
			case "getPartiteInCorso":
				if(kv[1].compareTo("OK")==0)
					partite=new ArrayList<Partita>();
				break;
			case "partita":
				String[] dati_partita=kv[1].split(",");
				int id_partita=Integer.parseInt(dati_partita[0]);
				int id_sfidante=Integer.parseInt(dati_partita[1]);
				String username_sfidante=dati_partita[2];
				int stato_partita=Integer.parseInt(dati_partita[3]);
				Partita p=new Partita();
				Account sfidante=new Account(id_sfidante);
				sfidante.setUsername(username_sfidante);
				p.setUtenteSfidato(sfidante);
				p.setStatoPartita(stato_partita);
				p.setIDPartita(id_partita);
				partite.add(p);
				break;
			case "message":
				m.setErrorMessage(kv[1]);
				break;
			}
		}
		return partite;
	}
}
