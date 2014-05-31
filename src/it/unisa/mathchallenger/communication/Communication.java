package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.eccezioni.ConnectionException;
import it.unisa.mathchallenger.eccezioni.LoginException;
import it.unisa.mathchallenger.status.AccountUser;
import it.unisa.mathchallenger.status.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Communication implements Runnable {

	private static Communication singleton;

	private Socket			   socket;
	private final static String  HOSTNAME	  = "pinoelefante.no-ip.biz";
	private final static int	 HOSTNAME_PORT = 50000;
	private static int		   TIMEOUT_READ  = 10000;				   // 10
																		   // secondi
																		   // timeout
	private static ThreadPing	t_ping;

	private Communication() {
		super();
		t_ping = ThreadPing.getInstance();
	}

	public static synchronized Communication getInstance() {
		if (singleton == null) {
			singleton = new Communication();
		}
		return singleton;
	}

	public void run() {}

	private OutputStream   out;
	private BufferedReader in;

	public boolean connect() throws UnknownHostException, IOException {
		if (socket == null || socket.isClosed()) {
			socket = new Socket(HOSTNAME, HOSTNAME_PORT);
			socket.setSoTimeout(30000);
			out = socket.getOutputStream();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

		try {
			write(m.getComando());
			m.setResponse(read());
			return;
		}
		catch (IOException e) {
			restart();
			send(m);
		}
		throw new ConnectionException();
	}

	private synchronized String read() throws IOException {
		String r = "";
		long timeout = TIMEOUT_READ + System.currentTimeMillis();
		while ((r = in.readLine()) == null) {
			if (System.currentTimeMillis() > timeout)
				throw new IOException("Timeout error");
		}
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

	private synchronized void write(String s) throws IOException {
		out.write((s + "\n").getBytes());
		Log.d("Communication_W", s);
		out.flush();
	}
}
