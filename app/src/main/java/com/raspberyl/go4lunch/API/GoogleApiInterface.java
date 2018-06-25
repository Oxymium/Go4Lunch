package com.raspberyl.go4lunch.API;

import com.raspberyl.go4lunch.model.googlemaps.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiInterface {

    @GET("api/place/nearbysearch/json?key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw")
    Call<Example> getNearbyRestaurants(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
