
package com.esd.model.data.apis;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("formatted_address")
    @Expose
    private List<String> formattedAddress = null;
    @SerializedName("thoroughfare")
    @Expose
    private String thoroughfare;
    @SerializedName("building_name")
    @Expose
    private String buildingName;
    @SerializedName("sub_building_name")
    @Expose
    private String subBuildingName;
    @SerializedName("sub_building_number")
    @Expose
    private String subBuildingNumber;
    @SerializedName("building_number")
    @Expose
    private String buildingNumber;
    @SerializedName("line_1")
    @Expose
    private String line1;
    @SerializedName("line_2")
    @Expose
    private String line2;
    @SerializedName("line_3")
    @Expose
    private String line3;
    @SerializedName("line_4")
    @Expose
    private String line4;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("town_or_city")
    @Expose
    private String townOrCity;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("country")
    @Expose
    private String country;

    /**
     * No args constructor for use in serialization
     *
     */
    public Address() {
    }

    /**
     *
     * @param country
     * @param townOrCity
     * @param line4
     * @param locality
     * @param county
     * @param thoroughfare
     * @param subBuildingName
     * @param buildingName
     * @param formattedAddress
     * @param district
     * @param buildingNumber
     * @param line3
     * @param line2
     * @param line1
     * @param subBuildingNumber
     */
    public Address(List<String> formattedAddress, String thoroughfare, String buildingName, String subBuildingName, String subBuildingNumber, String buildingNumber, String line1, String line2, String line3, String line4, String locality, String townOrCity, String county, String district, String country) {
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

    public List<String> getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(List<String> formattedAddress) {
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

}