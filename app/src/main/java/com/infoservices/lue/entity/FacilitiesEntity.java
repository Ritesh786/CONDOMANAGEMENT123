package com.infoservices.lue.entity;

import java.io.Serializable;

/**
 * Created by lue on 18-07-2016.
 */
public class FacilitiesEntity implements Serializable{
    private String id;
    private String title;
    private String point_1;
    private String point_2;
    private String point_3;
    private String rate;
    private String photo;
    private String booking_notice;
    private String product_id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoint_1() {
        return point_1;
    }

    public void setPoint_1(String point_1) {
        this.point_1 = point_1;
    }

    public String getPoint_2() {
        return point_2;
    }

    public void setPoint_2(String point_2) {
        this.point_2 = point_2;
    }

    public String getPoint_3() {
        return point_3;
    }

    public void setPoint_3(String point_3) {
        this.point_3 = point_3;
    }
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBooking_notice() {
        return booking_notice;
    }

    public void setBooking_notice(String booking_notice) {
        this.booking_notice = booking_notice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
