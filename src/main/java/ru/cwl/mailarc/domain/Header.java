package ru.cwl.mailarc.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by vadim.tishenko
 * on 13.03.2017 23:37.
 */
// TODO: 13.03.2017 add json serialization (from and to) sample https://github.com/m0mus/JavaOne2016-JSONB-Demo
// TODO: 13.03.2017 add import msg from zip
public class Header {
    String from;
    List<String> to;
    List<String> cc;
    List<String> bcc;
    String subj;
    LocalDateTime sent;
    String msgId;

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getSubj() {
        return subj;
    }

    public String getMsgId() {
        return msgId;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    @Override
    public String toString() {
        return "Header{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", subj='" + subj + '\'' +
                ", sent=" + sent +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
