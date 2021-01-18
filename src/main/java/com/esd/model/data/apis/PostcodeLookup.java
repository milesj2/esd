package com.esd.model.data.apis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostcodeLookup implements Serializable
{
    private String postcode;
    private Double latitude;
    private Double longitude;
    private List<Address> addresses = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5411542499439244587L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PostcodeLookup() {
    }

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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}