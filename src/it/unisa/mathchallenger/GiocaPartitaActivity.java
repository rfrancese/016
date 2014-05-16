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
import android.widget.Button;
import android.widget.ImageButton;

public class GiocaPartitaActivity extends Activity {
	private Communication comm;
	
	private Partita partita;
	private int domanda_corrente=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gioca_partita);
		comm=Communication.getInstance();
		int id = getIntent().getIntExtra("id_partita", 0);
		if (id > 0) {
			Messaggio m = CommunicationMessageCreator.getInstance().createGetDomande(id);
			try {
				comm.send(m);
				ArrayList<Domanda> list = CommunicationParser.getInstance().parseGetDomande(m);
				partita = Status.getInstance().getPartitaByID(id);
				for (int i = 0; i < list.size(); i++) {
					Domanda dom = list.get(i);
					partita.aggiungiDomanda(dom);
				}
				scriviDomanda();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ConnectionException e) {
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
	
	private void scriviDomanda(){
		Domanda d=partita.getDomanda(domanda_corrente);
		Button domanda=(Button) findViewById(R.id.gioca_partita_domanda);
		Button risp1=(Button) findViewById(R.id.gioca_partita_risp1);
		Button risp2=(Button) findViewById(R.id.gioca_partita_risp2);
		Button risp3=(Button) findViewById(R.id.gioca_partita_risp3);
		Button risp4=(Button) findViewById(R.id.gioca_partita_risp4);
		domanda.setText(d.getDomanda());
		risp1.setText(d.getRisposta(0)+"");
		risp2.setText(d.getRisposta(1)+"");
		risp3.setText(d.getRisposta(2)+"");
		risp4.setText(d.getRisposta(3)+"");
	}
}
