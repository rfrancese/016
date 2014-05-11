
package it.unisa.mathchallenger;

import java.io.IOException;
import java.util.ArrayList;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Account;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.Status;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class NuovaPartitaActivity extends ActionBarActivity {

	private Communication comm;
	private static boolean firstStart=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_partita);
		comm = Communication.getInstance();
		if(firstStart){
			firstStart=false;
			Messaggio m=CommunicationMessageCreator.getInstance().createGetMyFriends();
			try {
				comm.send(m);
				ArrayList<Account> friends=CommunicationParser.getInstance().parseGetMyFriends(m);
				if(friends!=null){
					for(int i=0;i<friends.size();i++)
						Status.getInstance().aggiungiAmico(friends.get(i));
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (LoginException e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (ConnectionException e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		aggiungiAmiciUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuova_partita, menu);
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

	public void onClickCreaRandom(View v) {
		Messaggio m = CommunicationMessageCreator.getInstance().createNewGameRandom();
		try {
			comm.send(m);
			Partita p = CommunicationParser.getInstance().parseNewGameRandom(m);
			Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			// TODO avvio partita
		}
		catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (LoginException e) {
			Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		catch (ConnectionException e) {
			Toast.makeText(getApplicationContext(),
					e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	public void onClickCercaUtente(View v){
		Intent intent=new Intent(getApplicationContext(), CercaUtenteActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void aggiungiAmiciUI(){
		Log.d("", "AggiungiAmiciUI invoked");
		final LinearLayout lay=(LinearLayout) findViewById(R.id.amici_elenco);
		lay.removeAllViews();
		ArrayList<Account> amici=Status.getInstance().getElencoAmici();
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		int height = (int) (scale * 45 + 0.5f);
		int width= (int) (scale*250 +0.5f);
		for(int i=0;i<amici.size();i++){
			final LinearLayout newLay=new LinearLayout(getApplicationContext());
			newLay.setOrientation(LinearLayout.HORIZONTAL);
			
			final Account acc=amici.get(i);
			Button btn_amico=new Button(getApplicationContext());
			newLay.addView(btn_amico);
			btn_amico.setText(acc.getUsername());
			btn_amico.setTextColor(Color.BLACK);
			btn_amico.setGravity(Gravity.CENTER);
			btn_amico.setBackgroundResource(R.drawable.button_style);
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, height);
			params.setMargins(0, (int) (10*scale), 0, 0);
			btn_amico.setLayoutParams(params);
			
			Button btn_remove=new Button(getApplicationContext());
			newLay.addView(btn_remove);
			btn_remove.setText("X");
			btn_remove.setTextColor(Color.BLACK);
			btn_remove.setGravity(Gravity.CENTER);
			btn_remove.setBackgroundResource(R.drawable.button_style);
			LayoutParams params2=new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			params2.setMargins(0, (int) (10*scale), 0, 0);
			btn_remove.setLayoutParams(params2);
			btn_remove.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View v) {
					Messaggio m=CommunicationMessageCreator.getInstance().createRimuoviAmico(acc.getID());
					try {
						comm.send(m);
						if(CommunicationParser.getInstance().parseRimuoviAmico(m)){
							Status.getInstance().rimuoviAmico(acc.getID());
							lay.removeView(newLay);
						}
					}
					catch (IOException e) {
						Toast.makeText(getApplicationContext(),
								e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					catch (LoginException e) {
						Toast.makeText(getApplicationContext(),
								e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					catch (ConnectionException e) {
						Toast.makeText(getApplicationContext(),
								e.getMessage(),
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					
				}
			});
			lay.addView(newLay);
		}
	}
}
