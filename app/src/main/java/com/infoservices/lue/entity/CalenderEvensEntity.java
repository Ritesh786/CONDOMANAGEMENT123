package com.infoservices.lue.entity;

import java.io.Serializable;

/**
 * Created by lue on 11-08-2016.
 */
public class CalenderEvensEntity implements Serializable{
    private String id;
    private String mgnt_id;
    private String title;
    private String event_purpose;
    private String event_date;
    private String from_time;
    private String to_time;
    private String targeted_paxs;
    private String description;
    private String posted_by;
    private String created;
    private String member_id;
    private String image;

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

    public String getEvent_purpose() {
        return event_purpose;
    }

    public void setEvent_purpose(String event_purpose) {
        this.event_purpose = event_purpose;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getTargeted_paxs() {
        return targeted_paxs;
    }

    public void setTargeted_paxs(String targeted_paxs) {
        this.targeted_paxs = targeted_paxs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
