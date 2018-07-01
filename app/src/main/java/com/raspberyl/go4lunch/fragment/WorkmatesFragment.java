package com.raspberyl.go4lunch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.recyclerview.RestaurantsAdapter;
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;
import com.raspberyl.go4lunch.model.User;
import com.raspberyl.go4lunch.model.googlemaps.Result;

import java.util.List;

public class WorkmatesFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private WorkmatesAdapter mWorkmatesAdapter;

    private List<User> mWorkmateList;

    public WorkmatesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_workmates, container, false);

        mRecyclerView = mView.findViewById(R.id.workmates_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mWorkmatesAdapter);

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWorkmateList = new Gson().fromJson(getArguments().getString("userListTest"),
                new TypeToken<List<User>>(){}.getType());
        mWorkmatesAdapter = new WorkmatesAdapter(mWorkmateList, getContext());



    }

}
