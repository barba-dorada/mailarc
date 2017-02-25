package ru.cwl.mailarc.parser.old.parser;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MsgDumper {

	// static String protocol;
	// static String host = null;
	// static String user = null;
	// static String password = null;
	// static String mbox = null;
	// static String url = null;
	// static int port = -1;
	static boolean verbose = false;
	static boolean debug = false;
	static boolean showStructure = true;
	static boolean showMessage = false;
	static boolean showAlert = false;
	static boolean saveAttachments = false;
	static int attnum = 1;

	public static void dumpPart0(Part p) throws Exception {
		level = 0;
		dumpPart(p);
	}
	public static void dumpPart(Part p) throws Exception {
		if (p instanceof Message) {
			// System.out.println("dumpEnvelope((Message) p)");
		}
		/**
		 * Dump input stream ..
		 * 
		 * InputStream is = p.getInputStream(); // If "is" is not already
		 * buffered, wrap a BufferedInputStream // around it. if (!(is
		 * instanceof BufferedInputStream)) is = new BufferedInputStream(is);
		 * int c; while ((c = is.read()) != -1) System.out.write(c);
		 **/
		pr("** " + p.getContent().getClass().getSimpleName());
		String ct = p.getContentType();
		
		String mimeType="";
		mimeType=new ContentType(ct).getBaseType();
		
		try {
			pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
		} catch (ParseException pex) {
			pr("BAD CONTENT-TYPE: " + ct);
		}
		
		String filename = p.getFileName();
		if (filename != null)
			pr("FILENAME: " + decodeText(filename));
		
		String disposition = p.getDisposition();
		if (disposition != null)
			pr("DISPOSITION: " + decodeText(disposition));



		/*
		 * Using isMimeType to determine the content type avoids fetching the
		 * actual content data until we need it.
		 */
		if (p.isMimeType("text/plain")) {
			pr("__This is plain text______________");
			// if (!showStructure && !saveAttachments)
			String content = "";
			if (p.getHeader("Content-Type") != null
					&& p.getHeader("Content-Type")[0].contains("utf-7")
			// XXX а это ^ корретно? может быть UTF-7
			// .equalsIgnoreCase("text/plain; charset=unicode-1-1-utf-7")
			) {
				p.setHeader("Content-Type", "text/plain; charset=ISO8859-1");
			}
			content = (String) p.getContent();

			System.out.println(content);
			
			// } else if (p.isMimeType("text/html")) {
			// pr("__This is html text______________");
			// // if (!showStructure && !saveAttachments)
			// String content = "";
			// if (p.getHeader("Content-Type") != null
			// && p.getHeader("Content-Type")[0].contains("utf-7")
			// // XXX а это ^ корретно? может быть UTF-7
			// // .equalsIgnoreCase("text/html; charset=unicode-1-1-utf-7")
			// ) {
			// p.setHeader("Content-Type", "text/html; charset=ISO8859-1");
			// }
			// content = (String) p.getContent();
			//
			// System.out.println(content);
			
			
			
		} else if (p.isMimeType("multipart/*")) {
			pr("__This is a Multipart________");
			Multipart mp = (Multipart) p.getContent();
			level++;
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				dumpPart(mp.getBodyPart(i));
			level--;
		} else if (p.isMimeType("message/rfc822")) {
			pr("__This is a Nested Message_________");
			level++;
			// dumpPart((Part) p.getContent());
			level--;
		} else {
			if (!showStructure && !saveAttachments) {
				/*
				 * If we actually want to see the data, and it's not a MIME type
				 * we know, fetch it and check its Java type.
				 */
				Object o = p.getContent();
				if (o instanceof String) {
					pr("__This is a string________");
					System.out.println((String) o);
				} else if (o instanceof InputStream) {
					pr("__This is just an input stream__________");
					InputStream is = (InputStream) o;
					int c;
					while ((c = is.read()) != -1)
						System.out.write(c);
				} else {
					pr("__This is an unknown type______________");
					pr(o.toString());
				}
			} else {
				// just a separator
				pr("---------------------------");
			}
		}

		/*
		 * If we're saving attachments, write out anything that looks like an
		 * attachment into an appropriately named file. Don't overwrite existing
		 * files to prevent mistakes.
		 */
		if (saveAttachments && level != 0 && p instanceof MimeBodyPart
				&& !p.isMimeType("multipart/*")) {
			String disp = p.getDisposition();
			// many mailers don't include a Content-Disposition
			if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
				if (filename == null)
					filename = "Attachment" + attnum++;
				pr("Saving attachment to file " + filename);
				try {
					File f = new File(filename);
					if (f.exists())
						// XXX - could try a series of names
						throw new IOException("file exists");
					((MimeBodyPart) p).saveFile(f);
				} catch (IOException ex) {
					pr("Failed to save attachment: " + ex);
				}
				pr("---------------------------");
			}
		}
	}

	static String decodeText(String src) {

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

	public static void dumpEnvelope(Message m) throws Exception {
		pr("_This is the message envelope___________________");
		// pr("---------------------------");
		Address[] a;
		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				pr("FROM: " + a[j].toString());
		}

		// REPLY TO
		// if ((a = m.getReplyTo()) != null) {
		// for (int j = 0; j < a.length; j++)
		// pr("REPLY TO: " + a[j].toString());
		// }

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++) {
				pr("TO: " + a[j].toString());
				InternetAddress ia = (InternetAddress) a[j];
				if (ia.isGroup()) {
					InternetAddress[] aa = ia.getGroup(false);
					for (int k = 0; k < aa.length; k++)
						pr("  GROUP: " + aa[k].toString());
				}
			}
		}

		// SUBJECT
		pr("SUBJECT: " + m.getSubject());

		// DATE
		Date d = m.getSentDate();
		pr("SendDate: " + (d != null ? d.toString() : "UNKNOWN"));

		// FLAGS
		Flags flags = m.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
			String s;
			Flags.Flag f = sf[i];
			if (f == Flags.Flag.ANSWERED)
				s = "\\Answered";
			else if (f == Flags.Flag.DELETED)
				s = "\\Deleted";
			else if (f == Flags.Flag.DRAFT)
				s = "\\Draft";
			else if (f == Flags.Flag.FLAGGED)
				s = "\\Flagged";
			else if (f == Flags.Flag.RECENT)
				s = "\\Recent";
			else if (f == Flags.Flag.SEEN)
				s = "\\Seen";
			else
				continue; // skip it
			if (first)
				first = false;
			else
				sb.append(' ');
			sb.append(s);
		}

		String[] uf = flags.getUserFlags(); // get the user flag strings
		for (int i = 0; i < uf.length; i++) {
			if (first)
				first = false;
			else
				sb.append(' ');
			sb.append(uf[i]);
		}
		pr("FLAGS: " + sb.toString());

		// X-MAILER
		String[] hdrs = m.getHeader("X-Mailer");
		if (hdrs != null)
			pr("X-Mailer: " + hdrs[0]);
		else
			pr("X-Mailer NOT available");
	}

	static String indentStr = "                                               ";
	static int level = 0;

	/**
	 * Print a, possibly indented, string.
	 */
	public static void pr(String s) {
		if (showStructure)
			System.out.print(indentStr.substring(0, level * 2));
		System.out.println(s);
	}

}
