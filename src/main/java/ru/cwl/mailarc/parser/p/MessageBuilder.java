package ru.cwl.mailarc.parser.p;



import ru.cwl.mailarc.domain.MessageAddress;
import ru.cwl.mailarc.domain.MessageAttach;
import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.old.parser.ent.MailText;
import ru.cwl.mailarc.parser.old.parser.ent.MailUndefined;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by vad on 12.04.2015 0:21.
 */
/* упрощает набивание данными MessageDigest
* */
class MessageBuilder {
    MessageDigest md;
    StringBuffer sb=new StringBuffer();

    MessageBuilder() {
        md = new MessageDigest();
    }


    MessageBuilder addr(Address[] adressArray, String type) {
        if (adressArray == null) return this;
        InternetAddress inetAdressArray[] = (InternetAddress[]) adressArray;
        for (InternetAddress internetAddress : inetAdressArray) {
            MessageAddress messageAddress = new MessageAddress();
            messageAddress.setAddress(internetAddress.getAddress().toLowerCase());
            messageAddress.setPerson(internetAddress.getPersonal());
          //  messageAddress.setMessageDigest(md);
            messageAddress.setType(type);
            md.getMessageAddresses().add(messageAddress);
        }
        return this;
    }

    MessageBuilder subj(String subj) {
        md.setSubject(subj);
        return this;
    }

    MessageBuilder messageId(String messageId) {
        md.setMessageId(messageId);
        return this;
    }

    MessageBuilder sentDate(Date sentDate) {
        if (sentDate != null) {
            md.setSentDate(new Timestamp(sentDate.getTime()));
        }
        return this;
    }

    public MessageDigest get() {
        md.setPlainText(Util.reduceNoise(sb.toString()));
        return md;
    }

 /*   public void addAttach(MailAttach mailAttach) {


    }*/

    public void addText(MailText mailText) {
        sb.append(mailText.getPlainText());
    }

    public void addUndefined(MailUndefined mailUndefined) {

    }


    public void addAttach(MessageAttach messageAttach) {
        md.getMessageAttachs().add(messageAttach);
        messageAttach.setMessageDigest(md);
    }

    public void reference(String references) {
        md.setReferences(references);
    }
}
