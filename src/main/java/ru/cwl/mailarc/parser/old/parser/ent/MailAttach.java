package ru.cwl.mailarc.parser.old.parser.ent;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author stanislav.malchenko
 */
public class MailAttach {

    private Part attach;
    private int multipartIndex;

    public MailAttach(Part attach, int multipartIndex) {
        this.attach = attach;
        this.multipartIndex = multipartIndex;
    }

    public MailAttach(Part attach) {
        this.attach = attach;
        this.multipartIndex = 0;
    }

    public int getMultipartIndex() {
        return multipartIndex;
    }

    public String getFileName() throws UnsupportedEncodingException,
            MessagingException {

        try {
            return MimeUtility.decodeText(attach.getFileName());

        } catch (NullPointerException e) {

            String fileName = "noname";

            if (attach.getContentType() != null)
                fileName = fileName + getExtension(attach.getContentType());

            return fileName;
        }
    }

    public int getSize() throws MessagingException {
        return attach.getSize();
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return attach.getInputStream();
    }

    private String getExtension(String contentType) {
        if (contentType.contains("excel"))
            return "xls";
        else if (contentType.contains("word"))
            return "doc";
        return "";
    }
}
