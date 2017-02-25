package ru.cwl.mailarc.parser.old.parser;

import ru.cwl.mailarc.domain.MessageAddress;
import ru.cwl.mailarc.parser.old.parser.ent.MailAttach;
import ru.cwl.mailarc.parser.old.parser.ent.MailText;
import ru.cwl.mailarc.parser.old.parser.ent.MailUndefined;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    MimeMessage mm;
    private List<MailText> textList;
    private List<MailUndefined> undefinedList;
    private List<MailAttach> attachementsList;

    // private List<MailAddr> textList;

    public void setMailMessage(MimeMessage mm) {
        this.mm = mm;
        textList = new ArrayList<MailText>();
        undefinedList = new ArrayList<MailUndefined>();
        attachementsList = new ArrayList<MailAttach>();
    }

    public String getSubject() {
        String res = "";
        try {
            res = decodeWord(mm.getSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Date getDateSent() {
        Date d = null;
        try {
            d = mm.getSentDate();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            d = null;
        }
        if (d == null || (d.getYear() < 0)) {
            d = new Date();
        }

        return d;
    }

    public List<MessageAddress> getAddr() {
        List<MessageAddress> addrList = new ArrayList<MessageAddress>();

        for (MailAddressType type : MailAddressType.values()) {

            String header = "";

            String name = type.getVal();

            // Исправление имен некорректных адресов
            try {
                header = mm.getHeader(name, ",");
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // System.out.println(f.getName());
                System.out.println(header);
                System.out.println(name);
                e.printStackTrace();
                // i++;
                continue;
            }
            if (header == null)
                continue;
            header = header.replace("'", ""); // 25->19
            InternetAddress[] address = null;
            try {
                address = InternetAddress.parseHeader(header, true);
            } catch (AddressException e) {
                System.out.println(header);
                e.printStackTrace();
                continue;
            }

            for (InternetAddress a : address) {
                MessageAddress ma = new MessageAddress();
                ma.setType(type.getVal());
                ma.setAddress(a.getAddress());
                ma.setPerson(decodeWord(a.getPersonal()));
                addrList.add(ma);
            }

        }
        return addrList;
    }

    String decodeWord(String src) {
        return Util.decodeWord(src);
    }

    public String getMessageId() {
        try {
            return mm.getMessageID();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String getMessageText() {
        try {
            parseMultiPart(mm);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s = "";
        for (MailText mt : textList) {
            s += mt.getPlainText();
        }
        return s;

    }

    private void parseMultiPart(Part message) throws IOException,
            MessagingException {

        if (isMultiPart(message)) {

            Multipart multipart = (Multipart) message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {

                Part part = multipart.getBodyPart(i);
                try {
                    parsePart(part, i);
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getStackTrace());
                }
            }
        } else {
            parsePart(message, -1);
        }

    }

    private void parsePart(Part part, int multipartIndex)
            throws MessagingException, IOException,
            UnsupportedEncodingException {

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
                attachementsList.add(new MailAttach(part, multipartIndex));
                return;
            } else if (disposition.equals(Part.INLINE)) {
                return;
            }
        }

        if (part.isMimeType("text/plain")) {
            // System.out.println(part.getHeader("Content-Type")[0]);
            // исправления для неподдерживаемой кодировки
            // charset=unicode-1-1-utf-7
            if (part.getHeader("Content-Type") != null
                    && part.getHeader("Content-Type")[0]
                    .equalsIgnoreCase("text/plain; charset=unicode-1-1-utf-7")) {
                part.setHeader("Content-Type", "text/plain; charset=ISO8859-1");
            }
            // конец подпорки

            textList.add(new MailText((String) part.getContent(), part
                    .getContentType()));

        } else if (part.isMimeType("text/html")) {

            textList.add(new MailText((String) part.getContent(), part
                    .getContentType()));

        } else if (part.isMimeType("application/ms-tnef")) {

            undefinedList.add(new MailUndefined(part));

            // try{
            // FileInputStream fin = new
            // FileInputStream("C:/workspace/test.rtf");
            // InputStream in = part.getInputStream();
            //
            // //creating a default blank styled document
            // DefaultStyledDocument styledDoc = new DefaultStyledDocument();
            //
            // //Creating a RTF Editor kit
            // RTFEditorKit rtfKit = new RTFEditorKit();
            //
            // //Populating the contents in the blank styled document
            // rtfKit.read(in,styledDoc,0);
            //
            //
            // // Getting the root document
            // Document doc = styledDoc.getDefaultRootElement().getDocument();
            // System.out.println(styledDoc.getText(0, styledDoc.getLength()));
            // System.out.println(doc.getText(0, doc.getLength()));
            //
            // }catch(BadLocationException e)
            // {
            // e.printStackTrace();
            // }

        } else if (part.isMimeType("multipart/*")) {

            parseMultiPart(part);

        } else {
            undefinedList.add(new MailUndefined(part));

        }

        return;
    }

    private boolean isMultiPart(Part part) throws MessagingException {
        return part.isMimeType("multipart/*");
    }

    public int getSize() {
        try {
            return mm.getSize();
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
