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
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HomeGiocoActivity extends ActionBarActivity {

	private Communication comm;
	private Thread		t_aggiorna_partite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comm = Communication.getInstance();
		setContentView(R.layout.activity_home_gioco);

		t_aggiorna_partite = new Thread(new t_aggiorna_partite());
		t_aggiorna_partite.start();
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
		switch (item.getItemId()) {
			case R.id.action_logout_menu:
				Messaggio m_logout = CommunicationMessageCreator.getInstance().createLogoutMessage();
				try {
					comm.send(m_logout);
					if (CommunicationParser.getInstance().parseLogout(m_logout)) {
						Status.getInstance().logout();
						Intent intent = new Intent(this, HomeAutenticazioneActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						t_aggiorna_partite.interrupt();
						startActivity(intent);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (LoginException e) {
					Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					t_aggiorna_partite.interrupt();
					startActivity(intent);
				}
				catch (ConnectionException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				break;
			case R.id.action_cambia_password_menu: {
				Intent intent = new Intent(this, ActivityCambiaPassword.class);
				startActivity(intent);
				break;
			}
			case R.id.action_exit_menu:
				t_aggiorna_partite.interrupt();
				comm.disconnect();
				Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	private void aggiungiPartite(ArrayList<Partita> partite) {
		if (partite == null || partite.size() == 0)
			return;
		final LinearLayout lay = (LinearLayout) findViewById(R.id.layoutPartiteInCorso);
		lay.removeAllViews();
		ArrayList<Partita> terminate = new ArrayList<Partita>();
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		int height = (int) (scale * 45 + 0.5f);
		boolean inCorso = false;
		for (int i = 0; i < partite.size(); i++) {
			final Partita p = partite.get(i);
			if (!p.isTerminata()) {
				inCorso = true;
				final Button b_prt = new Button(getApplicationContext());
				Account acc = p.getUtenteSfidato();
				b_prt.setText(acc == null ? "null" : acc.getUsername());
				b_prt.setTextColor(Color.BLACK);
				b_prt.setBackgroundResource(R.drawable.button_style);
				LayoutParams dim = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
				dim.setMargins(0, (int) (10 * scale), 0, 0);
				b_prt.setLayoutParams(dim);
				b_prt.setGravity(Gravity.CENTER);
				b_prt.setOnClickListener(new Button.OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
						Bundle bun = new Bundle();
						bun.putInt("id_partita", p.getIDPartita());
						intent.putExtras(bun);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						t_aggiorna_partite.interrupt();
						startActivity(intent);
					}
				});
				b_prt.setOnLongClickListener(new Button.OnLongClickListener() {

					public boolean onLongClick(View v) {
						new AlertDialog.Builder(HomeGiocoActivity.this).setCancelable(false).setMessage(R.string.dialog_abbandona_partita).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								Messaggio m = CommunicationMessageCreator.getInstance().createAbandonGame(p.getIDPartita());
								try {
									comm.send(m);
									if (CommunicationParser.getInstance().parseAbandon(m)) {
										Status.getInstance().rimuoviPartita(p.getIDPartita());
										lay.removeView(b_prt);
									}
								}
								catch (IOException | LoginException | ConnectionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).setNegativeButton(R.string.no, null).show();
						return true;
					}
				});
				lay.addView(b_prt);
			}
			else {
				terminate.add(p);
			}
		}
		if (inCorso) {
			TextView label_incorso = new TextView(getApplicationContext());
			label_incorso.setText(R.string.homegioco_partite_in_corso);
			label_incorso.setTextColor(Color.BLACK);
			lay.addView(label_incorso, 0);
		}
		if (terminate.size() > 0) {
			TextView label_terminate = new TextView(getApplicationContext());
			label_terminate.setText(R.string.homegioco_partite_terminate);
			label_terminate.setTextColor(Color.BLACK);
			lay.addView(label_terminate);
			for (int i = 0; i < terminate.size(); i++) {
				final Partita partita = terminate.get(i);
				final Button b = new Button(getApplicationContext());
				b.setBackgroundResource(R.drawable.button_style);
				b.setText(partita.getUtenteSfidato().getUsername());
				b.setTextColor(Color.BLACK);
				b.setGravity(Gravity.CENTER);
				LayoutParams dim = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
				dim.setMargins(0, (int) (10 * scale), 0, 0);
				b.setLayoutParams(dim);
				b.setOnClickListener(new Button.OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
						Bundle bun = new Bundle();
						bun.putInt("id_partita", partita.getIDPartita());
						intent.putExtras(bun);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						t_aggiorna_partite.interrupt();
						startActivity(intent);
					}
				});
				b.setOnLongClickListener(new Button.OnLongClickListener() {

					public boolean onLongClick(View v) {
						Status.getInstance().rimuoviPartita(partita.getIDPartita());
						lay.removeView(b);
						return true;
					}
				});
				lay.addView(b);
			}
		}
	}

	public void onClickNuovaPartita(View v) {
		Intent intent = new Intent(getApplicationContext(), NuovaPartitaActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// t_aggiorna_partite.interrupt();
		startActivity(intent);
	}

	public void getPartite() {
		Messaggio m = CommunicationMessageCreator.getInstance().createGetPartiteInCorso();
		try {
			comm.send(m);
			ArrayList<Partita> p = CommunicationParser.getInstance().parseGetPartiteInCorso(m);
			aggiungiPartite(p);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (LoginException e) {
			Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			t_aggiorna_partite.interrupt();
			startActivity(intent);
		}
		catch (ConnectionException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	class t_aggiorna_partite implements Runnable {

		private static final long time_sleep = 10 * 60 * 1000;

		public void run() {
			while (true) {
				Messaggio m = CommunicationMessageCreator.getInstance().createGetPartiteInCorso();
				try {
					comm.send(m);
					ArrayList<Partita> partite = CommunicationParser.getInstance().parseGetPartiteInCorso(m);
					if (partite != null) {
						for (int i = 0; i < partite.size(); i++)
							Status.getInstance().aggiungiPartita(partite.get(i));
					}
				}
				catch (IOException | LoginException | ConnectionException e1) {
					e1.printStackTrace();
				}
				runOnUiThread(new Runnable() {

					public void run() {
						aggiungiPartite(Status.getInstance().getElencoPartite());
					}
				});
				try {
					Thread.sleep(time_sleep);
				}
				catch (InterruptedException e) {
					Log.d("", "thread update interrotto");
				}
			}
		}
	}
}
