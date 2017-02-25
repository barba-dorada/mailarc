package ru.cwl.mailarc.parser.old.parser.ent;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author stanislav.malchenko
 */
public class MailAddress {

	@Override
	public String toString() {
		return getMailType() + ": " + getMail() + " [" + getPerson() + "]";
	}

	private InternetAddress address;
	private String mailType;

	public MailAddress(String mailType, InternetAddress address) {
		this.mailType = mailType;
		this.address = address;
	}

	public String getMailType() {
		return mailType;
	}

	public String getMail() {
		return decodeText(address.getAddress());
	}

	public String getPerson() {
		return decodeText(address.getPersonal());
	}

	private String decodeText(String etext) {
		String s = "";
		if (etext != null) {
			try {
				s = MimeUtility.decodeText(etext);
			} catch (UnsupportedEncodingException ex) {
				Logger.getLogger(MailAddress.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		return s;
	}
}
