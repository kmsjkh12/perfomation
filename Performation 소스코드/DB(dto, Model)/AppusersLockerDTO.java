package com.example.demo.dto;

public class AppusersLockerDTO {
    private String id;
    private String appusers_id;
    private String info_id;
    private String serial_number;
    private String buy_time;
    private String price;
    private String qrcode;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppusers_id() {
        return appusers_id;
    }

    public void setAppusers_id(String appusers_id) {
        this.appusers_id = appusers_id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInfo_id() { return info_id; }

    public void setInfo_id(String info_id) { this.info_id = info_id; }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

}
