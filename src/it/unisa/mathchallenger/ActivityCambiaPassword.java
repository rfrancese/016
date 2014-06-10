package it.unisa.mathchallenger;

import java.io.IOException;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCambiaPassword extends ActionBarActivity {

	private Communication comm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cambia_password);
		comm = Communication.getInstance();
		View view = (View) findViewById(R.id.cambiapasslayout);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			view.setBackgroundResource(R.drawable.sfondo_landscape_no);
		}
		else {
			view.setBackgroundResource(R.drawable.sfondo_potrait_no);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		View view = (View) findViewById(R.id.cambiapasslayout);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			view.setBackgroundResource(R.drawable.sfondo_landscape_no);
		}
		else {
			view.setBackgroundResource(R.drawable.sfondo_potrait_no);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cambia_password, menu);
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	public void onClickCambiaPassword(View v) {
		String oldPass_tv = ((TextView) findViewById(R.id.vecchia_pass_text)).getText().toString();
		String newPass1 = ((TextView) findViewById(R.id.pass1_cambia_text)).getText().toString();
		String newPass2 = ((TextView) findViewById(R.id.pass2_cambia_text)).getText().toString();
		if (newPass1.compareTo(newPass2) == 0) {
			Messaggio m = CommunicationMessageCreator.getInstance().createChangePasswordMessage(oldPass_tv, newPass1);
			try {
				comm.send(m);
				if (CommunicationParser.getInstance().parseChangePassword(m)) {
					Toast.makeText(getApplicationContext(), R.string.message_cambio_pass_ok, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getApplicationContext(), m.getErrorID()>=0?getResources().getString(m.getErrorID()):m.getErrorMessage(), Toast.LENGTH_LONG).show();
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (LoginException e) {
				Intent intent = new Intent(getApplicationContext(), HomeAutenticazioneActivity.class);
				startActivity(intent);
				finish();
			}
			catch (ConnectionException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.message_cambio_pass_password_non_corrispondenti, Toast.LENGTH_LONG).show();;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intente = new Intent(getApplicationContext(), HomeGiocoActivity.class);
		intente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intente);
	}
}
