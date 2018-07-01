package com.raspberyl.go4lunch.model.googleplaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    // Variables
    @SerializedName("location")
    @Expose
    private Location location;

    // Constructor
    public Geometry(Location location) {
        this.location = location;
    }

    // Getters & Setters
    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

}
