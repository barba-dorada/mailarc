/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.cwl.mailarc.parser.old.parser.ent;

import javax.mail.MessagingException;
import javax.mail.Part;

/**
 * 
 * @author stanislav.malchenko
 */
public class MailUndefined {

	Part part;

	public MailUndefined(Part part) {

		this.part = part;
	}

	public String getType() {
		try {
			return part.getContentType();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
