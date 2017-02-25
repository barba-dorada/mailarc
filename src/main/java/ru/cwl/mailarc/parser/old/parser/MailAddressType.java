package ru.cwl.mailarc.parser.old.parser;

/**
 * Created by vad on 22.01.2015.
 */
public enum MailAddressType {
    FROM("From"),
    TO("To"),
    CC("Cc"),
    BCC ("Bcc"),
    REPLY_TO("Reply-To");

    private String val;

    MailAddressType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

}
