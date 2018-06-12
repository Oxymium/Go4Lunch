package com.raspberyl.go4lunch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.recyclerview.RestaurantsAdapter;
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;

public class RestaurantsFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private RestaurantsAdapter mRestaurantsAdapter;


    public RestaurantsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        mRecyclerView = mView.findViewById(R.id.restaurants_recycler_view);

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        mRestaurantsAdapter = new WorkmatesAdapter(RESTAURANTLIST, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRestaurantsAdapter);
        */


    }

}
