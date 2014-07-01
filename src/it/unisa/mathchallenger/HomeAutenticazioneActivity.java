package it.unisa.mathchallenger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.ListaErrori;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Status;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeAutenticazioneActivity extends ActionBarActivity {

	Communication			comm;
	private int			  current_layout   = 0;
	private final static int HOME			 = 0, REGISTRA = 1, RECUPERA = 2;
	//private static boolean   socketOk		 = false;
	private boolean   isVersionChecked = false;
	private boolean   isVersionValid   = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(getIntent().getBooleanExtra("exit", false)){
			Status.getInstance().closeDB();
			Communication.getInstance().disconnect();
			finish();
			System.exit(0);
		}
		super.onCreate(savedInstanceState);
		current_layout = HOME;
		if (VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		comm = Communication.getInstance();
		Status.getInstance(getApplicationContext());

		if (isValidVersion()) {
			loginWithAuthcode();
		}
		else {
			setContentView(R.layout.activity_home_autenticazione);
			View view = (View) findViewById(R.id.ScrollHomeAutenticazione);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				view.setBackgroundResource(R.drawable.sfondo_landscape_no);
			}
			else {
				view.setBackgroundResource(R.drawable.sfondohome);
			}
			if (!isVersionChecked)
				new AlertDialog.Builder(this).setMessage(R.string.errore_verificare_connessione).setCancelable(false).setPositiveButton("OK", null).show();
			if(isVersionChecked && !isVersionValid)
				new AlertDialog.Builder(this).setMessage(R.string.errore_versione).setCancelable(false).setPositiveButton("OK", null).show();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		View view = null;
		if (current_layout == HOME)
			view = (View) findViewById(R.id.ScrollHomeAutenticazione);
		else if (current_layout == REGISTRA)
			view = (View) findViewById(R.id.ContainerRegistra);
		else if (current_layout == RECUPERA)
			view = (View) findViewById(R.id.ContainerRecupera);

		if (view != null) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				view.setBackgroundResource(R.drawable.sfondo_landscape_no);
			}
			else {
				view.setBackgroundResource(R.drawable.sfondohome);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_autenticazione, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_homelogin_exit) {
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (current_layout == HOME) {
			new AlertDialog.Builder(this).setMessage(R.string.exit_confirm).setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					HomeAutenticazioneActivity.this.finish();
				}
			}).setNegativeButton("No", null).show();
		}
		else {
			setContentView(R.layout.activity_home_autenticazione);
			current_layout = HOME;
		}
	}

	public void onClickRegistra(View v) {
		if (v.getId() == R.id.button_registrazione_home) {
			setContentView(R.layout.activity_register_layout);
			current_layout = REGISTRA;
			EditText user = (EditText) findViewById(R.id.reg_username_text);
			EditText pass = (EditText) findViewById(R.id.reg_pass1_text);
			EditText mail = (EditText) findViewById(R.id.reg_email_text);

			pass.setOnKeyListener(new EditText.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					switch (keyCode) {
						case KeyEvent.KEYCODE_SPACE:
							String s = ((TextView) v).getText().toString().replace(" ", "");
							((TextView) v).setText(s);
							((TextView) v).setText(s);
							EditText txt = (EditText) v;
							Editable text = txt.getText();
							Selection.setSelection(text, s.length());
							Toast.makeText(getApplicationContext(), R.string.string_contains_spaces, Toast.LENGTH_LONG).show();
							break;
						default:
							break;
					}
					return false;
				}
			});
			user.setOnKeyListener(new EditText.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					switch (keyCode) {
						case KeyEvent.KEYCODE_SPACE:
							String s = ((TextView) v).getText().toString().replace(" ", "");
							((TextView) v).setText(s);
							EditText txt = (EditText) v;
							Editable text = txt.getText();
							Selection.setSelection(text, s.length());
							Toast.makeText(getApplicationContext(), R.string.string_contains_spaces, Toast.LENGTH_LONG).show();
							break;
						default:
							break;
					}
					String s2 = ((TextView) v).getText().toString();

					if (s2.length() > 30) {
						s2 = s2.substring(0, s2.length() - 1);
						((TextView) v).setText(s2);
						EditText txt = (EditText) v;
						Editable text = txt.getText();
						Selection.setSelection(text, s2.length());
						new AlertDialog.Builder(HomeAutenticazioneActivity.this).setMessage(R.string.invalid_registration_username).setCancelable(true).setNeutralButton("OK", null).show();

					}
					return false;
				}
			});
			mail.setOnKeyListener(new EditText.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					switch (keyCode) {
						case KeyEvent.KEYCODE_SPACE:
							String s = ((TextView) v).getText().toString().replace(" ", "");
							((TextView) v).setText(s);
							((TextView) v).setText(s);
							EditText txt = (EditText) v;
							Editable text = txt.getText();
							Selection.setSelection(text, s.length());
							Toast.makeText(getApplicationContext(), R.string.string_contains_spaces, Toast.LENGTH_LONG).show();
							break;
						default:
							break;
					}
					String email = ((TextView) v).getText().toString();
					if (email.length() > 100) {
						new AlertDialog.Builder(HomeAutenticazioneActivity.this).setMessage(R.string.invalid_registration_mail).setCancelable(true).setNeutralButton("OK", null).show();
					}

					return false;
				}
			});
		}
		else if (v.getId() == R.id.reg_registra_button) {
			TextView user_tv = (TextView) findViewById(R.id.reg_username_text);
			TextView pass1_tv = (TextView) findViewById(R.id.reg_pass1_text);
			TextView pass2_tv = (TextView) findViewById(R.id.reg_pass2_text);
			TextView email_tv = (TextView) findViewById(R.id.reg_email_text);
			String username = user_tv.getText().toString();
			String pass1 = pass1_tv.getText().toString();
			String pass2 = pass2_tv.getText().toString();
			String email = email_tv.getText().toString();

			if (username.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || email.isEmpty()) {
				Toast.makeText(getApplicationContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();
				return;
			}

			if (pass1.length() < 8) {
				new AlertDialog.Builder(HomeAutenticazioneActivity.this).setMessage(R.string.invalid_registration_password).setCancelable(true).setNeutralButton("OK", null).show();
				return;
			}

			if (pass1.compareTo(pass2) != 0) {
				Toast.makeText(getApplicationContext(), R.string.reg_password_diverse, Toast.LENGTH_LONG).show();
				return;
			}
			if (!isValidEmail(email)) {
				Toast.makeText(getApplicationContext(), R.string.email_non_valida, Toast.LENGTH_LONG).show();
				return;
			}

			Messaggio m = CommunicationMessageCreator.getInstance().createRegisterMessage(username, pass1, email);
			try {
				comm.send(m);
				AccountUser acc = CommunicationParser.getInstance().parseRegister(m);
				if (acc == null) {
					if (m.hasError()) {
						Toast.makeText(getApplicationContext(), m.getErrorID() >= 0 ? getResources().getString(m.getErrorID()) : m.getErrorMessage(), Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(getApplicationContext(), R.string.error_unknown, Toast.LENGTH_LONG).show();
				}
				else {
					acc.setUsername(username);
					Status.getInstance().login(acc);

					Intent intent = new Intent(this, HomeGiocoActivity.class);
					startActivity(intent);
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), R.string.communication_error, Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (LoginException e) {
				e.printStackTrace();
			}
			catch (ConnectionException e) {
				e.printStackTrace();
			}
		}

	}

	public void onClickResetPassword(View v) {
		if (v.getId() == R.id.button_recupera_pass_home) {
			current_layout = RECUPERA;
			setContentView(R.layout.activity_reset_password);
		}
		else if (v.getId() == R.id.reg_reset_pass_button) {
			TextView user_tv = (TextView) findViewById(R.id.reset_username_text);
			String user = user_tv.getText().toString();
			if (user.isEmpty()) {

			}
			else {
				Messaggio msg = CommunicationMessageCreator.getInstance().createResetMessage(user);
				try {
					comm.send(msg);
					boolean reset_psw = CommunicationParser.getInstance().parseResetPassword(msg);
					if (reset_psw) {
						Toast.makeText(getApplicationContext(), R.string.reset_password_ok, Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(getApplicationContext(), R.string.reset_password_error, Toast.LENGTH_LONG).show();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (LoginException e) {
					e.printStackTrace();
				}
				catch (ConnectionException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isValidVersion() {
		if (isVersionChecked)
			return isVersionValid;
		Messaggio m = CommunicationMessageCreator.getInstance().createIsValidVersion(Status.CURRENT_VERSION);
		try {
			comm.send(m);
			isVersionChecked = true;
			isVersionValid = CommunicationParser.getInstance().parseValidateVersion(m);
			return isVersionValid;
		}
		catch (IOException | LoginException | ConnectionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void onClickAccedi(View v) {
		if (isValidVersion()) {
			TextView u_tv = (TextView) findViewById(R.id.username_text);
			TextView p_tv = (TextView) findViewById(R.id.password_text);
			String username = u_tv.getText().toString();
			String pass = p_tv.getText().toString();

			try {
				if(!isVersionChecked){
					Messaggio validaVersione=CommunicationMessageCreator.getInstance().createIsValidVersion(Status.CURRENT_VERSION);
					comm.send(validaVersione);
					isVersionChecked=true;
					isVersionValid=CommunicationParser.getInstance().parseValidateVersion(validaVersione);
					if(!isValidVersion()){
						int id_error=ListaErrori.getMessage(validaVersione.getErrorMessage());
						Toast.makeText(getApplicationContext(), id_error>=0?getResources().getString(id_error):validaVersione.getErrorMessage(), Toast.LENGTH_LONG).show();
					}
				}
				Messaggio m = CommunicationMessageCreator.getInstance().createLoginMessage(username, pass);
				comm.send(m);
				AccountUser acc = CommunicationParser.getInstance().parseLogin(m);
				if (acc == null) {
					if(m.getErrorMessage().compareTo(""+ListaErrori.SEI_LOGGATO)==0){
						Status.getInstance().loginAuth(acc);
						Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					else
						Toast.makeText(getApplicationContext(), R.string.login_fail_message, Toast.LENGTH_LONG).show();
				}
				else {
					acc.setUsername(username);
					Status.getInstance().login(acc);
					Intent intent = new Intent(this, HomeGiocoActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), R.string.communication_error, Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			catch (LoginException e) {
				e.printStackTrace();
			}
			catch (ConnectionException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		else
			new AlertDialog.Builder(this).setMessage("Per poter accedere, scarica l'ultima versione di Math Challenger dal Play Store").setCancelable(false).setPositiveButton("OK", null).show();
	}

	private void loginWithAuthcode() {
		boolean loginOK = false;
		AccountUser acc = Status.getInstance().getUtente();
		Messaggio m = null;
		if (acc != null) {
			m = CommunicationMessageCreator.getInstance().createLoginAuthcode(acc.getID(), acc.getAuthCode());
			try {
				comm.send(m);
				loginOK = CommunicationParser.getInstance().parseLoginAuthcode(m);
			}
			catch (IOException | LoginException | ConnectionException e) {
				e.printStackTrace();
			}
			
		}
		if(!loginOK &&m!=null && m.getErrorMessage().compareTo(ListaErrori.SEI_LOGGATO+"")==0){ //già loggato
			Status.getInstance().loginAuth(acc);
			Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		else if (!loginOK) {
			setContentView(R.layout.activity_home_autenticazione);
			View view = (View) findViewById(R.id.ScrollHomeAutenticazione);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				view.setBackgroundResource(R.drawable.sfondo_landscape_no);
			}
			else {
				view.setBackgroundResource(R.drawable.sfondohome);
			}
			/*
			if (!socketOk)
				Toast.makeText(getApplicationContext(), R.string.errore_verificare_connessione, Toast.LENGTH_LONG).show();
			*/
		}
		else {
			Status.getInstance().loginAuth(acc);
			Intent intent = new Intent(getApplicationContext(), HomeGiocoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	private boolean isValidEmail(String email) {
		Pattern p = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
