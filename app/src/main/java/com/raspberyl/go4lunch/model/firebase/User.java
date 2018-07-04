package com.raspberyl.go4lunch.model.firebase;

import android.support.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String chosenRestaurantId;


    public User() {
    }

    // Contructor
    public User(String uid, String username, String urlPicture, String chosenRestaurantId) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.chosenRestaurantId = chosenRestaurantId;

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

}

