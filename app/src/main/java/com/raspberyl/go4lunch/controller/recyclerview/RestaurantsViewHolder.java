package com.raspberyl.go4lunch.controller.recyclerview;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raspberyl.go4lunch.R;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {

    // Variables
    public TextView name, address, distance, workmateNumber, stars, openingTimes;
    public ImageView picture;

    // Constructor
    public RestaurantsViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.restaurant_view_name);
        address = view.findViewById(R.id.restaurant_view_address);
        distance = view.findViewById(R.id.restaurant_view_distance);
        workmateNumber = view.findViewById(R.id.restaurant_view_workmates);
        stars = view.findViewById(R.id.restaurant_view_stars);
        openingTimes = view.findViewById(R.id.restaurant_view_opening_times);

        picture = view.findViewById(R.id.restaurant_view_picture);

    }
}
