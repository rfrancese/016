
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
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CercaUtenteActivity extends ActionBarActivity {
	private Communication comm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comm=Communication.getInstance();
		setContentView(R.layout.activity_cerca_utente);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cerca_utente, menu);
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
	
	public void onClickCerca(View v){
		TextView tv=(TextView) findViewById(R.id.textfield_cerca);
		String text=tv.getText().toString();
		if(text.contains(" ")){
			Toast.makeText(getApplicationContext(), R.string.messagge_cerca_contains_spazio, Toast.LENGTH_LONG).show();
		}
		else if(text.length()<3){
			Toast.makeText(getApplicationContext(), R.string.messagge_cerca_length, Toast.LENGTH_LONG).show();
		}
		else {
			Messaggio m=CommunicationMessageCreator.getInstance().createSearchUserMessage(text);
			try {
				comm.send(m);
				ArrayList<Account> res=CommunicationParser.getInstance().parseSearchUser(m);
				addResToLay(res);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void addResToLay(ArrayList<Account> res){
		LinearLayout lay=(LinearLayout) findViewById(R.id.risultati_ricerca);
		lay.removeAllViews();
		if(res!=null){
			TextView tv_res;
			if(res.size()==0){
				tv_res=new TextView(getApplicationContext());
				tv_res.setText(R.string.risultato_ricerca_nessun_risultato);
			}
			else {
				String str=getResources().getString(R.string.risultato_ricerca_trovati)+res.size();
				tv_res=new TextView(getApplicationContext());
				tv_res.setText(str);
			}
			tv_res.setTextColor(Color.BLACK);
			lay.addView(tv_res);
			float scale = getApplicationContext().getResources().getDisplayMetrics().density;
			int height = (int) (scale * 45 + 0.5f);
			int width= (int) (scale*220 +0.5f);
			//int width2= (int) (scale*60 +0.5f);
			for(int i=0;i<res.size();i++){
				Account acc=res.get(i);
				Button btn=new Button(getApplicationContext());
				
				LayoutParams dim = new LinearLayout.LayoutParams(width, height);
				//dim.setMargins(0, (int) (10 * scale), 0, 0);
				btn.setLayoutParams(dim);
				btn.setText(acc.getUsername());
				btn.setBackgroundResource(R.drawable.button_style);
				btn.setTextColor(Color.BLACK);
				btn.setGravity(Gravity.CENTER);
				btn.setOnClickListener(new Button.OnClickListener(){
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
				Button btn_friend=new Button(getApplicationContext());
				LayoutParams dim2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
				//dim2.setMargins(0, (int) (10 * scale), 0, 0);
				btn_friend.setLayoutParams(dim2);
				btn_friend.setBackgroundResource(R.drawable.button_style);
				btn_friend.setTextColor(Color.BLACK);
				btn_friend.setText("+");
				btn_friend.setOnClickListener(new Button.OnClickListener(){
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
				LinearLayout lay_r=new LinearLayout(getApplicationContext());
				lay_r.setOrientation(LinearLayout.HORIZONTAL);
				lay_r.addView(btn);
				lay_r.addView(btn_friend);
				lay.addView(lay_r);
			}
		}
		//TODO
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
