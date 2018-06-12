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
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;

public class WorkmatesFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private WorkmatesAdapter mWorkmatesAdapter;

    public WorkmatesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_workmates, container, false);
        mRecyclerView = mView.findViewById(R.id.workmates_recycler_view);

        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DO STUFF HERE

        /*
        mWorkmatesAdapter = new WorkmatesAdapter(WORKMATESLIST, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mWorkmatesAdapter);
        */

    }

}
