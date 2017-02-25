/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.cwl.mailarc.parser.old.parser.ent;


import ru.cwl.mailarc.parser.old.parser.Html2Text;

import java.io.IOException;
import java.io.StringReader;

//import ru.cwl.parsemail.Html2Text;
//import parsemail.Html2Text;

/**
 * 
 * @author stanislav.malchenko
 */
public class MailText {

	private String text;
	private String contentType;

	public MailText(String text, String contentType) {

		this.text = text;
		this.contentType = contentType;

	}

	public String getText() {
		return text;
	}

	public String getType() {
		if (contentType == null)
			return null;

		if (contentType.contains(";"))
			return contentType.substring(0, contentType.indexOf(";"));
		else
			return contentType;

	}

	public String getPlainText() {
		if (getType() != null) {
			if (getType().equals("text/html")) {

				try {
					StringReader in = new StringReader(text);
					Html2Text parser = new Html2Text();
					parser.parse(in);
					in.close();
					return parser.getText();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (getType().equals("text/plain")) {
				return text;
			}
		}

		return "";
	}

}
