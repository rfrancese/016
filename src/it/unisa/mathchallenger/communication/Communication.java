package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Communication implements Runnable {

	private static Communication singleton;

	private Socket			   socket;
	private final static String  HOSTNAME	  = "pinoelefante.no-ip.biz";
	// private final static String HOSTNAME = "192.168.0.207";
	private final static int	 HOSTNAME_PORT = 50000;

	private Communication() {
		super();
	}

	public static synchronized Communication getInstance() {
		if (singleton == null) {
			singleton = new Communication();
		}
		return singleton;
	}

	public void run() {
		if (isConnected())
			return;
		try {
			connect();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PrintWriter	out;
	private BufferedReader in;

	public boolean connect() throws UnknownHostException, IOException {
		if (socket == null || socket.isClosed()) {
			socket = new Socket(HOSTNAME, HOSTNAME_PORT);
			socket.setSoTimeout(30000);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return true;
		}
		return true;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restart() throws UnknownHostException, IOException, LoginException, ConnectionException {
		try {
			send(CommunicationMessageCreator.getInstance().createPingMessage());
		}
		catch (IOException e) {
			close();
			connect();
			AccountUser a = Status.getInstance().getUtente();
			if (a != null) {
				Messaggio relog = CommunicationMessageCreator.getInstance().createLoginAuthcode(a.getID(), a.getAuthCode());
				send(relog);
			}
		}
	}

	public synchronized void send(Messaggio m) throws IOException, LoginException, ConnectionException {
		if (socket == null || socket.isClosed()) {
			connect();
		}
		for (int i = 0; i < 5; i++) {
			try {
				write(m.getComando());
				m.setResponse(read());
				return;
			}
			catch (IOException e) {
				restart();
			}
		}
		throw new ConnectionException();
	}

	private String read() throws IOException {
		String r = "";
		while ((r = in.readLine()) == null);
		Log.d("Communication_R", r);
		return r;
	}

	public boolean isConnected() {
		if (socket != null)
			return true;
		return false;
	}

	public void close() throws IOException {
		if (in != null)
			in.close();
		if (out != null)
			out.close();
		if (socket != null) {
			socket.close();
		}
	}

	private void write(String s) throws IOException {
		OutputStream out = socket.getOutputStream();
		out.write((s + "\n").getBytes());
		Log.d("Communication_W", s);
		out.flush();
	}
}
