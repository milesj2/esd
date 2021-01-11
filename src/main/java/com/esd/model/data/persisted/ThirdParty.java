package com.esd.model.data.persisted;

/**
 * Original Author: Angela Jackson
 * Use: This class is a simple data class used to store the data about third party
 */
public class ThirdParty {
    
    private int id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String town;
    private String postCode;
    private String type;
    private boolean active = true;
    
    public ThirdParty() {
    }
    
    public ThirdParty (int id, String name, String addressLine1, String addressLine2,
                        String addressLine3, String town, String postCode, String type, boolean active) {
        this.id = id;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.town = town;
        this.postCode = postCode;
        this.type = type;
        this.active = active;

    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
    this.name = name;
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
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isActive() { 
        return active; 
    }

    public void setActive(boolean active) { 
        this.active = active; 
    }
    
}
