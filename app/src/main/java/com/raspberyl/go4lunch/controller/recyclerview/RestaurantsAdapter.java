package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.googlemaps.Example;
import com.raspberyl.go4lunch.model.googlemaps.Result;
import com.raspberyl.go4lunch.utils.ItemClickSupport;

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



    }

    @Override
    public int getItemCount() {

        return mRestaurantList.size();
    }


}

