package br.com.silver.dybapp.domain;


import io.realm.RealmObject;

public class Delivery extends RealmObject {

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

}
