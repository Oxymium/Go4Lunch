package com.raspberyl.go4lunch.controller.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.firebase.User;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    // List of Workmates
    private List<User> mWorkmatesList;
    private Context mContext;
    private int restauID;

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

        // Workmate name
        String displayedUsername = (user.getUsername()).split(" ")[0];
        // No restaurant chosen
        String finalDisplayedUndecidedUserText = displayedUsername + " " + mContext.getString(R.string.workmate_view_undecided);
        // Restaurant chosen
        String finalDisplayedDecidedUserText = displayedUsername + " " + mContext.getString(R.string.workmate_view_decided) + " " + user.getChosenRestaurantName();
        // RestaurantActivity view
        String finalDisplayedRestaurantActivityView = displayedUsername + " " + mContext.getString(R.string.workmate_view_restaurantactivity);

        if (restauID == 0) {

            if (user.getChosenRestaurantName().isEmpty()) {
                holder.name.setText(finalDisplayedUndecidedUserText);
                // Italic
                holder.name.setTypeface(null, Typeface.ITALIC);
            } else {
                holder.name.setText(finalDisplayedDecidedUserText);
                // Bold
                holder.name.setTypeface(null, Typeface.BOLD);
            }

        }else if(restauID == 222) {

            holder.name.setText(finalDisplayedRestaurantActivityView);
        }


        // Workmate picture
        if (user.getUrlPicture() != null) {

            if (user.getUrlPicture().isEmpty()) {

                String noPictureUrl = "https://www.definitions-seo.com/images/definition-reecriture-url.png";
                Glide.with(mContext)
                        .load(noPictureUrl)
                        .apply(RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.unknown_userpicture))
                        .into(holder.picture);

            }else{

                Glide.with(mContext)
                        .load(user.getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.picture);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mWorkmatesList.size();

    }

    // Allow change of text from another activity
    public void setFinalDisplayedDecidedUserText(int restauID) {
        this.restauID = restauID;
        notifyDataSetChanged();
    }

    // Itemclick?
    public User getUserPosition(int position){
        return this.mWorkmatesList.get(position);
    }


}