package ru.cwl.mailarc.parser;

import ru.cwl.mailarc.domain.MessageAddress;
import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.old.parser.Parser2;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;

/**
 * Created by vadim.tishenko
 * on 22.02.2017 15:24 20:07.
 */
public class Parser implements IParser {
    private final Parser2 ph;
    public Parser(){
        ph = new Parser2();
    }

    public MessageDigest parse(MimeMessage m) throws MessagingException {
        MessageDigest res=new MessageDigest();
        ph.setMailMessage(m);
        ph.parse();
        for (Parser2.Address address : ph.getAddressList()) {
            MessageAddress ma=new MessageAddress();
            ma.setAddress(address.getAddress());
            ma.setPerson(address.getName());
            ma.setType(address.getType());
            res.getMessageAddresses().add(ma);
        }
        for (Parser2.Attach attach : ph.getAttachList()) {
            //res.setMessageAttachs(ph.getAttachList());
        }
        res.setMessageId(ph.getMessageId());
        res.setSentDate(new Timestamp(ph.getSentDate().getTime()));
        res.setSubject(ph.getSubject());
        res.setPlainText(ph.getText());
        res.setReferences(ph.getReferences());

        return res;
    }
}
