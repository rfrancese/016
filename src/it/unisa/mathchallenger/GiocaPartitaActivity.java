package it.unisa.mathchallenger;

import java.io.IOException;
import java.util.ArrayList;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Domanda;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.Status;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;



public class GiocaPartitaActivity extends Activity {
	private Communication comm; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gioca_partita);
		int id = getIntent().getIntExtra("id_partita", 0);
		if(id>0){
		Messaggio m = CommunicationMessageCreator.getInstance().createGetDomande(id);
		try {
			comm.send(m);
			ArrayList<Domanda> list = CommunicationParser.getInstance().parseGetDomande(m);
			Partita p = Status.getInstance().getPartitaByID(id);
			for(int i=0;i<list.size();i++){
				Domanda dom = list.get(i);
				p.aggiungiDomanda(dom);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gioca_partita, menu);
		return true;
	}
}
