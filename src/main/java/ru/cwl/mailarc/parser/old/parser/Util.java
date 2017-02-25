package ru.cwl.mailarc.parser.old.parser;

import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

class Util {
	static String decodeWord(String src) {

		String s = src;
		int count = 10;
		if (s == null)
			return "";
		s = s.replaceAll("[\\r\\n]", "");
		try {

			// допиливаем сабжект.....
			int beginIndex, endIndex;
			do {
				endIndex = s.lastIndexOf("?=");
				beginIndex = s.lastIndexOf("=?", endIndex - 2);
				if (beginIndex != -1 && endIndex > beginIndex) {
					String codedText = s.substring(beginIndex, endIndex + 2);
					// есть некорректные строки в =XX кодировке с пробелами
					if (codedText.indexOf("?Q?=") != -1) {
						codedText = codedText.replace(" ", "=20");
					}
					String decodetText = MimeUtility.decodeText(codedText);

					s = s.substring(0, beginIndex) + decodetText
							+ s.substring(endIndex + 2);

				}
				count--;
				if (count == 0)
					return s + " !!!count";
			} while (endIndex != -1);
			// return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

}
