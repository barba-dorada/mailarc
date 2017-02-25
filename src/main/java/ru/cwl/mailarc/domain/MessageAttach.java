package ru.cwl.mailarc.domain;

public class MessageAttach implements java.io.Serializable {

	private static final long serialVersionUID = 8193751939164545206L;
	
	private Integer id;
	private MessageDigest messageDigest;
	private String name;
	private Long length;

	public MessageAttach() {
	}

	public MessageAttach(Integer id, MessageDigest messageDigest, String name) {
		this.id = id;
		this.messageDigest = messageDigest;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MessageDigest getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(MessageDigest messageDigest) {
		this.messageDigest = messageDigest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}
}