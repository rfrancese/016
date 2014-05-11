package it.unisa.mathchallenger.eccezioni;


public class DettagliNonPresentiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DettagliNonPresentiException() {
		// TODO Auto-generated constructor stub
	}

	public DettagliNonPresentiException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public DettagliNonPresentiException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public DettagliNonPresentiException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}
