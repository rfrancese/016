package it.unisa.mathchallenger.status;

public class Account {

	private int	id;
	private String nomeutente;

	public Account(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public String getUsername() {
		return nomeutente;
	}

	public void setUsername(String u) {
		nomeutente = u;
	}

	public void setID(int i) {
		id = i;
	}
}
