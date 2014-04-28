package it.unisa.mathchallenger.communication;

import it.unisa.mathchallenger.status.AccountUser;

public class CommunicationParser {
	private static CommunicationParser sing;
	
	public static CommunicationParser getInstance(){
		if(sing==null)
			sing=new CommunicationParser();
		return sing;
	}
	private CommunicationParser() {}
	
	public AccountUser parseLogin(Messaggio res){
		String[] properties=res.getResponse().split(";");
		AccountUser acc=null;
		int id = -1;
		String authcode = null;
		boolean loginOK=false;
		for(int i=0;i<properties.length;i++){
			String[] kv=properties[i].split("=");
			switch(kv[0]){
			case "login":
				if(kv[1].compareTo("OK")==0)
					loginOK=true;
				break;
			case "message":
				res.setErrorMessage(kv[1]);
				break;
			case "id":
				id=Integer.parseInt(kv[1]);
				break;
			case "authcode":
				authcode=kv[1];
				break;
			}
		}
		if(loginOK){
			acc=new AccountUser(id, authcode);
		}
		return acc;
	}
	public AccountUser parseRegister(Messaggio res){
		AccountUser acc=null;
		String[] properties=res.getResponse().split(";");
		int id = -1;
		String authcode = null;
		boolean registerOK=false;
		for(int i=0;i<properties.length;i++){
			String[] kv=properties[i].split("=");
			switch(kv[0]){
			case "register":
				if(kv[1].compareTo("OK")==0)
					registerOK=true;
				break;
			case "message":
				res.setErrorMessage(kv[1]);
				break;
			case "id":
				id=Integer.parseInt(kv[1]);
				break;
			case "authcode":
				authcode=kv[1];
				break;
			}
		}
		if(registerOK){
			acc=new AccountUser(id, authcode);
		}
		return acc;
	}
	public boolean parseResetPassword(Messaggio m){
		String[] prop=m.getResponse().split(";");
		for(int i=0;i<prop.length;i++){
			String[] kv=prop[i].split("=");
			switch(kv[0]){
			case "reset-psw":
				if(kv[1].compareTo("OK")==0)
					return true;
				break;
			case "message":
				m.setErrorMessage(kv[1]);
				break;
			}
		}
		return false;
	}
}
