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
	public Messaggio createLoginAuthcode(int id, String auth){
		return new Messaggio("login-authcode "+id+" "+auth);
	}
	public Messaggio createLogoutMessage(){
		return new Messaggio("logout");
	}
	public Messaggio createChangePasswordMessage(String pass){
		return new Messaggio("change-psw "+pass);
	}
	public Messaggio createSearchUserMessage(String username_utente){
		return new Messaggio("search-user "+username_utente);
	}
	public Messaggio createNewGameMessage(int id_utente){
		return new Messaggio("newgame "+id_utente);
	}
}
