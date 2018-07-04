package com.raspberyl.go4lunch.fragment;

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
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;
import com.raspberyl.go4lunch.model.firebase.User;
import com.raspberyl.go4lunch.utils.DividerItemDecoration;
import com.raspberyl.go4lunch.utils.ItemClickSupport;

import java.util.List;

public class WorkmatesFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private WorkmatesAdapter mWorkmatesAdapter;

    private List<User> mWorkmatesList;

    public WorkmatesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_workmates, container, false);

        mRecyclerView = mView.findViewById(R.id.workmates_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mWorkmatesAdapter);

        // Add custom divider between Recycler's views
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), R.drawable.custom_divider);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        this.configureOnClickRecyclerView();

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWorkmatesList = new Gson().fromJson(getArguments().getString("userListTest"),
                new TypeToken<List<User>>(){}.getType());
        mWorkmatesAdapter = new WorkmatesAdapter(mWorkmatesList, getContext());

    }

    // ItemClick listener
    private void configureOnClickRecyclerView () {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_workmates)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        User user = mWorkmatesAdapter.getUserPosition(position);
                        // 2 - Show result in a Toast
                        Toast.makeText(getContext(), "You clicked on workmate: " + user.getUsername(), Toast.LENGTH_SHORT).show();

                        // Pass restaurant ID to activity with intent
                        /*Intent startRestaurantActivity = new Intent(getContext(), RestaurantActivity.class);
                        startRestaurantActivity.putExtra("restaurantId", result.getPlaceId());
                        startActivity(startRestaurantActivity); */
                    }
                });

    }


}
