package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;

import java.io.IOException;

import android.util.Log;

public class ThreadPing extends Thread {
	private static ThreadPing thread;
	public static ThreadPing getInstance(){
		if(thread==null)
			thread=new ThreadPing();
		return thread;
	}
	private ThreadPing(){}
	public void run() {
		Communication comm=Communication.getInstance();
		while(true){
			Log.d("", "ThreadPing");
			try {
				sleep(10000L);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			Messaggio m=new Messaggio("ping");
			try {
				comm.send(m);
			} 
			catch (IOException | LoginException | ConnectionException e) {
				e.printStackTrace();
			}
		}
	}
}
