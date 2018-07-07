package com.raspberyl.go4lunch.controller.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raspberyl.go4lunch.R;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    // Variables
    public TextView name;
    public ImageView picture;

    // Constructor
    public WorkmatesViewHolder(View view) {
        super(view);

        name = view.findViewById(R.id.workmate_view_text);
        picture = view.findViewById(R.id.workmate_view_picture);

    }
}

