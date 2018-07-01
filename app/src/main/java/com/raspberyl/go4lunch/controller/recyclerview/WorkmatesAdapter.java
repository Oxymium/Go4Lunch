package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.User;
import com.raspberyl.go4lunch.utils.TextReformat;

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

        final User user = mWorkmatesList.get(position);

        // Bind items here

        holder.name.setText(user.getUsername());

        if (user.getUrlPicture() != null) {
            Glide.with(mContext)
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.picture);
        }

    }

    @Override
    public int getItemCount() {
        return mWorkmatesList.size();


    }

}