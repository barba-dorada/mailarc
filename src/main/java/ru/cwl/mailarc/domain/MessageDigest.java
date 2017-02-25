package ru.cwl.mailarc.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class MessageDigest implements java.io.Serializable {

    private static final long serialVersionUID = 3683166291847021725L;

    private Integer id;
    private String subject;
    private Timestamp sentDate;
    private String messageId;
    private String plainText = "";
    private String srcFile;
    private Set<MessageAttach> messageAttachs = new HashSet<MessageAttach>(0);
    private Set<MessageAddress> messageAddresses = new HashSet<MessageAddress>(0);
    private String sha1;
    private int fileSize;
    private String references;

    public MessageDigest() {
    }

/*	public MessageDigest(Integer id, String plainText, String srcFile) {
        this.id = id;
		this.plainText = plainText;
		this.srcFile = srcFile;
	}*/

/*	public MessageDigest(Integer id, String subject, Timestamp sentDate,
			String messageId, String plainText, String srcFile,
			Set<MessageAttach> messageAttachs,
			Set<MessageAddress> messageAddresses) {
		this.id = id;
		this.subject = subject;
		this.sentDate = sentDate;
		this.messageId = messageId;
		this.plainText = plainText;
		this.srcFile = srcFile;
		this.messageAttachs = messageAttachs;
		this.messageAddresses = messageAddresses;
	}*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    public Set<MessageAttach> getMessageAttachs() {
        if (messageAttachs == null) {
            messageAttachs = new HashSet<>();
        }

        return messageAttachs;
    }

    public void setMessageAttachs(Set<MessageAttach> messageAttachs) {
        this.messageAttachs = messageAttachs;
    }

    public Set<MessageAddress> getMessageAddresses() {
        if (messageAddresses == null) {
            messageAddresses = new HashSet<>();
        }
        return messageAddresses;
    }

    public void setMessageAddresses(Set<MessageAddress> messageAddresses) {
        this.messageAddresses = messageAddresses;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }
}