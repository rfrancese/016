package it.unisa.mathchallenger;

import java.io.IOException;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.status.Status;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeGiocoActivity extends ActionBarActivity {
	private Communication comm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comm=Communication.getInstance();
		setContentView(R.layout.activity_home_gioco);
		//TODO get partite
		//
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
}
