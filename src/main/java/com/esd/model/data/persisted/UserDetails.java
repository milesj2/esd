package com.esd.model.data.persisted;

import com.esd.model.data.UserGroup;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about the address and other details
 */
public class UserDetails {

    private int id;
    private int userId;

    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;

    private String town;
    private String postCode;
    private UserGroup userGroup;


    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        sb.append(addressLine1 + "\n");
        sb.append(addressLine2 + "\n");
        sb.append(addressLine3 + "\n");
        sb.append(town + "\n");
        sb.append(postCode + "\n");
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
