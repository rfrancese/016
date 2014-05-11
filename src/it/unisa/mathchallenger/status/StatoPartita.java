package it.unisa.mathchallenger.status;


public class StatoPartita {
	private boolean haiRisposto, avversario_risposto;
	private int n_domande, utente, nuovo_stato;
	private int[] tue_risposte, avversario_risposte;
	
	public StatoPartita(){}
	public void setNumeroDomande(int n){
		n_domande=n;
	}
	public void setRisposteAvversario(int[] risposte){
		avversario_risposte=risposte;
	}
	public int getStato(){
		return nuovo_stato;
	}
	public void setStato(int s){
		nuovo_stato=s;
	}
	public void setRisposteUtente(int[] risposte){
		tue_risposte=risposte;
	}
	public int getNumeroDomande(){
		return n_domande;
	}
	public int[] getRisposteUtente(){
		return tue_risposte;
	}
	public int[] getRisposteAvversario(){
		return avversario_risposte;
	}
	public boolean isUtenteRisposto(){
		return haiRisposto;
	}
	public void setUtenteRisposto(boolean b){
		haiRisposto=b;
	}
	public boolean isAvversarioRisposto(){
		return avversario_risposto;
	}
	public void setAvversarioRisposto(boolean b){
		avversario_risposto=b;
	}
	public int getUtente(){
		return utente;
	}
	public void setUtente(int i){
		utente=i;
	}
}
