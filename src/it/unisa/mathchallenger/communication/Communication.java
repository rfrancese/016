package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Communication extends Thread {

	private static Communication singleton;

	private Socket			   socket;
	private final static String  HOSTNAME	  = "54.76.113.193"; //ip del server
	//private final static String  HOSTNAME	  = "mathchallenger.servegame.com";
	//private final static String  HOSTNAME	  = "5.231.68.209"; //host1free
	private final static int	 HOSTNAME_PORT = 50000;
	private static int		   TIMEOUT_READ  = 10000;		// 10 secondi timeout
	private static ThreadPing	t_ping;
	private static long last_write;
	
	private static boolean restart_connection=false;

	private Communication() {
		super();
		t_ping = ThreadPing.getInstance();
	}
	
	private static boolean canAccess=true;
	public static synchronized Communication getInstance() {
		while(!canAccess){} //sincronizzazione primitiva
		canAccess=false;
		if(singleton==null){
			singleton = new Communication();
		}
		canAccess=true;
		return singleton;
	}

	public void run() {}

	private OutputStream   out;
	private BufferedReader in;
	private InputStream input;

	public synchronized boolean connect() throws UnknownHostException, IOException {
		if (socket == null || socket.isClosed()) {
			socket = new Socket(HOSTNAME, HOSTNAME_PORT);
			socket.setSoTimeout(TIMEOUT_READ);
			out = socket.getOutputStream();
			input=socket.getInputStream();
			in = new BufferedReader(new InputStreamReader(input));
			if(t_ping==null)
				t_ping=ThreadPing.getInstance();
			if (t_ping != null && !t_ping.isAlive())
				t_ping.start();
			return true;
		}
		return false;
	}

	public void disconnect() {
		try {
			send(CommunicationMessageCreator.getInstance().createExitMessage());
			in.close();
			out.close();
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (LoginException e) {
			e.printStackTrace();
		}
		catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public synchronized void restart() throws UnknownHostException, IOException, LoginException, ConnectionException {
		Log.d("", "Riavvio della connessione");
		if(t_ping!=null)
			t_ping.interrupt();
		t_ping=null;
		close();
		connect();
		restart_connection=false;
		AccountUser a = Status.getInstance().getUtente();
		if (a != null) {
			Messaggio valida_versione = CommunicationMessageCreator.getInstance().createIsValidVersion(Status.CURRENT_VERSION);
			write(valida_versione.getComando());
			valida_versione.setResponse(read());
			if(CommunicationParser.getInstance().parseValidateVersion(valida_versione)){
				Messaggio relog = CommunicationMessageCreator.getInstance().createLoginAuthcode(a.getID(), a.getAuthCode());
				write(relog.getComando());
				relog.setResponse(read());
				if(!CommunicationParser.getInstance().parseLoginAuthcode(relog))
					throw new LoginException("Errore durante il relog");
			}
		}
	}

	public synchronized void send(Messaggio m) throws IOException, LoginException, ConnectionException {
		if(restart_connection)
			restart();
		else if (socket == null || socket.isClosed()) {
			connect();
		}

		for(int i=0;i<5;i++){
			try {
				write(m.getComando());
				m.setResponse(read());
				return;
			}
			catch (IOException e) {
				try {
					restart();
				}
				catch(IOException e1){
					e.printStackTrace();
					restart_connection=true;
				}
			}
		}
		throw new ConnectionException();
	}

	private synchronized String read() throws IOException {
		if(in==null)
			throw new IOException("ConnectionReader null");
		String r = "";
		long timeout = TIMEOUT_READ + System.currentTimeMillis();
		while(input.available()==0){
			if (System.currentTimeMillis() > timeout)
				throw new IOException("Timeout error");
		}
		if((r = in.readLine()) == null)
			throw new IOException("String null");
		
		Log.d("Communication_R", r);
		return r;
	}

	public boolean isConnected() {
		if (socket != null)
			return true;
		return false;
	}

	public void close() throws IOException {
		if (in != null){
			in.close();
			in=null;
		}
		if (out != null){
			out.close();
			out=null;
		}
		if (socket != null) {
			socket.close();
			socket=null;
		}
	}

	private synchronized void write(String s) throws IOException {
		if(out==null)
			throw new IOException("ConnectionWriter null");
		out.write((s + "\n").getBytes());
		Log.d("Communication_W", s);
		out.flush();
		last_write=System.currentTimeMillis();
	}
	public static long getLastWriteTime(){
		return last_write;
	}
}
