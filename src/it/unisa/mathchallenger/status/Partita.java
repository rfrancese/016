
package it.unisa.mathchallenger.status;

import it.unisa.mathchallenger.eccezioni.DettagliNonPresentiException;

import java.util.ArrayList;

public class Partita {

	public final static int CREATA = 0, INIZIATA = 1, ABBANDONATA_1 = 2,
			ABBANDONATA_2 = 3, VINCITORE_1 = 4, VINCITORE_2 = 5,
			TEMPO_SCADUTO = 6, PAREGGIATA = 7;
	private Account utente_sfidato;
	private ArrayList<Domanda> domande;
	private int id_partita;
	private int stato_partita;
	private StatoPartita stato;

	public Partita() {
		domande = new ArrayList<Domanda>(6);
	}

	public Account getUtenteSfidato() {
		return utente_sfidato;
	}

	public void setUtenteSfidato(Account a) {
		utente_sfidato = a;
	}

	public void aggiungiDomanda(Domanda d) {
		domande.add(d);
	}

	public Domanda getDomanda(int i) {
		return domande.get(i);
	}

	public int getNumDomande() {
		return domande.size();
	}

	public int getIDPartita() {
		return id_partita;
	}

	public void setIDPartita(int i) {
		id_partita = i;
	}

	public int getStatoPartita() {
		return stato_partita;
	}

	public void setStatoPartita(int s) {
		stato_partita = s;
	}

	public boolean isTerminata() {
		if (getStatoPartita() != CREATA && getStatoPartita() != INIZIATA)
			return true;
		return false;
	}
	public StatoPartita getDettagliPartita(){
		return stato;
	}
	public void setDettagliPartita(StatoPartita p){
		stato=p;
		if(p!=null)
			setStatoPartita(p.getStato());
	}
	public boolean haiVinto() throws DettagliNonPresentiException{
		if(stato!=null){
			switch(stato.getUtente()){
				case 1:
					if(stato_partita==VINCITORE_1)
						return true;
					return false;
				case 2:
					if(stato_partita==VINCITORE_2)
						return true;
					return false;
			}
		}
		throw new DettagliNonPresentiException("Devi richiedere lo stato della partita al server");
	}
	public boolean isPareggiata(){
		return stato_partita==PAREGGIATA;
	}
	public boolean isAbbandonata() throws DettagliNonPresentiException{
		if(stato!=null){
			switch(stato.getUtente()){
				case 1:
					if(stato_partita==ABBANDONATA_1)
						return true;
					return false;
				case 2:
					if(stato_partita==ABBANDONATA_2)
						return true;
					return false;
			}
		}
		throw new DettagliNonPresentiException("Devi richiedere lo stato della partita al server");
	}
}
