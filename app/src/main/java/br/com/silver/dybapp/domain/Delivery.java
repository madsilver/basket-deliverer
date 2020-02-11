package br.com.silver.dybapp.domain;


import com.google.gson.Gson;

import io.realm.RealmObject;

public class Delivery extends RealmObject {

    public static int NOT_SYNCED = 0;
    public static int SYNCED = 1;
    public static int DELEVIRED = 0;
    public static int OCCURRENCE = 1;
    public static int MAIN_ADDRESS = 2;
    public static int ALTERNATE_ADDRESS = 3;
    public static int ABSENT = 4;
    public static int CHANGED_ADDRESS = 5;
    public static int INCORRECT_ADDRESS = 6;

    private String imei;
    private String code;
    private String date;
    private int status;
    private int statusDetail;
    private String lat;
    private String lng;
    private int sync;

    public Delivery() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(int statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "{" +
                "imei:" + getImei() +
                ",code:" + getCode() +
                ",date:" + getDate() +
                ",status:" + getStatus() +
                ",status_detail:" + getStatusDetail() +
                ",lat:" + getLat() +
                ",lng:" + getLng() +
                "}";
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
