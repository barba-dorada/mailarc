package ru.cwl.mailarc.parser.old.parser;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser2 {
    MimeMessage m;

    // envelope fields
    Date sentDate;
    String subject;
    String messageId;
    List<Address> addressList;

    public Date getSentDate() {
        return sentDate;
    }

    public String getSubject() {
        return subject;
    }

    // body fields
    String text;
    List<Attach> attachList;

    private boolean envelopeParsed = false;
    private boolean bodyParsed = false;

    public Parser2() {

    }

    public void setMailMessage(MimeMessage mm) {
        m = mm;
        clear();

    }

    private void clear() {
        sentDate = null;
        subject = null;
        messageId = null;
        addressList = null;
        text = null;
        attachList = null;
        envelopeParsed = false;
        bodyParsed = false;
    }

    public String getMessageId() {
        return messageId;
    }

    public List<Address> getAddressList() {
        return addressList;
    }


    public String getText() {
        return text;
    }

    public List<Attach> getAttachList() {
        return attachList;
    }


    public void parse() throws MessagingException {
        parseEnvelope();
        parseBody();
    }

    public void parseEnvelope() throws MessagingException {
        sentDate = parseSentDate();
        subject = parseSubject();
        messageId = parseMessageID();
        addressList = parseAddresses();

        envelopeParsed = true;
    }


    private List<Address> parseAddresses() throws MessagingException {
        // from...
        List<Address> al = new ArrayList<Address>();

        javax.mail.Address[] a;
        a = m.getFrom();
        if ((a) != null) {
            for (int j = 0; j < a.length; j++) {
                InternetAddress ia = (InternetAddress) a[j];
                al.add(new Address("from", ia.getAddress(), ia.getPersonal()));
            }
        }

        ext(al, Message.RecipientType.TO);
        ext(al, Message.RecipientType.CC);
        ext(al, Message.RecipientType.BCC);
        return al;
    }

    private void ext(List<Address> al, Message.RecipientType type) throws MessagingException {
        javax.mail.Address[] a = null;
        a = m.getRecipients(type);
        if (a != null) {
            for (int j = 0; j < a.length; j++) {
                InternetAddress ia = (InternetAddress) a[j];
                al.add(new Address(type.toString(), ia.getAddress(),
                        ia.getPersonal()));
            }
        }
    }


    private String parseMessageID() throws MessagingException {
        return (m.getMessageID());
    }

    private String parseSubject() throws MessagingException {
        return (Util.decodeWord(m.getSubject()));
    }

    private Date parseSentDate() throws MessagingException {
        return m.getSentDate();
    }


    public void parseBody() {

        bodyParsed = true;
        attachList = new ArrayList<>();
    }

    public String getReferences() throws MessagingException {
        String references = "";
            String[] res = m.getHeader("References");
            if (res != null && res.length > 0 && res[0] != null) {
                references = res[0];
            }
        return references;
    }

    public class Address {

        String type;
        String address;
        String name;

        public Address(String type, String address, String name) {
            if (type == null)
                throw new NullPointerException();
            if (address == null)
                throw new NullPointerException();
            this.type = type;
            this.address = address;
            this.name = name;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (type != null) {
                builder.append(type);
                builder.append(":");
            }
            if (address != null) {
                builder.append(address);
            }
            if (name != null) {
                builder.append("(");
                builder.append(name);
                builder.append(")");
            }
            return builder.toString();
        }

        public String getType() {
            return type;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }
    }

    public class Attach {
        String name;
        String mimeType;
        // or
        MimePart part;

    }

}
