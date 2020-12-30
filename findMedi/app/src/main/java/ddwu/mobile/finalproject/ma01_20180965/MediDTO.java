package ddwu.mobile.finalproject.ma01_20180965;

import java.io.Serializable;

public class MediDTO implements Serializable {
    private int _id;
    private String type;
    private String address; // dutyAddr
    private String divName; // dutyDivName
    private String name; // dutyName
    private String tel; // dutyTel1
    private String[] time; // startTime endTime
    private String memo;
    private double lat; // latitude
    private double lng; // longitude
    private String photoUrl;

    public MediDTO(){}

    public MediDTO(int _id, String type, String address, String divName, String name, String tel, String[] time, String memo, double lat, double lng, String photoUrl) {
        this._id = _id;
        this.type = type;
        this.address = address;
        this.divName = divName;
        this.name = name;
        this.tel = tel;
        this.time = time;
        this.memo = memo;
        this.lat = lat;
        this.lng = lng;
        this.photoUrl = photoUrl;
    }

    public MediDTO(String type, String address, String divName, String name, String tel, String[] time, String memo, double lat, double lng, String photoUrl) {
        this._id = _id;
        this.type = type;
        this.address = address;
        this.divName = divName;
        this.name = name;
        this.tel = tel;
        this.time = time;
        this.memo = memo;
        this.lat = lat;
        this.lng = lng;
        this.photoUrl = photoUrl;
    }

    public MediDTO(String type, String address, String divName, String name, String tel, String[] time, String memo, double lat, double lng) {
        this._id = _id;
        this.type = type;
        this.address = address;
        this.divName = divName;
        this.name = name;
        this.tel = tel;
        this.time = time;
        this.memo = memo;
        this.lat = lat;
        this.lng = lng;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
