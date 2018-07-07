package com.raspberyl.go4lunch.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.RestaurantActivity;
import com.raspberyl.go4lunch.controller.recyclerview.RestaurantsAdapter;
import com.raspberyl.go4lunch.model.googleplaces.Result;
import com.raspberyl.go4lunch.utils.ItemClickSupport;

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

        // Add custom divider between Recycler's views
        com.raspberyl.go4lunch.utils.DividerItemDecoration mDividerItemDecoration = new com.raspberyl.go4lunch.utils.DividerItemDecoration(
                mRecyclerView.getContext(), R.drawable.horizontal_divider);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);



        this.configureOnClickRecyclerView();

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get RestaurantList from MainActivity
        mRestaurantList = new Gson().fromJson(getArguments().getString("valuesArray"),
                new TypeToken<List<Result>>() {
                }.getType());
        // Pass it to Adapter
        mRestaurantsAdapter = new RestaurantsAdapter(mRestaurantList, getContext()); }


        // ItemClick listener
        private void configureOnClickRecyclerView () {
            ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_restaurants)
                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            Result result = mRestaurantsAdapter.getRestaurantPosition(position);
                            // Show result in a Toast
                            Toast.makeText(getContext(), "You clicked on restaurant: "+result.getName(), Toast.LENGTH_SHORT).show();

                            // Pass restaurant ID & Picture to activity with intent
                            Intent startRestaurantActivity = new Intent(getContext(), RestaurantActivity.class);
                            startRestaurantActivity.putExtra("restaurantId", result.getPlaceId());
                            if (result.getPhotos().get(0).getPhotoReference() != null) {
                            startRestaurantActivity.putExtra("restaurantPicture", result.getPhotos().get(0).getPhotoReference());
                            }
                            startActivity(startRestaurantActivity);


                        }
                    });

    }

}
