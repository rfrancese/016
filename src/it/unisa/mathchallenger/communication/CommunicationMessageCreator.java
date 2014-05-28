package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.status.Partita;

public class CommunicationMessageCreator {

	private static CommunicationMessageCreator cms;

	public static CommunicationMessageCreator getInstance() {
		if (cms == null)
			cms = new CommunicationMessageCreator();
		return cms;
	}

	private CommunicationMessageCreator() {}

	public Messaggio createLoginMessage(String username, String password) {
		return new Messaggio("login " + username + " " + password);
	}

	public Messaggio createRegisterMessage(String username, String pass, String email) {
		return new Messaggio("register " + username + " " + pass + " " + email);
	}

	public Messaggio createExitMessage() {
		return new Messaggio("exit");
	}

	public Messaggio createResetMessage(String username) {
		return new Messaggio("reset-psw " + username);
	}

	public Messaggio createLoginAuthcode(int id, String auth) {
		return new Messaggio("login-authcode " + id + " " + auth);
	}

	public Messaggio createLogoutMessage() {
		return new Messaggio("logout");
	}

	public Messaggio createChangePasswordMessage(String oldPass, String newPass) {
		return new Messaggio("change-psw " + oldPass + " " + newPass);
	}

	public Messaggio createSearchUserMessage(String username_utente) {
		return new Messaggio("search-user " + username_utente);
	}

	public Messaggio createNewGameMessage(int id_utente) {
		return new Messaggio("newgame " + id_utente);
	}

	public Messaggio createGetPartiteInCorso() {
		return new Messaggio("getPartiteInCorso");
	}

	public Messaggio createNewGameRandom() {
		return new Messaggio("newgame-random");
	}

	public Messaggio createAbandonGame(int id) {
		return new Messaggio("abandon " + id);
	}

	public Messaggio createPingMessage() {
		return new Messaggio("ping");
	}

	public Messaggio createAggiungiAmico(int id_amico) {
		return new Messaggio("addfriend " + id_amico);
	}

	public Messaggio createRimuoviAmico(int id_amico) {
		return new Messaggio("removefriend " + id_amico);
	}

	public Messaggio createGetMyFriends() {
		return new Messaggio("getMyFriends");
	}

	public Messaggio createGetDettagliPartita(int idP) {
		return new Messaggio("getDettagliPartita " + idP);
	}

	public Messaggio createGetDomande(int idP) {
		return new Messaggio("getDomande " + idP);
	}

	public Messaggio createRisposte(Partita partita) {
		String ris = "answer " + partita.getIDPartita();
		for (int i = 0; i < partita.getNumDomande(); i++) {
			ris += " " + partita.getDomanda(i).getRispostaUtente();
		}
		return new Messaggio(ris);
	}
	public Messaggio createGetStatistiche(){
		return new Messaggio("getStatistiche");
	}
	public Messaggio createIsValidVersion(int v){
		return new Messaggio("validateVersion "+v);
	}
}
