package com.raspberyl.go4lunch.model.googlemaps;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Parcelable{

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

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
    }

    protected Geometry(Parcel in) {
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel source) {
            return new Geometry(source);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
