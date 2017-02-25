package ru.cwl.mailarc.parser.p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cwl.mailarc.domain.MessageAttach;
import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.old.parser.ent.MailAttach;
import ru.cwl.mailarc.parser.old.parser.ent.MailText;
import ru.cwl.mailarc.parser.old.parser.ent.MailUndefined;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by vad on 09.04.2015 0:53.
 * преобразует из текста в типы для обработки,хранения,
 * локализует MessageException
 */
public class Parser {
    static Logger log = LoggerFactory.getLogger(Parser.class);

    public MessageDigest parse(MimeMessage mm) throws MessagingException {
        //TODO add parse body
        //TODO add parse attach
        //TODO parse sent time with time zone (toZonedDateTime)
        //TODO target: parse 30000 without exceptions.
        //TODO add logging

        MessageBuilder mb = new MessageBuilder();
        /// try {
        mb.subj(mm.getSubject());
        mb.messageId(mm.getMessageID());
        mb.sentDate(mm.getSentDate());


        mb.addr(mm.getFrom(), "FROM");
        for (RecType type : RecType.values()) {
            mb.addr(getRecipients(mm, type), type.name());
        }

        try {
            parseBody(mm, mb);
        } catch (IOException e) {
            throw new MessagingException("IO", e);
        }
        mb.sentDate(mm.getSentDate());
        mb.reference(getReferences(mm));
        MessageDigest messageDigest = mb.get();
        return messageDigest;
    }

    public String getReferences(MimeMessage m) throws MessagingException {
        String references = "";
        String[] res = m.getHeader("References");
        if (res != null && res.length > 0 && res[0] != null) {
            references = res[0];
        }
        return references;
    }

    private void parseBody(MimeMessage mm, MessageBuilder mb) throws IOException, MessagingException {
        parseMultiPart(mm, mb);
    }

    private void parseMultiPart(Part message, MessageBuilder mb) throws IOException,
            MessagingException {

        if (isMultiPart(message)) {

            Multipart multipart = (Multipart) message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                Part part = multipart.getBodyPart(i);
                parsePart(part, i, mb);
            }
        } else {
            parsePart(message, -1, mb);
        }

    }

    private void parsePart(Part part, int multipartIndex, MessageBuilder mb)
            throws MessagingException, IOException {

        // text/plain
        // text/html
        // application/msword
        // image/jpeg
        // application/ms-tnef
        // application/vnd.ms-excel
        // video/x-ms-wmv
        // video/mpeg
        // message/delivery-status
        // message/rfc822
        // text/rfc822-headers

        String[] headers = part.getHeader("Content-Disposition");

        // if ((headers = getCorrectDisposition(headers)) != null) {
        // for (String rightDisposition : headers) {
        // part.setHeader("Content-Disposition", rightDisposition);
        // }
        // }

        String disposition = part.getDisposition();

        if (disposition != null) {
            if (disposition.equals(Part.ATTACHMENT)) {

                MailAttach mailAttach = new MailAttach(part, multipartIndex);
                MessageAttach messageAttach = new MessageAttach();
                messageAttach.setLength((long) mailAttach.getSize());
                messageAttach.setName(mailAttach.getFileName());
                mb.addAttach(messageAttach);
                //mb.addAttach(mailAttach);
                return;
            } else if (disposition.equals(Part.INLINE)) {
                return;
            }
        }

        if (part.isMimeType("text/plain")) {
            patchCharset(part);
            mb.addText(new MailText((String) part.getContent(), part.getContentType()));
        } else if (part.isMimeType("text/html")) {  // TODO add 'garbage' filter (<tag>, =XX, long(>50) words)
            mb.addText(new MailText((String) part.getContent(), part.getContentType()));
        } else if (part.isMimeType("application/ms-tnef")) {
            mb.addUndefined(new MailUndefined(part));
        } else if (part.isMimeType("multipart/*")) {
            parseMultiPart(part, mb);
        } else {
            mb.addUndefined(new MailUndefined(part));
        }

        return;
    }

    private void patchCharset(Part part) throws MessagingException {
        // System.out.println(part.getHeader("Content-Type")[0]);
        // исправления для неподдерживаемой кодировки
        // charset=unicode-1-1-utf-7
        if (part.getHeader("Content-Type") != null
                && part.getHeader("Content-Type")[0]
                .equalsIgnoreCase("text/plain; charset=unicode-1-1-utf-7")) {
            part.setHeader("Content-Type", "text/plain; charset=ISO8859-1");
        }
        // конец подпорки
    }

    private boolean isMultiPart(Part part) throws MessagingException {
        return part.isMimeType("multipart/*");
    }

    private Address[] getRecipients(MimeMessage mm, RecType type) throws MessagingException {
        String s = mm.getHeader(type.recipientType.toString(), ",");
        if (s == null) return null;
        s = s.replaceAll("'", "");
        return InternetAddress.parseHeader(s, false); //<- no strict
    }

    /*private void onErrorAction(byte[] buf, MessagingException e) {
        // log.error("parse error!", e);
        //TODO add logging and collect "bad" msg
        // System.getenv("");
        String dir = "C:\\dev\\projects\\mailarc\\src\\test\\resources\\badmsg\\";
        String fileName = CRC.crcSHA1(buf) + ".msg";
        File file = new File(dir + fileName);
        if (!file.exists()) {
            try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
                fileOuputStream.write(buf);
                fileOuputStream.close();
            } catch (IOException e1) {
                log.error("error on write msg", e1);
                e1.printStackTrace();
            }
        }
        //throw new RuntimeException("error in parser!!!", e);
    }*/

    enum RecType {
        TO(Message.RecipientType.TO),
        CC(Message.RecipientType.CC),
        BCC(Message.RecipientType.BCC);

        Message.RecipientType recipientType;

        RecType(Message.RecipientType recipientType) {
            this.recipientType = recipientType;
        }
    }

}

