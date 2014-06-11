package it.unisa.mathchallenger.communication;

import java.util.ArrayList;

import it.unisa.mathchallenger.status.Account;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Classifica;
import it.unisa.mathchallenger.status.Domanda;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.Statistiche;
import it.unisa.mathchallenger.status.StatoPartita;

public class CommunicationParser {

	private static CommunicationParser sing;

	public static CommunicationParser getInstance() {
		if (sing == null)
			sing = new CommunicationParser();
		return sing;
	}

	private CommunicationParser() {}

	public AccountUser parseLogin(Messaggio res) {
		String[] properties = res.getResponse().split(";");
		AccountUser acc = null;
		int id = -1;
		String authcode = null;
		boolean loginOK = false;
		for (int i = 0; i < properties.length; i++) {
			String[] kv = properties[i].split("=");
			switch (kv[0]) {
				case "login":
					if (kv[1].compareTo("OK") == 0)
						loginOK = true;
					break;
				case "message":
					res.setErrorMessage(kv[1]);
					break;
				case "id":
					id = Integer.parseInt(kv[1]);
					break;
				case "authcode":
					authcode = kv[1];
					break;
			}
		}
		if (loginOK) {
			acc = new AccountUser(id, authcode);
		}
		return acc;
	}

	public AccountUser parseRegister(Messaggio res) {
		AccountUser acc = null;
		String[] properties = res.getResponse().split(";");
		int id = -1;
		String authcode = null;
		boolean registerOK = false;
		for (int i = 0; i < properties.length; i++) {
			String[] kv = properties[i].split("=");
			switch (kv[0]) {
				case "register":
					if (kv[1].compareTo("OK") == 0)
						registerOK = true;
					break;
				case "message":
					res.setErrorMessage(kv[1]);
					break;
				case "id":
					id = Integer.parseInt(kv[1]);
					break;
				case "authcode":
					authcode = kv[1];
					break;
			}
		}
		if (registerOK) {
			acc = new AccountUser(id, authcode);
		}
		return acc;
	}

	public boolean parseResetPassword(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "reset-psw":
					if (kv[1].compareTo("OK") == 0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}

	public boolean parseExit(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "exit":
					if (kv[1].compareTo("OK") == 0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}

	public boolean parseLoginAuthcode(Messaggio m) {
		String prop[] = m.getResponse().split(";");
		boolean loginOK = false;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "login":
					if (kv[1].compareTo("OK") == 0)
						loginOK = true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return loginOK;
	}

	public boolean parseLogout(Messaggio m) {
		boolean logoutOK = false;
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "logout":
					if (kv[1].compareTo("OK") == 0)
						logoutOK = true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return logoutOK;
	}

	public boolean parseChangePassword(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		boolean changeOK = false;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "change-psw":
					if (kv[1].compareTo("OK") == 0)
						changeOK = true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return changeOK;
	}

	public Partita parseNewGame(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		Partita partita = null;
		int id = 0;
		boolean partitaOK = false;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "newgame":
					if (kv[1].compareTo("OK") == 0)
						partitaOK = true;
					break;
				case "id":
					id = Integer.parseInt(kv[1]);
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		if (partitaOK) {
			partita = new Partita();
			partita.setIDPartita(id);
		}
		return partita;
	}

	public ArrayList<Account> parseSearchUser(Messaggio m) {
		ArrayList<Account> trovati = null;
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "trovati":
					int trovati_n = Integer.parseInt(kv[1]);
					if (trovati_n > 0)
						trovati = new ArrayList<Account>(trovati_n);
					break;
				case "search-user":
					if (kv[1].compareTo("OK") != 0)
						return null;
					break;
				case "utente":
					String[] acc = kv[1].split(",");
					int id = Integer.parseInt(acc[1]);
					Account a = new Account(id);
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

	public ArrayList<Partita> parseGetPartiteInCorso(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		ArrayList<Partita> partite = null;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "getPartiteInCorso":
					if (kv[1].compareTo("OK") == 0)
						partite = new ArrayList<Partita>();
					break;
				case "partita":
					String[] dati_partita = kv[1].split(",");
					int id_partita = Integer.parseInt(dati_partita[0]);
					Integer id_sfidante = Integer.parseInt(dati_partita[1]);
					String username_sfidante = dati_partita[2];
					int stato_partita = Integer.parseInt(dati_partita[3]);
					boolean inAttesa=Boolean.parseBoolean(dati_partita[4]);
					Partita p = new Partita();
					Account sfidante = new Account(id_sfidante);
					sfidante.setUsername(username_sfidante);
					p.setUtenteSfidato(sfidante);
					p.setStatoPartita(stato_partita);
					p.setIDPartita(id_partita);
					p.setInAttesa(inAttesa);
					partite.add(p);
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return partite;
	}

	public Partita parseNewGameRandom(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		Partita partita = null;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "newgame-random":
					if (kv[1].compareTo("OK") == 0)
						partita = new Partita();
					break;
				case "id":
					partita.setIDPartita(Integer.parseInt(kv[1]));
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return partita;
	}

	public boolean parseAbandon(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "abandon":
					if (kv[1].compareTo("OK") == 0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}

	public boolean parseAggiungiAmico(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "addfriend":
					if (kv[1].compareTo("OK") == 0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}

	public boolean parseRimuoviAmico(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "removefriend":
					if (kv[1].compareTo("OK") == 0)
						return true;
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
			}
		}
		return false;
	}

	public ArrayList<Account> parseGetMyFriends(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		ArrayList<Account> amici = null;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "getMyFriends":
					if (kv[1].compareTo("OK") == 0) {}
					break;
				case "trovati":
					int size = Integer.parseInt(kv[1]);
					if (size > 0)
						amici = new ArrayList<Account>(size);
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
				case "account":
					String[] d = kv[1].split(",");
					Account a = new Account(Integer.parseInt(d[0]));
					a.setUsername(d[1]);
					amici.add(a);
					break;
			}
		}
		return amici;
	}

	public StatoPartita parseGetDettaglioPartita(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		StatoPartita stato = null;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "getDettagliPartita":
					if (kv[1].compareTo("OK") == 0)
						stato = new StatoPartita();
					break;
				case "domande":
					stato.setNumeroDomande(Integer.parseInt(kv[1]));
					break;
				case "utente":
					stato.setUtente(Integer.parseInt(kv[1]));
					break;
				case "hai_risposto":
					stato.setUtenteRisposto(Integer.parseInt(kv[1]) == 1 ? true : false);
					break;
				case "tue_risposte": {
					String[] r = kv[1].split(",");
					int[] tue_r = new int[r.length];
					for (int j = 0; j < tue_r.length; j++) {
						tue_r[j] = Integer.parseInt(r[j]);
					}
					stato.setRisposteUtente(tue_r);
					break;
				}
				case "avversario_risposto":
					stato.setAvversarioRisposto(Integer.parseInt(kv[1]) == 1 ? true : false);
					break;
				case "avversario_risposte": {
					String[] r = kv[1].split(",");
					int[] avv_r = new int[r.length];
					for (int j = 0; j < avv_r.length; j++) {
						avv_r[j] = Integer.parseInt(r[j]);
					}
					stato.setRisposteAvversario(avv_r);
					break;
				}
				case "stato_partita":
					int newStato = Integer.parseInt(kv[1]);
					stato.setStato(newStato);
					break;
			}
		}
		return stato;
	}

	public ArrayList<Domanda> parseGetDomande(Messaggio m) {
		String[] prop = m.getResponse().split(";");
		ArrayList<Domanda> list = null;
		for (int i = 0; i < prop.length; i++) {
			String[] kv = prop[i].split("=");
			switch (kv[0]) {
				case "getDomande":
					if (kv[1].compareTo("OK") == 0)
						list = new ArrayList<Domanda>();
					break;
				case "message":
					m.setErrorMessage(kv[1]);
					break;
				case "domanda1":
					Domanda dom = new Domanda();
					dom.setDomanda(kv[1]);
					list.add(dom);
					break;
				case "domanda2":
					Domanda dom2 = new Domanda();
					dom2.setDomanda(kv[1]);
					list.add(dom2);
					break;
				case "domanda3":
					Domanda dom3 = new Domanda();
					dom3.setDomanda(kv[1]);
					list.add(dom3);
					break;
				case "domanda4":
					Domanda dom4 = new Domanda();
					dom4.setDomanda(kv[1]);
					list.add(dom4);
					break;
				case "domanda5":
					Domanda dom5 = new Domanda();
					dom5.setDomanda(kv[1]);
					list.add(dom5);
					break;
				case "domanda6":
					Domanda dom6 = new Domanda();
					dom6.setDomanda(kv[1]);
					list.add(dom6);
					break;
				case "risposta1": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(0);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
				case "risposta2": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(1);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
				case "risposta3": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(2);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
				case "risposta4": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(3);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
				case "risposta5": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(4);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
				case "risposta6": {
					String[] rs = kv[1].split(",");
					Domanda d = list.get(5);
					for (int j = 0; j < rs.length; j++)
						d.setRisposta(Float.parseFloat(rs[j]), j);
					break;
				}
			}
		}
		return list;
	}

	public boolean parserAnswer(Messaggio mess) {
		if (mess.getResponse().compareTo("answer=OK") == 0)
			return true;
		else
			return false;
	}
	public Statistiche parseGetStatistiche(Messaggio msg){
		Statistiche stat=null;
		String[] prop = msg.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "getStatistiche":
					if(kv[1].compareTo("OK")==0)
						stat=new Statistiche();
					break;
				case "giocate":
					stat.setPartite_giocate(Integer.parseInt(kv[1]));
					break;
				case "vinte":
					stat.setVittorie(Integer.parseInt(kv[1]));
					break;
				case "perse":
					stat.setSconfitte(Integer.parseInt(kv[1]));
					break;
				case "pareggi":
					stat.setPareggi(Integer.parseInt(kv[1]));
					break;
				case "abbandoni":
					stat.setAbbandonate(Integer.parseInt(kv[1]));
					break;
				case "punti":
					stat.setPunti(Integer.parseInt(kv[1]));
					break;
				case "url_classifica":
					Classifica.setURLClassifica(kv[1]);
					break;
			}
		}
		return stat;
	}
	public boolean parseValidateVersion(Messaggio m){
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
				case "validateVersion":
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
}
