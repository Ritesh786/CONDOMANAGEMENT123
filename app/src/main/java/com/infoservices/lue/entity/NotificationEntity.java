package com.infoservices.lue.entity;

import java.io.Serializable;

/**
 * Created by lue on 29-07-2016.
 */
public class NotificationEntity implements Serializable{
    private String id;
    private String notification_id;
    private String mgnt_id;
    private String title;
    private String image;
    private String description;
    private String status;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }
}
