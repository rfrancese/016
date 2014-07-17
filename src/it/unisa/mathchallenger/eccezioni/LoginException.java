package it.unisa.mathchallenger.eccezioni;

public class LoginException extends Exception {

    private static final long serialVersionUID = 1L;

    public LoginException() {
	// TODO Auto-generated constructor stub
    }

    public LoginException(String detailMessage) {
	super(detailMessage);
	// TODO Auto-generated constructor stub
    }

    public LoginException(Throwable throwable) {
	super(throwable);
	// TODO Auto-generated constructor stub
    }

    public LoginException(String detailMessage, Throwable throwable) {
	super(detailMessage, throwable);
	// TODO Auto-generated constructor stub
    }

}
