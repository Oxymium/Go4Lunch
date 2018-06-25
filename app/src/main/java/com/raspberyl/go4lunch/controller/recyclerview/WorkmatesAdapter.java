package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.User;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    // List of Workmates
    private List<User> mWorkmatesList;
    private Context mContext;

    // Constructors
    public WorkmatesAdapter(List<User> mWorkmatesList, Context mContext) {
        this.mWorkmatesList = mWorkmatesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.workmate_view, parent, false);
        WorkmatesViewHolder vHolder = new WorkmatesViewHolder(itemView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

        // Bind items here


    }

    @Override
    public int getItemCount() {
        return 0;
    }

}