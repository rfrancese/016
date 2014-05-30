package it.unisa.mathchallenger;

import java.io.IOException;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Classifica;
import it.unisa.mathchallenger.status.Statistiche;
import it.unisa.mathchallenger.widget.CustomButton;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
		getClassifica();
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
			getMieStatistiche();
			getClassifica();
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
		giocate.setText(getResources().getString(R.string.stat_giocate)+s.getPartite_giocate());
		TextView vinte=(TextView) findViewById(R.id.tv_partite_vinte);
		vinte.setText(getResources().getString(R.string.stat_vinte)+s.getVittorie());
		TextView pareggiate=(TextView) findViewById(R.id.tv_partite_pareggiate);
		pareggiate.setText(getResources().getString(R.string.stat_pareggiate)+s.getPareggi());
		TextView perse=(TextView) findViewById(R.id.tv_partite_perse);
		perse.setText(getResources().getString(R.string.stat_perse)+s.getSconfitte());
		TextView abbandonate=(TextView) findViewById(R.id.tv_partite_abbandonate);
		abbandonate.setText(getResources().getString(R.string.stat_abbandonate)+s.getAbbandonate());
		TextView punti=(TextView) findViewById(R.id.tv_punti);
		punti.setText(getResources().getString(R.string.stat_punti)+s.getPunti());
	}
	private void getClassifica(){
		Classifica cl=Classifica.getInstance();
		cl.loadClassifica();
		LinearLayout container_top=(LinearLayout) findViewById(R.id.containerClassifica);
		container_top.removeAllViews();
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		int width_pos=(int) ((scale*(getResources().getDisplayMetrics().widthPixels/100))*5);
		int width_nome=(int) ((scale*(getResources().getDisplayMetrics().widthPixels/100))*80);
		int width_punti=(int) ((scale*(getResources().getDisplayMetrics().widthPixels/100))*15);
		for(int i=0;i<cl.getNumeroUtenti();i++){
			String utente=cl.getUsernameAtIndex(i);
			int punti=cl.getPuntiAtIndex(i);
			
			Log.d("Utente:", utente+ " - Punti: "+punti);
			RelativeLayout container=new RelativeLayout(getApplicationContext());
			CustomButton b_posizione=new CustomButton(getApplicationContext());
			b_posizione.setId(i*3+1);
			b_posizione.setBackgroundResource(R.drawable.button_trasparente);
			switch(i){
				case 0:
				case 1:
				case 2:
				default: 
						b_posizione.setText(""+(i+1));
			}
			RelativeLayout.LayoutParams l_pos=new RelativeLayout.LayoutParams(width_pos,ViewGroup.LayoutParams.WRAP_CONTENT);
			l_pos.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			b_posizione.setLayoutParams(l_pos);
			
			
			CustomButton b_nome=new CustomButton(getApplicationContext());
			b_nome.setText(utente);
			b_nome.setGravity(Gravity.CENTER);
			b_nome.setBackgroundResource(R.drawable.button_trasparente);
			b_nome.setId(i*3+2);
			RelativeLayout.LayoutParams l_nome=new RelativeLayout.LayoutParams(width_nome,ViewGroup.LayoutParams.WRAP_CONTENT);
			l_nome.addRule(RelativeLayout.RIGHT_OF, i*3+1);
			l_nome.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			b_nome.setLayoutParams(l_nome);
			
			
			CustomButton b_punti=new CustomButton(getApplicationContext());
			b_punti.setId(i*3+3);
			b_punti.setText(punti+"");
			b_punti.setGravity(Gravity.CENTER);
			b_punti.setBackgroundResource(R.drawable.button_trasparente);
			RelativeLayout.LayoutParams l_punti=new RelativeLayout.LayoutParams(width_punti,ViewGroup.LayoutParams.WRAP_CONTENT);
			l_punti.addRule(RelativeLayout.RIGHT_OF, i*3+2);
			l_punti.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			b_punti.setLayoutParams(l_punti);
			b_punti.setId(i*3+1);
			
			container.addView(b_posizione);
			container.addView(b_nome);
			//container.addView(b_punti);
			
			container_top.addView(container);
		}
	}
}
