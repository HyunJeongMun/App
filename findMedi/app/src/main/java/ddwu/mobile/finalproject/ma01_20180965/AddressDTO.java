package ddwu.mobile.finalproject.ma01_20180965;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class AddressDTO implements Serializable {
    private String address;
    private double lat;
    private double lng;

    public AddressDTO() {
    }

    public AddressDTO(String address, double lat, double lng) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
