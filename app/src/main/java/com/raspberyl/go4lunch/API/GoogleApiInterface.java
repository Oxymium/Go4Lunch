package com.raspberyl.go4lunch.API;

import com.raspberyl.go4lunch.model.googledetails.Details;
import com.raspberyl.go4lunch.model.googleplaces.Example;
import com.raspberyl.go4lunch.model.googleplaces.Photo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiInterface {

    // Nearbysearch API
    @GET("api/place/nearbysearch/json?key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw")
    Call<Example> getNearbyRestaurants(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    // Photo API
    @GET("api/place/photo?key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw")
    Call<Photo> getRestaurantPhoto(@Query("photoreference") String photoreference, @Query("maxheight") int maxheight, @Query("maxwidth") int maxwidth);

    // Details API
    @GET("api/place/details/json?key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw")
    Call<Details> getRestaurantDetails(@Query("placeid") String placeId);

}
