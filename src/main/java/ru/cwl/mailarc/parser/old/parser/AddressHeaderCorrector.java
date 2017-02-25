/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.cwl.mailarc.parser.old.parser;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Stas
 */

public class AddressHeaderCorrector {

	private String header;

	public AddressHeaderCorrector(String addressHeader) {

		setHeader(addressHeader);

	}

	public void setHeader(String addressHeader) {
		this.header = addressHeader;
	}

	public String getHeader() throws UnsupportedEncodingException,
            MessagingException {
		return getCorrectAddressHeader(header);
	}

	private String getCorrectAddressHeader(String sourceHeader)
			throws UnsupportedEncodingException, MessagingException {

		// addressHeader = decodeText(addressHeader, addressHeader);
		if (sourceHeader == null)
			return "";

		// Шаблон для опредления в строке email адреса

		// Если адрес записан с начала строки в кавычках, то для корректного
		// распознавания кавычки в header'е следует убрать.
		Pattern pattern = Pattern.compile("^<.*?>$");
		Matcher matcher = pattern.matcher(sourceHeader);

		if (matcher.matches()) {
			sourceHeader = sourceHeader.replace("<", "").replace(">", "");

		}

		String[] addressEntry = getAddressEntries(sourceHeader);

		String header = "";

		for (String entry : addressEntry) {

			String prefix = (!header.equals("")) ? ", " : "";

			String email = "";
			String person = "";

			person = getAddressEntryPerson(entry);
			email = getAddressEntryEmail(entry);

			if (email != null)
				if (person != null)
					header += prefix + "\"" + person + "\" <" + email + ">";
				else
					header += prefix + email;
		}
		return header;
	}

	private String getCorrectEmailAddress(String addressEntry) {
		// TODO Добавить проверку на наличие недопустимых символов в адресе, и
		// если они есть вернуть null

		addressEntry = addressEntry.replace("<", "").replace(">", "");

		Pattern correctSymbolsOnlyPattern = Pattern
				.compile("[\\w\\d@-_\\:\\.]*");
		Matcher matcher = correctSymbolsOnlyPattern.matcher(addressEntry);

		// Если встретится хоть один некорректный символ в email адресе
		if (!matcher.matches())
			return null;

		Pattern emailPattern = Pattern
				.compile("[\\w\\d-_.]+@[\\w\\d-]+\\.[\\w\\d-]+[.[\\w\\d-]]+");

		Matcher emailMatcher = emailPattern.matcher(addressEntry);

		if (emailMatcher.find()) {
			return emailMatcher.group(0);
		}
		return null;
	}

	private String[] getAddressEntries(String header) {
		return header.split(",");
	}

	private String getAddressEntryEmail(String entry) {
		String email;

		if (entry.contains(" "))
			email = getCorrectEmailAddress(entry.substring(
					entry.lastIndexOf(" ")).trim());
		else
			email = getCorrectEmailAddress(entry);

		return email;
	}

	private String getAddressEntryPerson(String entry) {
		if (entry.contains(" "))
			return entry.substring(0, entry.lastIndexOf(" ")).trim()
					.replace("\"", "");
		else
			return null;
	}

	public String getOnlyCorrectCharsHeader() {
		return removeIncorrectCharEntries(header);
	}

	private String removeIncorrectCharEntries(String sourceHeader) {

		if (sourceHeader == null)
			return "";

		String header = "";

		String[] addressEntry = getAddressEntries(sourceHeader);

		for (String entry : addressEntry) {

			String prefix = (!header.equals("")) ? ", " : "";
			boolean hasBadCharacter = false;
			String email;

			if (entry.trim().contains(" "))
				email = entry.substring(entry.lastIndexOf(" ")).trim();
			else
				email = entry.trim();

			if (email != null) {
				for (int i = 0; i < email.length(); i++) {
					char c = email.charAt(i);

					if (c <= 040 || c >= 0177) {
						hasBadCharacter = true;
						break;
					}
				}

				if (!hasBadCharacter)
					header += prefix + entry.trim();
			}
		}
		return header;
	}

}
