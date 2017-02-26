package ru.cwl.mailarc.parser;

import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.p.Parser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by vadim.tishenko
 * on 22.02.2017 18:35 20:07.
 */
public class OldParser implements IParser {

    ru.cwl.mailarc.parser.p.Parser p = new Parser();

    @Override
    public MessageDigest parse(MimeMessage m) throws MessagingException {
        return p.parse(m);
    }
}
