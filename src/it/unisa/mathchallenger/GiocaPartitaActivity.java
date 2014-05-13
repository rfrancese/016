package it.unisa.mathchallenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;



public class GiocaPartitaActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gioca_partita);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gioca_partita, menu);
		return true;
	}
}
