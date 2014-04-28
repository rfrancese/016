package it.unisa.mathchallenger.communication;

public class CommunicationMessageCreator {
	private static CommunicationMessageCreator cms;
	public static CommunicationMessageCreator getInstance(){
		if(cms==null)
			cms=new CommunicationMessageCreator();
		return cms;
	}
	private CommunicationMessageCreator() {}
	
	public Messaggio createLoginMessage(String username, String password){
		return new Messaggio("login "+username+" "+password);
	}
	public Messaggio createRegisterMessage(String username, String pass, String email){
		return new Messaggio("register "+username+" "+pass+" "+email);
	}
	public Messaggio createExitMessage(){
		return new Messaggio("exit");
	}
	public Messaggio createResetMessage(String username){
		return new Messaggio("reset-psw "+username);
	}
}
