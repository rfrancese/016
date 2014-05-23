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
import android.content.res.Configuration;
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

	private Communication  comm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_partita);
		comm = Communication.getInstance();
		aggiornaAmici();
		aggiungiAmiciUI();
		View view = (View) findViewById(R.id.container);
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    view.setBackgroundResource (R.drawable.prova2hdhorizontal);
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
			if(p!=null){
			Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			}
			else 
				Toast.makeText(getApplicationContext(), R.string.errore_durante_creazione_partita, Toast.LENGTH_LONG).show();
		}
		catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (LoginException e) {
			Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		catch (ConnectionException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void onClickCercaUtente(View v) {
		Intent intent = new Intent(getApplicationContext(), CercaUtenteActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void aggiungiAmiciUI() {
		Log.d("", "AggiungiAmiciUI invoked");
		final LinearLayout lay = (LinearLayout) findViewById(R.id.amici_elenco);
		lay.removeAllViews();
		ArrayList<Account> amici = Status.getInstance().getElencoAmici();
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		int height = (int) (scale * 40 + 0.5f);
		int screen_w=getResources().getDisplayMetrics().widthPixels;
		int width = (int) (((screen_w/100)*90));
		for (int i = 0; i < amici.size(); i++) {
			final LinearLayout newLay = new LinearLayout(getApplicationContext());
			newLay.setOrientation(LinearLayout.HORIZONTAL);

			final Account acc = amici.get(i);
			Button btn_amico = new Button(getApplicationContext());
			newLay.addView(btn_amico);
			btn_amico.setText(acc.getUsername());
			btn_amico.setTextColor(Color.WHITE);
			btn_amico.setGravity(Gravity.CENTER);
			btn_amico.setBackgroundResource(R.drawable.button_amico);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
			params.setMargins(0, (int) (5 * scale), 0, 0);
			btn_amico.setLayoutParams(params);
			btn_amico.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Messaggio m=CommunicationMessageCreator.getInstance().createNewGameMessage(acc.getID());
					try {
						comm.send(m);
						Partita p=CommunicationParser.getInstance().parseNewGame(m);
						if(p!=null){
							Intent intent=new Intent(getApplicationContext(), HomeGiocoActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
						else {
							Toast.makeText(getApplicationContext(), R.string.errore_durante_creazione_partita, Toast.LENGTH_LONG).show();
						}
					}
					catch (IOException | LoginException | ConnectionException e) {
						e.printStackTrace();
					}
				}
			});

			Button btn_remove = new Button(getApplicationContext());
			newLay.addView(btn_remove);
			btn_remove.setTextColor(Color.BLACK);
			btn_remove.setGravity(Gravity.CENTER);
			btn_remove.setBackgroundResource(R.drawable.button_rimuovi);
			LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
			params2.setMargins(0, (int) (5 * scale), 0, 0);
			btn_remove.setLayoutParams(params2);
			btn_remove.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Messaggio m = CommunicationMessageCreator.getInstance().createRimuoviAmico(acc.getID());
					try {
						comm.send(m);
						if (CommunicationParser.getInstance().parseRimuoviAmico(m)) {
							Status.getInstance().rimuoviAmico(acc.getID());
							lay.removeView(newLay);
						}
					}
					catch (IOException e) {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					catch (LoginException e) {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					catch (ConnectionException e) {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

				}
			});
			lay.addView(newLay);
		}
	}
	private void aggiornaAmici(){
		if (Status.getInstance().isFriendUpdated()) {
			Status.getInstance().setFriendUpdated(true);
			Messaggio m = CommunicationMessageCreator.getInstance().createGetMyFriends();
			try {
				comm.send(m);
				ArrayList<Account> friends = CommunicationParser.getInstance().parseGetMyFriends(m);
				if (friends != null) {
					for (int i = 0; i < friends.size(); i++)
						Status.getInstance().aggiungiAmico(friends.get(i));
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (LoginException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (ConnectionException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}
}
