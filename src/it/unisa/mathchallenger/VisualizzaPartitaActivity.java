package it.unisa.mathchallenger;

import java.io.IOException;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.DettagliNonPresentiException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.StatoPartita;
import it.unisa.mathchallenger.status.Status;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizzaPartitaActivity extends ActionBarActivity {
	private Communication comm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comm = Communication.getInstance();
		setContentView(R.layout.activity_visualizza_partita);
		int idPartita = getIntent().getIntExtra("id_partita", 0);
		if (idPartita > 0) {
			Partita p = Status.getInstance().getPartitaByID(idPartita);
			if (p != null) {
				Messaggio m = CommunicationMessageCreator.getInstance().createGetDettagliPartita(idPartita);
				try {
					comm.send(m);
					StatoPartita stato = CommunicationParser.getInstance().parseGetDettaglioPartita(m);
					int oldStat=p.getStatoPartita();
					p.setDettagliPartita(stato);
					int newStat=p.getStatoPartita();
					if(oldStat!=newStat)
						Status.getInstance().aggiornaPartita(p);
					disegna(p);
				}
				catch (IOException | LoginException | ConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			Toast.makeText(getApplicationContext(), "bundle = null", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		View view = (View) findViewById(R.id.container);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    view.setBackgroundResource (R.drawable.prova2hdhorizontal);
		} else {
		    view.setBackgroundResource (R.drawable.prova2hd);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizza_partita, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void disegna(final Partita p) {
		TextView tv_user_current = (TextView) findViewById(R.id.visualizzaUsernameProprioTV);
		tv_user_current.setText(Status.getInstance().getUtente().getUsername());
		TextView tv_avversario = (TextView) findViewById(R.id.VisualizzaUsernameAvversarioTV);
		tv_avversario.setText(p.getUtenteSfidato().getUsername());

		LinearLayout azione_container = (LinearLayout) findViewById(R.id.visualizza_azione_container);
		switch (p.getStatoPartita()) {
			case Partita.CREATA:
			case Partita.INIZIATA:
				if (!p.getDettagliPartita().isUtenteRisposto()) {
					float scale = getApplicationContext().getResources().getDisplayMetrics().density;
					int height = (int) (scale * 45 + 0.5f);
					Button b_gioca = new Button(getApplicationContext());
					LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
					b_gioca.setLayoutParams(dim);
					b_gioca.setTextColor(Color.BLACK);
					b_gioca.setText(R.string.gioca);
					b_gioca.setBackgroundResource(R.drawable.button_style);
					b_gioca.setOnClickListener(new Button.OnClickListener() {
						public void onClick(View v) {
							Intent intent=new Intent(getApplicationContext(), GiocaPartitaActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("id_partita", p.getIDPartita());
							startActivity(intent);
						}
					});
					azione_container.addView(b_gioca);
				}
				else {
					TextView in_attesa = new TextView(getApplicationContext());
					in_attesa.setGravity(Gravity.CENTER);
					in_attesa.setTextColor(Color.BLACK);
					in_attesa.setText(R.string.in_attesa_dell_avversario);
					in_attesa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
					in_attesa.setTypeface(null, Typeface.BOLD);
					azione_container.addView(in_attesa);
				}
				break;
			case Partita.PAREGGIATA:
				TextView pareggiata = new TextView(getApplicationContext());
				pareggiata.setText(R.string.pareggiata);
				pareggiata.setGravity(Gravity.CENTER);
				pareggiata.setTextColor(Color.BLACK);
				pareggiata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				pareggiata.setTypeface(null, Typeface.BOLD);
				azione_container.addView(pareggiata);
				break;
			case Partita.VINCITORE_1:
			case Partita.VINCITORE_2:
				try {
					if (p.haiVinto()) {
						TextView vinta = new TextView(getApplicationContext());
						vinta.setText(R.string.vinto);
						vinta.setGravity(Gravity.CENTER);
						vinta.setTextColor(Color.BLACK);
						vinta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
						vinta.setTypeface(null, Typeface.BOLD);
						azione_container.addView(vinta);
					}
					else {
						TextView persa = new TextView(getApplicationContext());
						persa.setText(R.string.persa);
						persa.setGravity(Gravity.CENTER);
						persa.setTextColor(Color.BLACK);
						persa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
						persa.setTypeface(null, Typeface.BOLD);
						azione_container.addView(persa);
					}
				}
				catch (DettagliNonPresentiException e) {
					TextView errore = new TextView(getApplicationContext());
					errore.setText(R.string.visualizza_errore_dettagli);
					errore.setGravity(Gravity.CENTER);
					errore.setTextColor(Color.BLACK);
					errore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
					errore.setTypeface(null, Typeface.BOLD);
					azione_container.addView(errore);
					e.printStackTrace();
				}
				break;
			case Partita.TEMPO_SCADUTO:
				TextView tempo_scaduto = new TextView(getApplicationContext());
				tempo_scaduto.setText(R.string.tempo_scaduto);
				tempo_scaduto.setGravity(Gravity.CENTER);
				tempo_scaduto.setTextColor(Color.BLACK);
				tempo_scaduto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				tempo_scaduto.setTypeface(null, Typeface.BOLD);
				azione_container.addView(tempo_scaduto);
				break;
			case Partita.ABBANDONATA_1:
			case Partita.ABBANDONATA_2:
				TextView abbandonata = new TextView(getApplicationContext());

				abbandonata.setGravity(Gravity.CENTER);
				abbandonata.setTextColor(Color.BLACK);
				abbandonata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				abbandonata.setTypeface(null, Typeface.BOLD);
				azione_container.addView(abbandonata);
				try {
					if (p.isAbbandonata()) {
						abbandonata.setText(R.string.abbandonata);
					}
					else
						abbandonata.setText(R.string.abbandonata_sfidante);
				}
				catch (DettagliNonPresentiException e) {
					abbandonata.setText(R.string.visualizza_errore_dettagli);
					e.printStackTrace();
				}
				break;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
