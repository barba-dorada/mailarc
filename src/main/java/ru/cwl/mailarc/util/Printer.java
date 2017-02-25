package ru.cwl.mailarc.util;

import ru.cwl.mailarc.domain.MessageAddress;
import ru.cwl.mailarc.domain.MessageAttach;
import ru.cwl.mailarc.domain.MessageDigest;

/**
 * Created by vadim.tishenko
 * on 23.02.2017 18:41.
 */
public class Printer {
    public String print(MessageDigest md){
        /**
         * from? to, date,
         * subject
         * text
         * attaches
         *
         *
         **/
        StringBuffer sb=new StringBuffer();

        for (MessageAddress ma : md.getMessageAddresses()) {
            sb.append(ma.getType()).append(':').append(ma.getAddress()).append(ma.getPerson()).append('\n');
        }
        sb.append("sent:").append(md.getSentDate()).append('\n');
        sb.append("subj:").append(md.getSubject()).append('\n');
        sb.append("msgId:").append(md.getMessageId()).append('\n');
        sb.append("ref:").append(md.getReferences()).append('\n');
        for (MessageAttach ma : md.getMessageAttachs()) {
            sb.append("attach:").append(ma.getName()).append(':').append(ma.getLength()).append('\n');
        }

        sb.append("---").append(md.getPlainText()).append("\n---\n");

        return sb.toString();

    }
}
