package com.raspberyl.go4lunch.model.googledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    // Variables

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;

    @SerializedName("place_id")
    @Expose
    private String place_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("website")
    @Expose
    private String website;


    // Constructor

    public Result(String formattedAddress, String formattedPhoneNumber, String place_id, String name, String website) {
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.place_id = place_id;
        this.website = website;
        this.name = name;
    }

    // Getters & setters

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
