package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;

import java.io.IOException;

import android.util.Log;

public class ThreadPing extends Thread {

	private static ThreadPing thread;

	private final static long SLEEP_TIME_PING = 20000L; // 20 secondi

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
				sleep(SLEEP_TIME_PING);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			Messaggio m = new Messaggio("ping");
			try {
				Log.d("", "Invio messaggio ping");
				comm.send(m);
			}
			catch (IOException | LoginException | ConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void interrupt() {
		super.interrupt();
		thread=null;
	}
}
