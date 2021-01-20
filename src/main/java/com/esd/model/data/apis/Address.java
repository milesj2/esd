package com.esd.model.data.apis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Original Author: Trent Meier
 * Use: Plain old java object for mapping json
 */

public class Address implements Serializable
{
    private String[] formattedAddress = null;
    private String thoroughfare;
    private String buildingName;
    private String subBuildingName;
    private String subBuildingNumber;
    private String buildingNumber;
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String locality;
    private String townOrCity;
    private String county;
    private String district;
    private String country;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3959039178437972566L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Address() {
    }

    public Address(String[] formattedAddress, String thoroughfare, String buildingName, String subBuildingName, String subBuildingNumber, String buildingNumber, String line1, String line2, String line3, String line4, String locality, String townOrCity, String county, String district, String country) {
        super();
        this.formattedAddress = formattedAddress;
        this.thoroughfare = thoroughfare;
        this.buildingName = buildingName;
        this.subBuildingName = subBuildingName;
        this.subBuildingNumber = subBuildingNumber;
        this.buildingNumber = buildingNumber;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.locality = locality;
        this.townOrCity = townOrCity;
        this.county = county;
        this.district = district;
        this.country = country;
    }

    public String[] getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String[] formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getThoroughfare() {
        return thoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.thoroughfare = thoroughfare;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getSubBuildingName() {
        return subBuildingName;
    }

    public void setSubBuildingName(String subBuildingName) {
        this.subBuildingName = subBuildingName;
    }

    public String getSubBuildingNumber() {
        return subBuildingNumber;
    }

    public void setSubBuildingNumber(String subBuildingNumber) {
        this.subBuildingNumber = subBuildingNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getTownOrCity() {
        return townOrCity;
    }

    public void setTownOrCity(String townOrCity) {
        this.townOrCity = townOrCity;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
