package com.infoservices.lue.entity;

/**
 * Created by lue on 29-03-2017.
 */

public class PreResitrationEntity {
    private String member_name;
    private String mgnt_id;
    private String member_id;
    private String nric_number;
    private String car_number;
    private String from_date;
    private String to_date;
    private String reg_date;
    private String status;
    private String passport_no;

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMgnt_id() {
        return mgnt_id;
    }

    public void setMgnt_id(String mgnt_id) {
        this.mgnt_id = mgnt_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getNric_number() {
        return nric_number;
    }

    public void setNric_number(String nric_number) {
        this.nric_number = nric_number;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }
}
