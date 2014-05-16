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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class GiocaPartitaActivity extends Activity {
	private Communication comm;
	private final static int DURATA_DOMANDA = 10;
	private Partita	   partita;
	private int		   domanda_corrente = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gioca_partita);
		comm = Communication.getInstance();
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
	private Thread tempo=null; 
	private void scriviDomanda() {
		if (domanda_corrente < partita.getNumDomande()) {
			Domanda d = partita.getDomanda(domanda_corrente);
			Button domanda = (Button) findViewById(R.id.gioca_partita_domanda);
			Button risp1 = (Button) findViewById(R.id.gioca_partita_risp1);
			Button risp2 = (Button) findViewById(R.id.gioca_partita_risp2);
			Button risp3 = (Button) findViewById(R.id.gioca_partita_risp3);
			Button risp4 = (Button) findViewById(R.id.gioca_partita_risp4);
			domanda.setText(d.getDomanda());
			risp1.setText(d.getRisposta(0) + "");
			risp2.setText(d.getRisposta(1) + "");
			risp3.setText(d.getRisposta(2) + "");
			risp4.setText(d.getRisposta(3) + "");
			risp1.setOnClickListener(new clickRisposta(d));
			risp2.setOnClickListener(new clickRisposta(d));
			risp3.setOnClickListener(new clickRisposta(d));
			risp4.setOnClickListener(new clickRisposta(d));
			final ProgressBar bar=(ProgressBar) findViewById(R.id.progressBar1);
			runOnUiThread(new Runnable() {
				public void run() {
					bar.setMax(DURATA_DOMANDA);
				}
			});
			tempo=new timer_partita(bar);
			tempo.start();
		}
		else {
			Messaggio mess= CommunicationMessageCreator.getInstance().createRisposte(partita);
			try {
				comm.send(mess);
			} 
			catch (IOException | LoginException | ConnectionException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("id_partita", partita.getIDPartita());
			startActivity(intent);
		}
	}

	class clickRisposta implements Button.OnClickListener {
		Domanda domanda;

		public clickRisposta(Domanda d) {
			domanda = d;
		}

		public void onClick(View v) {
			tempo.interrupt();
			Button b = (Button) v;
			String risposta = b.getText().toString();
			float r = Float.parseFloat(risposta);
			domanda.setRispostaUtente(r);
			domanda_corrente++;
			scriviDomanda();
		}
	}
	class timer_partita extends Thread {
		private ProgressBar progressbar;
		public timer_partita(ProgressBar bar){
			progressbar=bar;
		}
		public void run() {
			int time=DURATA_DOMANDA;
			while(time>0){
				try {
					sleep(1000L);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				time--;
				progressUpdater pu=new progressUpdater(progressbar, time);
				runOnUiThread(pu);
			}
			domanda_corrente++;
			//TODO assegna risposta sbagliata
			scriviDomanda();
		}
	}
	class progressUpdater implements Runnable {
		int value;
		ProgressBar p_bar;
		public progressUpdater(ProgressBar bar, int val){
			value=val;
			p_bar=bar;
		}
		public void run() {
			p_bar.setProgress(value);
		}
		
	}
}
