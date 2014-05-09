package it.unisa.mathchallenger.status;

import it.unisa.mathchallenger.communication.Communication;

public class ShutdownThread extends Thread {
	public void run() {
		System.out.println("Sto eseguendo il thread di shutdown");
		Account acc=Status.getInstance().getUtente();
		if(acc!=null){
			Communication com=Communication.getInstance();
			com.disconnect();
			System.runFinalization();
		}
	}
}
