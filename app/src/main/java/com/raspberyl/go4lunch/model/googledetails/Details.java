package com.raspberyl.go4lunch.model.googledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raspberyl.go4lunch.model.googledetails.Result;

import java.util.ArrayList;
import java.util.List;

public class Details {

    // Variables

    @SerializedName("result")
    @Expose
    private Result result;

    // Constructor

    public Details(Result result) {
        this.result = result;
    }

    // Getters & Setters

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
