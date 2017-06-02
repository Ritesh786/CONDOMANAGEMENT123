package com.infoservices.lue.entity;

/**
 * Created by lue on 21-07-2016.
 */
public class ImportantContactEntity {
    private String id;
    private String name;
    private String number;
    private String num_type;
    private String updated;
    private String created;
    private String designation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNum_type() {
        return num_type;
    }

    public void setNum_type(String num_type) {
        this.num_type = num_type;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
