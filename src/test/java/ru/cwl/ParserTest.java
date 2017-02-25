package ru.cwl;

import org.junit.Test;
import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.IParser;
import ru.cwl.mailarc.parser.Parser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedInputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by adm on 22.02.2017 16:51.
 */
public class ParserTest {

    protected IParser p;

    public ParserTest() {
        p = new Parser();
    }

    @Test
    public void testMessage() throws MessagingException {

        /**
         * md50000051213.msg
         Message-ID: <002301c3230b$554cba10$7600a8c0@sonya>
         Reply-To: "Sh. Sonia" <pne@sferacom.ru>
         From: "Sh. Sonia" <pne@sferacom.ru>
         To: <lavrushin@iph.ru>
         References: <000201c32106$ee9b6e70$a4180e0a@iph.int>
         */

        MimeMessage mimeMessage = getMimeMessageFromResource("md50000051213.msg");

        MessageDigest res = p.parse(mimeMessage);

        assertThat(res.getSubject(), is("(Archive Copy) Re:"));
        assertThat(res.getMessageId(), is("<002301c3230b$554cba10$7600a8c0@sonya>"));
        assertThat(res.getReferences(), is("<000201c32106$ee9b6e70$a4180e0a@iph.int>"));
        assertThat(res.getMessageAddresses().stream().filter(pe -> "lavrushin@iph.ru".equals(pe.getAddress())).count(), is(1L));
        assertThat(res.getMessageAddresses().stream().filter(pe -> "pne@sferacom.ru".equals(pe.getAddress())).count(), is(1L));
    }

    private MimeMessage getMimeMessageFromResource(String fileName) throws MessagingException {
        InputStream is2 = ReadMessage.class.getResourceAsStream("/testmsg/" + fileName);
        InputStream ist = new BufferedInputStream(is2);
        return new MimeMessage(null, ist);
    }

    /**
     * Reply-To: <cherepov@iph.ru>
     * From: "Cherepov Dennis" <cherepov@iph.ru>
     * To: <tishenko@iph.ru>
     * Subject: (Archive Copy)
     * Date: Mon, 12 May 2003 17:18:32 +0400
     * Message-ID: <003e01c31888$fe371ef0$9628400a@iph.int>
     */
    @Test
    public void testBigMessag71G() throws MessagingException {
        MimeMessage mimeMessage = getMimeMessageFromResource("md50000047541.msg");

        MessageDigest res = p.parse(mimeMessage);

        assertThat(res.getSubject(), is("(Archive Copy)"));
        assertThat(res.getMessageId(), is("<003e01c31888$fe371ef0$9628400a@iph.int>"));
        assertThat(res.getReferences(), is(""));
        assertThat(res.getMessageAddresses().stream().filter(pe -> "cherepov@iph.ru".equals(pe.getAddress())).count(), is(1L));
        assertThat(res.getMessageAddresses().stream().filter(pe -> "tishenko@iph.ru".equals(pe.getAddress())).count(), is(1L));

    }

}
