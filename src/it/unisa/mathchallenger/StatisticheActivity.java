package it.unisa.mathchallenger;

import java.io.IOException;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Statistiche;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StatisticheActivity extends Activity {
	private Communication comm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistiche);
		comm=Communication.getInstance();
		View view = (View) findViewById(R.id.containerStatistiche);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			view.setBackgroundResource(R.drawable.prova2hdhorizontal);
		}
		else {
			view.setBackgroundResource(R.drawable.prova2hd);
		}
		getMieStatistiche();
	}
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		View view = (View) findViewById(R.id.containerStatistiche);
		if(view!=null){
    		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
    			view.setBackgroundResource(R.drawable.prova2hdhorizontal);
    		}
    		else {
    			view.setBackgroundResource(R.drawable.prova2hd);
    		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistiche, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.aggiorna_statistiche) {
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		Intent intent=new Intent(getApplicationContext(), HomeGiocoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	private void getMieStatistiche(){
		Messaggio m=CommunicationMessageCreator.getInstance().createGetStatistiche();
		try {
			comm.send(m);
			Statistiche stat=CommunicationParser.getInstance().parseGetStatistiche(m);
			aggiornaMieStatistiche(stat);
		}
		catch (IOException | LoginException | ConnectionException e) {
			e.printStackTrace();
		}
	}
	private void aggiornaMieStatistiche(Statistiche s){
		TextView giocate=(TextView) findViewById(R.id.tv_partite_giocate);
		giocate.setText(giocate.getText().toString()+s.getPartite_giocate());
		TextView vinte=(TextView) findViewById(R.id.tv_partite_vinte);
		vinte.setText(vinte.getText().toString()+s.getVittorie());
		TextView pareggiate=(TextView) findViewById(R.id.tv_partite_pareggiate);
		pareggiate.setText(pareggiate.getText().toString()+s.getPareggi());
		TextView perse=(TextView) findViewById(R.id.tv_partite_perse);
		perse.setText(perse.getText().toString()+s.getSconfitte());
		TextView abbandonate=(TextView) findViewById(R.id.tv_partite_abbandonate);
		abbandonate.setText(abbandonate.getText().toString()+s.getAbbandonate());
		TextView punti=(TextView) findViewById(R.id.tv_punti);
		punti.setText(punti.getText().toString()+s.getPunti());
	}
}
