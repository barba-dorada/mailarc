package ru.cwl.mailarc.domain;

/**
 *
 */
public class MessageAddress implements java.io.Serializable {

    private static final long serialVersionUID = -2450941045712480896L;

    private Integer id;
    private String address;
    private String person;
    private String type;

    public MessageAddress() {
    }


    public MessageAddress(Integer id, String address, String type) {
        this.id = id;
        this.address = address;
        this.type = type;
    }



    public MessageAddress(Integer id, String address, String person, String type) {
        this.id = id;
        this.address = address;
        this.person = person;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

