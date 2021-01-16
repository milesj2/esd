package com.esd.model.data.apis;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostcodeLookup {

    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("addresses")
    @Expose
    private List<Address> addresses = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public PostcodeLookup() {
    }

    /**
     *
     * @param addresses
     * @param latitude
     * @param postcode
     * @param longitude
     */
    public PostcodeLookup(String postcode, Double latitude, Double longitude, List<Address> addresses) {
        super();
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addresses = addresses;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}