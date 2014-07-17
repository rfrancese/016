package it.unisa.mathchallenger.status;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Classifica {
    private static String URLClassifica = "http://pinoelefante.altervista.org/rank/getRankXML.php";

    class EntryClassifica {
	private String username;
	private int    punti;

	public EntryClassifica(String u, int p) {
	    username = u;
	    punti = p;
	}

	public String getUsername() {
	    return username;
	}

	public int getPunti() {
	    return punti;
	}
    }

    private ArrayList<EntryClassifica> classifica;
    private static Classifica	  instance;

    public static Classifica getInstance() {
	if (instance == null)
	    instance = new Classifica();
	return instance;
    }

    private Classifica() {
	classifica = new ArrayList<EntryClassifica>(50);
    }

    public void loadClassifica() {
	DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder domparser = null;
	try {
	    domparser = dbfactory.newDocumentBuilder();
	    Document doc = domparser.parse(URLClassifica);
	    NodeList entries = doc.getElementsByTagName("entry");
	    if (entries.getLength() > 0)
		classifica.clear();
	    for (int i = 0; i < entries.getLength(); i++) {
		Node entry = entries.item(i);
		NodeList attrs = entry.getChildNodes();
		String user = "";
		int p = 0;
		for (int j = 0; j < attrs.getLength(); j++) {
		    Node att = attrs.item(j);
		    if (att instanceof Element) {
			Element e = (Element) att;
			if (e.getTagName().compareTo("user") == 0) {
			    user = e.getTextContent();
			}
			else if (e.getTagName().compareTo("punti") == 0) {
			    p = Integer.parseInt(e.getTextContent());
			}
		    }
		}
		EntryClassifica ec = new EntryClassifica(user, p);
		classifica.add(ec);
	    }
	}
	catch (ParserConfigurationException | SAXException | IOException e) {
	    e.printStackTrace();
	}
    }

    public int getNumeroUtenti() {
	return classifica.size();
    }

    public String getUsernameAtIndex(int i) {
	return classifica.get(i).getUsername();
    }

    public int getPuntiAtIndex(int i) {
	return classifica.get(i).getPunti();
    }

    public static void setURLClassifica(String p) {
	URLClassifica = p;
    }
}
