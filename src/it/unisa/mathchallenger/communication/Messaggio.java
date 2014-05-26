package it.unisa.mathchallenger.communication;

public class Messaggio {

	private String  comando, response, error_message;
	private boolean error = false;
	private int error_id_string=-1;

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
	public void setErrorID(int i){
		error_id_string=i;
	}
	public int getErrorID(){
		return error_id_string;
	}
}
