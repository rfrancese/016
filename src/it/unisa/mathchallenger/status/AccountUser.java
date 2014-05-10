
package it.unisa.mathchallenger.status;

public class AccountUser extends Account {

	private String authcode;

	public AccountUser(int id, String auth) {
		super(id);
		setAuthCode(auth);
	}

	public String getAuthCode() {
		return authcode;
	}

	public void setAuthCode(String a) {
		authcode = a;
	}
}
