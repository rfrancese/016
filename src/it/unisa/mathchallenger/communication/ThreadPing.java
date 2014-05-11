package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;

import java.io.IOException;

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
			}
			Messaggio m = new Messaggio("ping");
			try {
				comm.send(m);
			}
			catch (IOException | LoginException | ConnectionException e) {
				try {
					comm.restart();
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (LoginException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (ConnectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
