package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.googleplaces.Result;
import com.raspberyl.go4lunch.utils.LatLongiDistanceConverter;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    // List of Restaurants
    private List<Result> mRestaurantList;
    private Context mContext;

    // Constructors
    public RestaurantsAdapter(List<Result> mRestaurantList, Context mContext) {
        this.mRestaurantList = mRestaurantList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.restaurant_view, parent, false);
        RestaurantsViewHolder vHolder = new RestaurantsViewHolder(itemView);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {

        final Result result = mRestaurantList.get(position);

        //Bind here

        // Restaurant name
        holder.name.setText(result.getName());

        // Restaurant address
        holder.address.setText(result.getVicinity());

        // Restaurant opening/closing times
        boolean isOpenNow = result.getOpeningHours().getOpenNow();
        if (isOpenNow) {
            holder.openingTimes.setText("is open now");
        }else{
            holder.openingTimes.setText("is closed now");
        }

        // Restaurant distance (m)
        double longitudeTest = 0.107929;
        double latitudeTest = 49.49437;
        double targetLatitude = result.getGeometry().getLocation().getLat();
        double targetLongitude = result.getGeometry().getLocation().getLng();
        float distance = LatLongiDistanceConverter.distFrom((float) longitudeTest, (float) latitudeTest, (float) targetLatitude, (float) targetLongitude);
        String distanceS = String.valueOf(distance);
        holder.distance.setText(distanceS);

    }

    @Override
    public int getItemCount() {

        return mRestaurantList.size();
    }

    // Itemclick?
    public Result getRestaurant(int position){
        return this.mRestaurantList.get(position);
    }


}

