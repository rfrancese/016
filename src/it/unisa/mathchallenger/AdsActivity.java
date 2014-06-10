package it.unisa.mathchallenger;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AdsActivity extends ActionBarActivity {
	/** The view to show the ad. */
	private AdView			  adView;

	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "INSERT_YOUR_AD_UNIT_ID_HERE";

	/** Called when the activity is first created. */
	@Override
	  public void onCreate(Bundle savedInstanceState) {
		time_start=System.currentTimeMillis();
//	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.);
//
//	    // Create an ad.
//	    adView = new AdView(this);
//	    adView.setAdSize(AdSize.BANNER);
//	    adView.setAdUnitId(AD_UNIT_ID);
//
//	    // Add the AdView to the view hierarchy. The view will have no size
//	    // until the ad is loaded.
//	    LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
//	    layout.addView(adView);
//
//	    // Create an ad request. Check logcat output for the hashed device ID to
//	    // get test ads on a physical device.
//	    AdRequest adRequest = new AdRequest.Builder()
//	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//	        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
//	        .build();
//
//	    // Start loading the ad in the background.
//	    adView.loadAd(adRequest);
	  }
//
	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
	
	long time_start;
	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis()>time_start+3000){
			Intent intent = new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
			Bundle bun = new Bundle();
			bun.putInt("id_partita", getIntent().getIntExtra("id_partita", 0));
			intent.putExtras(bun);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.ad_non_skippable, Toast.LENGTH_SHORT).show();
		}
	}
}
