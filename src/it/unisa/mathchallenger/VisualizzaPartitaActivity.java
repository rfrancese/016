
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
import android.graphics.Color;
import android.os.Bundle;
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
		comm=Communication.getInstance();
		if(savedInstanceState!=null){
			int idPartita=savedInstanceState.getInt("id_partita");
			Partita p=Status.getInstance().getPartitaByID(idPartita);
			if(p!=null){
				Messaggio m=CommunicationMessageCreator.getInstance().createGetDettagliPartita(idPartita);
				try {
					comm.send(m);
					StatoPartita stato=CommunicationParser.getInstance().parseGetDettaglioPartita(m);
					p.setDettagliPartita(stato);
					disegna(p);
				}
				catch (IOException | LoginException | ConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		setContentView(R.layout.activity_visualizza_partita);
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
	private void disegna(Partita p){
		TextView tv_user_current=(TextView) findViewById(R.id.visualizzaUsernameProprioTV);
		tv_user_current.setText(Status.getInstance().getUtente().getUsername());
		TextView tv_avversario=(TextView) findViewById(R.id.VisualizzaUsernameAvversarioTV);
		tv_avversario.setText(p.getUtenteSfidato().getUsername());
		
		LinearLayout azione_container=(LinearLayout) findViewById(R.id.visualizza_azione_container);
		switch(p.getStatoPartita()){
			case Partita.INIZIATA:
				if(!p.getDettagliPartita().isUtenteRisposto()){
					float scale = getApplicationContext().getResources().getDisplayMetrics().density;
					int height = (int) (scale * 45 + 0.5f);
					Button b_gioca=new Button(getApplicationContext());
					LinearLayout.LayoutParams dim=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
					b_gioca.setLayoutParams(dim);
					b_gioca.setTextColor(Color.BLACK);
					b_gioca.setText(R.string.gioca);
					b_gioca.setBackgroundResource(R.drawable.button_style);
					b_gioca.setOnClickListener(new Button.OnClickListener() {
						public void onClick(View v) {
							Toast.makeText(getApplicationContext(), "Non disponibile", Toast.LENGTH_LONG).show();
						}
					});
					azione_container.addView(b_gioca);
				}
				else {
					TextView in_attesa=new TextView(getApplicationContext());
					in_attesa.setText(R.string.in_attesa_dell_avversario);
					azione_container.addView(in_attesa);
				}
				break;
			case Partita.PAREGGIATA:
				TextView pareggiata=new TextView(getApplicationContext());
				pareggiata.setText(R.string.pareggiata);
				azione_container.addView(pareggiata);
				break;
			case Partita.VINCITORE_1:
			case Partita.VINCITORE_2:
				try {
					if(p.haiVinto()){
						TextView vinta=new TextView(getApplicationContext());
						vinta.setText(R.string.vinto);
						azione_container.addView(vinta);
					}
					else {
						TextView vinta=new TextView(getApplicationContext());
						vinta.setText(R.string.vinto);
						azione_container.addView(vinta);
					}
				}
				catch (DettagliNonPresentiException e) {
					TextView errore=new TextView(getApplicationContext());
					errore.setText(e.getMessage());
					azione_container.addView(errore);
					e.printStackTrace();
				}
				break;
			case Partita.TEMPO_SCADUTO:
				TextView tempo_scaduto=new TextView(getApplicationContext());
				tempo_scaduto.setText(R.string.tempo_scaduto);
				azione_container.addView(tempo_scaduto);
				break;
		}
	}
}
