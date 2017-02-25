package ru.cwl.mailarc.parser.old.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyFilter {

	// эти два паттерна чистят до трети тела письма
	Pattern base64pattern = Pattern.compile("[a-zA-Z0-9+/]{5,}={0,2}");
	Pattern spPatt = Pattern.compile("\\s{2,}");

	// а тут пока не понял.... quoted-printable strings -
	Pattern en = Pattern.compile("=[0-9a-fA-F]{2}");

	public String filter(String src) {
		String result = base64filter(src);
		result=enFilter(result);
		result = spFilter(result);
		return result;
	}

	String base64filter(String src) {
		Matcher matcher = base64pattern.matcher(src);
		String result = matcher.replaceAll("");
		return result;
	}

	String spFilter(String src) {
		Matcher matcher = spPatt.matcher(src);
		String result = matcher.replaceAll("");
		return result;
	}

	String enFilter(String src) {
		Matcher matcher = en.matcher(src);
		String result = matcher.replaceAll("");
		return result;
	}

}
