package com.raspberyl.go4lunch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.MainActivity;
import com.raspberyl.go4lunch.controller.recyclerview.RestaurantsAdapter;
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;
import com.raspberyl.go4lunch.model.googlemaps.Example;
import com.raspberyl.go4lunch.model.googlemaps.Geometry;
import com.raspberyl.go4lunch.model.googlemaps.Result;

import java.util.List;

public class RestaurantsFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private RestaurantsAdapter mRestaurantsAdapter;

    private List<Result> mRestaurantList;

    public RestaurantsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_restaurants, container, false);

        mRecyclerView = mView.findViewById(R.id.restaurants_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRestaurantsAdapter);

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRestaurantList = new Gson().fromJson(getArguments().getString("valuesArray"),
                new TypeToken<List<Result>>(){}.getType());
        mRestaurantsAdapter = new RestaurantsAdapter(mRestaurantList, getContext());

        Log.w("FRAGMENT Restaurants", new GsonBuilder().setPrettyPrinting().create().toJson(mRestaurantList));


        /*
        mRestaurantsAdapter = new RestaurantsAdapter(mRestaurantList, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRestaurantsAdapter);
        */
    }

}
