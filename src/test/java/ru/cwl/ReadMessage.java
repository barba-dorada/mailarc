package ru.cwl;

import ru.cwl.mailarc.domain.MessageDigest;
import ru.cwl.mailarc.parser.IParser;
import ru.cwl.mailarc.parser.Parser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by adm on 21.02.2017 14:45.
 */
public class ReadMessage {
    public static void main(String[] args) throws MessagingException, FileNotFoundException {

        String files[] = {
                "md50000047541.msg", "md50000051214.msg", "md50000051218.msg",
                "md50000051222.msg", "md50000051226.msg", "md50000051230.msg",
                "md50000051211.msg", "md50000051215.msg", "md50000051219.msg",
                "md50000051223.msg", "md50000051227.msg", "md50000051212.msg",
                "md50000051216.msg", "md50000051220.msg", "md50000051224.msg",
                "md50000051228.msg", "md50000051213.msg", "md50000051217.msg",
                "md50000051221.msg", "md50000051225.msg", "md50000051229.msg"
        };

        IParser p = new Parser();

        for (String file : files) {
            //File file=new File("testmsg/md50000051213.msg");

            InputStream is2 = ReadMessage.class.getResourceAsStream("/testmsg/"+file);//md50000051213.msg");
            //System.out.println(file.getAbsolutePath());
            InputStream is = new BufferedInputStream(is2);
            MimeMessage mimeMessage = new MimeMessage(null, is);
            MessageDigest res = p.parse(mimeMessage);
            System.out.println(file);
            System.out.println(res.getSubject());
            Arrays.toString(res.getMessageAddresses().toArray());
        }

    }

/*    private static ParsedMessage parse(MimeMessage mimeMessage) {
        ParsedMessage res=new ParsedMessage();
        Parser parser=new Parser();
        parser.setMailMessage(mimeMessage);
        parser.getAddr();
        parser.getDateSent();
        parser.getMessageId();
        parser.getMessageText();
        parser.getSubject();
        parser.getSize();
        return null;
    }*/
}
