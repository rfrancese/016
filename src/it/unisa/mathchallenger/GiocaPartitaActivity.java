package it.unisa.mathchallenger;

import java.io.IOException;
import java.util.ArrayList;

import it.unisa.mathchallenger.communication.Communication;
import it.unisa.mathchallenger.communication.CommunicationMessageCreator;
import it.unisa.mathchallenger.communication.CommunicationParser;
import it.unisa.mathchallenger.communication.Messaggio;
import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.Domanda;
import it.unisa.mathchallenger.status.Partita;
import it.unisa.mathchallenger.status.Status;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class GiocaPartitaActivity extends Activity {
    private Communication    comm;
    private final static int DURATA_DOMANDA   = 10;
    private Partita	  partita;
    private int	      domanda_corrente = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_gioca_partita);
	dimensionaBottoni();
	comm = Communication.getInstance();
	int id = getIntent().getIntExtra("id_partita", 0);
	if (id > 0) {
	    Messaggio m = CommunicationMessageCreator.getInstance().createGetDomande(id);
	    try {
		comm.send(m);
		ArrayList<Domanda> list = CommunicationParser.getInstance().parseGetDomande(m);
		partita = Status.getInstance().getPartitaByID(id);
		for (int i = 0; i < list.size(); i++) {
		    Domanda dom = list.get(i);
		    partita.aggiungiDomanda(dom);
		}
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
	View view = (View) findViewById(R.id.containerGiocaPartita);
	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    view.setBackgroundResource(R.drawable.prova2hdhorizontal);
	}
	else {
	    view.setBackgroundResource(R.drawable.prova2hd);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.gioca_partita, menu);
	return true;
    }

    public void onBackPressed() {

    };

    public void onClickStart(View v) {
	Button buttonAvvia = (Button) findViewById(R.id.contdomanda);
	buttonAvvia.setOnClickListener(null);
	buttonAvvia.setClickable(false);
	buttonAvvia.setText("");
	scriviDomanda();
    }

    private Thread tempo = null;

    private void scriviDomanda() {
	if (domanda_corrente < partita.getNumDomande()) {
	    Domanda d = partita.getDomanda(domanda_corrente);
	    Button domanda = (Button) findViewById(R.id.contdomanda);
	    Button risp1 = (Button) findViewById(R.id.gioca_partita_risp1);
	    Button risp2 = (Button) findViewById(R.id.gioca_partita_risp2);
	    Button risp3 = (Button) findViewById(R.id.gioca_partita_risp3);
	    Button risp4 = (Button) findViewById(R.id.gioca_partita_risp4);
	    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
	    domanda.setTypeface(font);
	    domanda.setText(d.getDomanda());

	    String r1 = (d.getRisposta(0) + "").endsWith(".0") ? (d.getRisposta(0) + "").substring(0, (d.getRisposta(0) + "").length() - 2) : d.getRisposta(0) + "";
	    risp1.setText(r1);
	    risp1.setTextColor(Color.WHITE);
	    String r2 = (d.getRisposta(1) + "").endsWith(".0") ? (d.getRisposta(1) + "").substring(0, (d.getRisposta(1) + "").length() - 2) : d.getRisposta(1) + "";
	    risp2.setText(r2);
	    risp2.setTextColor(Color.WHITE);
	    String r3 = (d.getRisposta(2) + "").endsWith(".0") ? (d.getRisposta(2) + "").substring(0, (d.getRisposta(2) + "").length() - 2) : d.getRisposta(2) + "";
	    risp3.setText(r3);
	    risp3.setTextColor(Color.WHITE);
	    String r4 = (d.getRisposta(3) + "").endsWith(".0") ? (d.getRisposta(3) + "").substring(0, (d.getRisposta(3) + "").length() - 2) : d.getRisposta(3) + "";
	    risp4.setText(r4);
	    risp4.setTextColor(Color.WHITE);

	    risp1.setOnClickListener(new clickRisposta(d, d.getRisposta(0)));
	    risp2.setOnClickListener(new clickRisposta(d, d.getRisposta(1)));
	    risp3.setOnClickListener(new clickRisposta(d, d.getRisposta(2)));
	    risp4.setOnClickListener(new clickRisposta(d, d.getRisposta(3)));
	    final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
	    runOnUiThread(new Runnable() {
		public void run() {
		    bar.setMax(DURATA_DOMANDA * 1000);
		}
	    });

	    tempo = new timer_partita(bar);
	    tempo.start();
	}
	else {
	    Messaggio mess = CommunicationMessageCreator.getInstance().createRisposte(partita);
	    try {
		comm.send(mess);
	    }
	    catch (IOException | LoginException | ConnectionException e) {
		e.printStackTrace();
	    }
	    Intent intent = new Intent(getApplicationContext(), AdsActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("id_partita", partita.getIDPartita());
	    startActivity(intent);
	}
    }

    private void attesa() {
	Button domanda = (Button) findViewById(R.id.contdomanda);
	Button risp1 = (Button) findViewById(R.id.gioca_partita_risp1);
	Button risp2 = (Button) findViewById(R.id.gioca_partita_risp2);
	Button risp3 = (Button) findViewById(R.id.gioca_partita_risp3);
	Button risp4 = (Button) findViewById(R.id.gioca_partita_risp4);
	risp1.setOnClickListener(null);
	risp2.setOnClickListener(null);
	risp3.setOnClickListener(null);
	risp4.setOnClickListener(null);
	risp1.setText("");
	risp2.setText("");
	risp3.setText("");
	risp4.setText("");

	if (domanda_corrente < partita.getNumDomande()) {
	    domanda.setTypeface(Typeface.DEFAULT);
	    domanda.setText(R.string.prossimaDomanda);
	    domanda.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    onClickStart(v);
		}
	    });
	}
	else {
	    domanda.setTypeface(Typeface.DEFAULT);
	    domanda.setText(R.string.invioRisposte);
	    scriviDomanda();
	}
    }

    class clickRisposta implements Button.OnClickListener {
	Domanda domanda;
	float   risposta;

	public clickRisposta(Domanda d, float risposta) {
	    domanda = d;
	    this.risposta = risposta;
	}

	public void onClick(View v) {
	    tempo.interrupt();
	    domanda.setRispostaUtente(this.risposta);
	    domanda_corrente++;
	    attesa();
	}
    }

    class timer_partita extends Thread {
	private ProgressBar progressbar;
	private int	 current_id_sound;
	private SoundPool   sp;

	public timer_partita(ProgressBar bar) {
	    progressbar = bar;
	}

	@Override
	public void interrupt() {
	    if (sp != null && current_id_sound > 0) {
		sp.stop(current_id_sound);
	    }
	    super.interrupt();
	}

	public void run() {
	    int time = DURATA_DOMANDA;
	    int sleep_time = time * 1000;
	    boolean audio_started = false;
	    while (time > 0) {
		try {
		    sleep(100L);
		    sleep_time -= 100;
		}
		catch (InterruptedException e) {
		    e.printStackTrace();
		    return;
		}
		if (sleep_time % 1000 == 0) {
		    time--;
		    audio_started = false;
		}
		if (time == 3) {
		    if (!audio_started) {
			audio_started = true;
			Log.d("MathC_Audio", "time=" + time);
			sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
			current_id_sound = sp.load(getApplicationContext(), R.raw.countdown, 1); // in
												 // 2nd
												 // param
												 // u
												 // have
												 // to
												 // pass
												 // your
												 // desire
												 // ringtone
			sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			    @Override
			    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(sampleId, 1, 1, 1, 0, 1);
			    }
			});
		    }
		}
		progressUpdater pu = new progressUpdater(progressbar, sleep_time);
		runOnUiThread(pu);
	    }
	    // TODO assegna risposta sbagliata
	    runOnUiThread(new Runnable() {
		public void run() {
		    domanda_corrente++;
		    attesa();
		}
	    });
	}
    }

    class progressUpdater implements Runnable {
	int	 value;
	ProgressBar p_bar;

	public progressUpdater(ProgressBar bar, int val) {
	    value = val;
	    p_bar = bar;
	}

	public void run() {
	    p_bar.setProgress(value);
	}
    }

    private void dimensionaBottoni() {
	int h_screen = getResources().getDisplayMetrics().heightPixels;
	int w_screen = getResources().getDisplayMetrics().widthPixels;

	int h_domanda = (h_screen / 100) * 40;
	Button domanda = (Button) findViewById(R.id.contdomanda);
	domanda.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_domanda / 5);
	RelativeLayout.LayoutParams l_domanda = (RelativeLayout.LayoutParams) domanda.getLayoutParams();
	l_domanda.height = h_domanda;

	int w_button = (w_screen / 100) * 45;
	int h_button = (h_screen / 100) * 18;
	Button risp1 = (Button) findViewById(R.id.gioca_partita_risp1);
	risp1.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_button / 3);
	RelativeLayout.LayoutParams l_r1 = (RelativeLayout.LayoutParams) risp1.getLayoutParams();
	l_r1.height = h_button;
	l_r1.width = w_button;
	Button risp2 = (Button) findViewById(R.id.gioca_partita_risp2);
	risp2.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_button / 3);
	RelativeLayout.LayoutParams l_r2 = (RelativeLayout.LayoutParams) risp2.getLayoutParams();
	l_r2.height = h_button;
	l_r2.width = w_button;
	Button risp3 = (Button) findViewById(R.id.gioca_partita_risp3);
	risp3.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_button / 3);
	RelativeLayout.LayoutParams l_r3 = (RelativeLayout.LayoutParams) risp3.getLayoutParams();
	l_r3.height = h_button;
	l_r3.width = w_button;
	Button risp4 = (Button) findViewById(R.id.gioca_partita_risp4);
	risp4.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_button / 3);
	RelativeLayout.LayoutParams l_r4 = (RelativeLayout.LayoutParams) risp4.getLayoutParams();
	l_r4.height = h_button;
	l_r4.width = w_button;
    }
}
