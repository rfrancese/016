package it.unisa.mathchallenger;

import java.io.IOException;
import java.util.ArrayList;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.Status;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeGiocoActivity extends ActionBarActivity {
	private Communication comm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comm=Communication.getInstance();
		setContentView(R.layout.activity_home_gioco);
		getPartite();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_gioco, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
			case R.id.action_logout_menu:
				Messaggio m_logout=CommunicationMessageCreator.getInstance().createLogoutMessage();
				try {
					comm.send(m_logout);
					if(CommunicationParser.getInstance().parseLogout(m_logout)){
						Status.getInstance().logout();
						Intent intent=new Intent(this, HomeAutenticazioneActivity.class);
						startActivity(intent);
					}
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.action_cambia_password_menu:	{
				Intent intent=new Intent(this, ActivityCambiaPassword.class);
				startActivity(intent);
				break;
			}
			//Messaggio m_res=CommunicationMessageCreator.getInstance().createChangePasswordMessage(pass);
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
	private void aggiungiPartite(ArrayList<Partita> partite){
		if(partite==null || partite.size()==0)
			return;
		LinearLayout lay=(LinearLayout) findViewById(R.id.layoutPartiteInCorso);
		lay.removeAllViews();
		ArrayList<Partita> terminate=new ArrayList<Partita>();
		TextView label_incorso=new TextView(getApplicationContext());
		label_incorso.setText(R.string.homegioco_partite_in_corso);
		lay.addView(label_incorso);
		float scale=getApplicationContext().getResources().getDisplayMetrics().density;
		int height=(int) (scale*45+0.5f);
		for(int i=0;i<partite.size();i++){
			final Partita p=partite.get(i);
			if(p.getStatoPartita()==Partita.CREATA || p.getStatoPartita()==Partita.INIZIATA){
				Button b_prt=new Button(getApplicationContext());
				b_prt.setText(p.getUtenteSfidato().getUsername());
				b_prt.setBackgroundResource(R.drawable.button_partite);
				b_prt.setHeight(height);
				b_prt.setGravity(Gravity.CENTER);
				b_prt.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						Intent intent=new Intent(getApplicationContext(), GiocaPartitaActivity.class);
						Bundle bun=new Bundle();
						bun.putInt("id_partita", p.getIDPartita());
						intent.putExtras(bun);
						startActivity(intent);
					}
				});
				lay.addView(b_prt);
			}
			else {
				terminate.add(p);
			}
		}
		if(terminate.size()>0){
			TextView label_terminate=new TextView(getApplicationContext());
			label_terminate.setText(R.string.homegioco_partite_terminate);
			lay.addView(label_terminate);
			for(int i=0;i<terminate.size();i++){
				final Partita partita=terminate.get(i);
				Button b=new Button(getApplicationContext());
				b.setBackgroundResource(R.drawable.button_partite);
				b.setText(partita.getUtenteSfidato().getUsername());
				b.setGravity(Gravity.CENTER);
				b.setHeight(height);
				b.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						Intent intent=new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
						Bundle bun=new Bundle();
						bun.putInt("id_partita", partita.getIDPartita());
						intent.putExtras(bun);
						startActivity(intent);
					}
				});
				lay.addView(b);
			}
		}
	}
	public void getPartite(){
		Messaggio m = CommunicationMessageCreator.getInstance().createGetPartiteInCorso();
		try {
			comm.send(m);
			ArrayList<Partita> p=CommunicationParser.getInstance().parseGetPartiteInCorso(m);
			aggiungiPartite(p);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
