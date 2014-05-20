package it.unisa.mathchallenger.status;

import android.util.Log;

public class Domanda {
	public final static int NON_RISPOSTO=-1, ESATTA=0, SBAGLIATA=1;
	private int	num_domanda;
	private String domanda;
	private float  risposta1, risposta2, risposta3, risposta4, risposta_utente;

	public Domanda() {}

	public float getRisposta(int i) {
		switch (i) {
			case 0:
				return risposta1;
			case 1:
				return risposta2;
			case 2:
				return risposta3;
			case 3:
				return risposta4;
			default:
				throw new RuntimeException("Numero risposta non valida: " + i);
		}
	}

	public String getDomanda() {
		return domanda;
	}

	public int getNumeroDomanda() {
		return num_domanda;
	}

	public void setDomanda(String d) {
		domanda = d;
	}

	public void setRisposta(float r, int i) {
		switch (i) {
			case 0:
				risposta1 = r;
				break;
			case 1:
				risposta2 = r;
				break;
			case 2:
				risposta3 = r;
				break;
			case 3:
				risposta4 = r;
				break;
			default:
				throw new RuntimeException("Numero risposta non valida: " + i);
		}
		
	}

	public void setNumeroDomanda(int i) {
		num_domanda = i;
	}
	public void setRispostaUtente(float f){
		Log.d("setRisposta", "Risposta " + f + " alla domanda "+ domanda);
		risposta_utente=f;
	}
	public float getRispostaUtente(){
		return risposta_utente;
	}
}
