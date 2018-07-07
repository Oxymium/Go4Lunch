package com.raspberyl.go4lunch.model.firebase;

public class Restaurant {

    private String restaurantName;
    int numberOfLikes;

    // Constructor
    public Restaurant(String restaurantName, int numberOfLikes) {
        this.restaurantName = restaurantName;
        this.numberOfLikes = numberOfLikes; }


    // Getters
    public String getRestaurantName() {
        return restaurantName;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }



    // Setters
    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setId(String id) {
    }

}


