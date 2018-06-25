package com.raspberyl.go4lunch.model.googlemaps;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Example implements Parcelable {

    // Variables
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    @Expose
    private String status;

    // Constructor
    public Example(List<Object> htmlAttributions, String nextPageToken, List<Result> results, String status) {
        this.htmlAttributions = htmlAttributions;
        this.nextPageToken = nextPageToken;
        this.results = results;
        this.status = status;
    }

    // Getters & Setters
    /**
     *
     * @return
     * The htmlAttributions
     */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     *
     * @param htmlAttributions
     * The html_attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     *
     * @return
     * The nextPageToken
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     *
     * @param nextPageToken
     * The next_page_token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.htmlAttributions);
        dest.writeString(this.nextPageToken);
        dest.writeTypedList(this.results);
        dest.writeString(this.status);
    }

    protected Example(Parcel in) {
        this.htmlAttributions = new ArrayList<Object>();
        in.readList(this.htmlAttributions, Object.class.getClassLoader());
        this.nextPageToken = in.readString();
        this.results = in.createTypedArrayList(Result.CREATOR);
        this.status = in.readString();
    }

    public static final Parcelable.Creator<Example> CREATOR = new Parcelable.Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel source) {
            return new Example(source);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };
}

