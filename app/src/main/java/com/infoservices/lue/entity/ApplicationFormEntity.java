package com.infoservices.lue.entity;

import java.io.Serializable;

/**
 * Created by lue on 10-08-2016.
 */
public class ApplicationFormEntity implements Serializable{


    private String id;
    private String mgnt_id;
    private String tital;
    private String image;
    private String description;
    private String created;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMgnt_id() {
        return mgnt_id;
    }

    public void setMgnt_id(String mgnt_id) {
        this.mgnt_id = mgnt_id;
    }

    public String getTital() {
        return tital;
    }

    public void setTital(String tital) {
        this.tital = tital;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
