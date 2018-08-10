package com.raspberyl.go4lunch.model.firebase;

public class Restaurant {

    private String restaurantName;
    int numberOfLikes;
    int numberOfPeopleJoining;

    public Restaurant() {}

    // Constructor
    public Restaurant(String restaurantName, int numberOfLikes, int numberOfPeopleJoining) {
        this.restaurantName = restaurantName;
        this.numberOfLikes = numberOfLikes;
        this.numberOfPeopleJoining = numberOfPeopleJoining; }


    // Getters
    public String getRestaurantName() {
        return restaurantName;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public int getNumberOfPeopleJoining() {
        return numberOfPeopleJoining;
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

    public void setNumberOfPeopleJoining(int numberOfPeopleJoining) {
        this.numberOfPeopleJoining = numberOfPeopleJoining;
    }

}


