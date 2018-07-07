package com.raspberyl.go4lunch.model.firebase;

import android.support.annotation.Nullable;

import java.util.List;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String chosenRestaurantId;
    private String chosenRestaurantName;
    private String chosenRestaurantUrlPicture;

    public User() {}

    // Contructor
    public User(String uid, String username, String urlPicture, String chosenRestaurantId, String chosenRestaurantName, String chosenRestaurantUrlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.chosenRestaurantId = chosenRestaurantId;
        this.chosenRestaurantName = chosenRestaurantName;
        this.chosenRestaurantUrlPicture = chosenRestaurantUrlPicture;
    }

    // Getters
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public String getChosenRestaurantId() { return chosenRestaurantId; }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }

    public String getChosenRestaurantUrlPicture() {
        return chosenRestaurantUrlPicture;
    }


    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setChosenRestaurantId(String chosenRestaurantId) { this.chosenRestaurantId = chosenRestaurantId; }

    public void setChosenRestaurantName(String chosenRestaurantName) {
        this.chosenRestaurantName = chosenRestaurantName;
    }

    public void setChosenRestaurantUrlPicture(String chosenRestaurantUrlPicture) {
        this.chosenRestaurantUrlPicture = chosenRestaurantUrlPicture;
    }


}

