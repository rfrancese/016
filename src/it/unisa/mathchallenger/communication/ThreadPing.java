package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;

import java.io.IOException;

import android.util.Log;

public class ThreadPing extends Thread {

	private static ThreadPing thread;

	private static long SLEEP_TIME = 20000L; // 20 secondi (tempo per non disconnettersi)
	private final static long SLEEP_TIME_PING = 20000L; // 20 secondi (tempo per non disconnettersi)
	private static int ping_auto;
	private final static int PING_SENZA_INTERAZIONE_LIMIT=10;

	public static ThreadPing getInstance() {
		if (thread == null)
			thread = new ThreadPing();
		return thread;
	}

	private ThreadPing() {}

	public void run() {
		Communication comm = Communication.getInstance();
		while (true) {
			try {
				sleep(SLEEP_TIME);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			if(System.currentTimeMillis()-Communication.getLastWriteTime()>SLEEP_TIME_PING){
				Messaggio m = new Messaggio("ping");
				try {
					ping_auto++;
					Log.d("", "Invio messaggio ping");
					comm.send(m);
				}
				catch (IOException | LoginException | ConnectionException e) {
					e.printStackTrace();
				}
			}
			else {
				SLEEP_TIME = 20000L;
				ping_auto=0;
			}
			if(ping_auto>=PING_SENZA_INTERAZIONE_LIMIT){
				if(SLEEP_TIME<50000L){
					SLEEP_TIME+=2500L;
				}
			}
		}
	}
	@Override
	public void interrupt() {
		super.interrupt();
		thread=null;
	}
}
