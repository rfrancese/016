package it.unisa.mathchallenger;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Intent;

public class AdsActivity extends ActionBarActivity {
	/** The view to show the ad. */
	private InterstitialAd	  adView;

	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "ca-app-pub-8204920344341375/5947464640";
	private boolean			 started	= false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ads);

		adView = new InterstitialAd(this);
		adView.setAdUnitId(AD_UNIT_ID);

		AdRequest adRequest = new AdRequest.Builder().addTestDevice("E943BD5C69F0A5BC165B54E395CAB1D8").build();
		adView.loadAd(adRequest);
		adView.setAdListener(new AdListener() {
			public void onAdLoaded() {
				displayInterstitial();
			}

			public void onAdClosed() {
				Intent intent = new Intent(getApplicationContext(), VisualizzaPartitaActivity.class);
				Bundle bun = new Bundle();
				bun.putInt("id_partita", getIntent().getIntExtra("id_partita", 0));
				intent.putExtras(bun);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	public void displayInterstitial() {
		if (adView.isLoaded()) {
			time_start = System.currentTimeMillis();
			started = true;
			adView.show();
		}
	}

	long time_start;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() > time_start + 3000 && started) {
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
