package com.raspberyl.go4lunch.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapsClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/";
    private static Retrofit retrofit = null;

    // Configure Retrofit client
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}

