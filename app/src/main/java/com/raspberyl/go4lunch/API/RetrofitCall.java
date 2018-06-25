package com.raspberyl.go4lunch.API;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApi;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.model.googlemaps.Example;
import com.raspberyl.go4lunch.model.googlemaps.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCall {

    private String type;
    private double longitude;
    private double latitude;
    private static final int PROXIMITY_RADIUS = 50000;


    public void callRetrofit(double latitude, double longitude, int PROXIMITY_RADIUS) {

        GoogleApiInterface service = GoogleMapsClient.getClient().create(GoogleApiInterface.class);

        Call<Example> call = service.getNearbyRestaurants("restaurant", latitude + "," + longitude, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                List<Result> test = response.body().getResults();
                Log.w("Nearby Restaurants", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                try {


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });

    }

}
