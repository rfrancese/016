
package it.unisa.mathchallenger.communication;

public class Messaggio {

	private String comando, response, error_message;
	private boolean error = false;

	public Messaggio(String m) {
		comando = m;
	}

	public String getComando() {
		return comando;
	}

	public void setResponse(String r) {
		response = r;
	}

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		if (error_message == null)
			return "";
		else
			return error_message;
	}

	public void setErrorMessage(String e) {
		error = true;
		error_message = e;
	}

	public boolean hasError() {
		return error;
	}
}
