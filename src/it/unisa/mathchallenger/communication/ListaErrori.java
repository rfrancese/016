package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.R;

public class ListaErrori {
	public final static int OK = 0, DEVI_ESSERE_LOGGATO = 1, SEI_LOGGATO = 2,
			INVALID_AUTHCODE = 3, VECCHIA_PASSWORD_ERRATA = 4,
			USERNAME_IN_USO = 5, NON_PUOI_SFIDARE_QUESTO_UTENTE = 6,
			NON_E_UNA_TUA_PARTITA = 7, PARTITA_NON_TROVATA = 8,
			RIPROVA_PIU_TARDI = 9,
			NON_PUOI_AGGIUNGERE_QUESTO_ACCOUNT_AGLI_AMICI = 10,
			VERSIONE_NON_VALIDA = 11;
	
	public static int getMessage(String m){
		try {
			int m_id=Integer.parseInt(m);
			switch(m_id){
				case 0:
					return R.string.errore_0;
				case 1:
					return R.string.errore_1;
				case 2:
					return R.string.errore_2;
				case 3:
					return R.string.errore_3;
				case 4:
					return R.string.errore_4;
				case 5:
					return R.string.errore_5;
				case 6:
					return R.string.errore_6;
				case 7:
					return R.string.errore_7;
				case 8:
					return R.string.errore_8;
				case 9:
					return R.string.errore_9;
				case 10:
					return R.string.errore_10;
				case 11:
					return R.string.errore_11;
				default:
			}
		}
		catch(NumberFormatException e){
			
		}
		return -1;
	}
}
