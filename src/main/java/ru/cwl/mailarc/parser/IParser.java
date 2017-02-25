package ru.cwl.mailarc.parser;

import ru.cwl.mailarc.domain.MessageDigest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by adm on 22.02.2017 15:36.
 */
public interface IParser {
    MessageDigest parse(MimeMessage m) throws MessagingException;
}
