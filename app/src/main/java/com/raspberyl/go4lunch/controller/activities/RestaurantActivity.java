package com.raspberyl.go4lunch.controller.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.GoogleMapsClient;
import com.raspberyl.go4lunch.API.RestaurantHelper;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.recyclerview.WorkmatesAdapter;
import com.raspberyl.go4lunch.model.firebase.Restaurant;
import com.raspberyl.go4lunch.model.firebase.User;
import com.raspberyl.go4lunch.model.googledetails.Details;
import com.raspberyl.go4lunch.model.googledetails.Result;
import com.raspberyl.go4lunch.utils.AlertDialogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends AppCompatActivity {

    private Button mRestaurantCallButton, mRestaurantWebsiteButton, mRestaurantLikeButton;
    private FloatingActionButton mGoRestaurantButton;
    private TextView mRestaurantNameText, mRestaurantAddressText;
    private ImageView mRestaurantImageView;

    private RecyclerView mRecyclerView;
    private WorkmatesAdapter mWorkmatesAdapter;

    private Result mResult;

    private String mPlaceId, mPhotoId;
    private List<User> workmatesOnThatRestaurant;

    private List<User> test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Get PlaceId and PhotoId from Intents
        mPlaceId = getIntent().getStringExtra("restaurantId");
        mPhotoId = getIntent().getStringExtra("restaurantPicture");

        getPlaceDetails(mPlaceId);

        // Init ArrayList
        workmatesOnThatRestaurant = new ArrayList<>();
        workmatesOnThatRestaurant.clear();

        mWorkmatesAdapter = new WorkmatesAdapter(workmatesOnThatRestaurant, getApplicationContext());
        mRecyclerView = findViewById(R.id.activity_restaurant_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mWorkmatesAdapter);

    }



    // ------------------
    // API
    // ------------------

    // Check place details from PlaceID string
    public void getPlaceDetails(String placeId) {

        GoogleApiInterface service = GoogleMapsClient.getClient().create(GoogleApiInterface.class);

        Call<Details> call = service.getRestaurantDetails(placeId);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                try {

                    mResult = response.body().getResult();

                    // set: restaurant picture
                    setRestaurantPictureBackground();
                    // set: name
                    setRestaurantName(mResult);
                    // set: address
                    setRestaurantAddress(mResult);
                    // init: restaurant call button (phone number)
                    initRestaurantCallButton(mResult);
                    // init: restaurant website button (webview)
                    initRestaurantWebsiteButton(mResult);
                    // init: go restaurant website (restaurant choice)
                    initGoButton(mResult);
                    // init: like button
                    initRestaurantLikeButton(mResult);

                    getAllUsersOnCurrentRestaurant(mResult);


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });

    }

    // ------------------------
    // Text & ImageViews update
    // ------------------------

    private void setRestaurantPictureBackground() {

        mRestaurantImageView = findViewById(R.id.activity_restaurant_restaurant_picture);

        if (mPhotoId != null) {
            String restaurantPictureUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=600" + "&maxheight=300" +
                    "&photoreference=" + mPhotoId +
                    "&key=AIzaSyDqefrTQHVLLodQoTiQWHpIWRUofSV1SUw";

            Glide.with(this)
                    .load(restaurantPictureUrl)
                    .into(mRestaurantImageView);
        }
    }

    private void setRestaurantName(Result result) {
        mRestaurantNameText = findViewById(R.id.activity_restaurant_name);
        mRestaurantNameText.setText(result.getName());

    }

    private void setRestaurantAddress(Result result) {
        mRestaurantAddressText = findViewById(R.id.activity_restaurant_address);
        String vicinity = result.getFormattedAddress();
        String displayedAddress = vicinity.split(",")[0];
        mRestaurantAddressText.setText(displayedAddress);
    }

    // --------
    // Buttons
    // --------

    // Restaurant PhoneCall Button
    private void initRestaurantCallButton(Result result) {

        final String restaurantPhoneNumber = result.getFormattedPhoneNumber();

        mRestaurantCallButton = findViewById(R.id.activity_restaurant_button_call);
        mRestaurantCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurantPhoneNumber, null));

                if (ActivityCompat.checkSelfPermission(RestaurantActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

    }
    // Restaurant Website Button
    private void initRestaurantWebsiteButton(Result result) {

        final String restaurantWebsiteUrl = result.getWebsite();

        mRestaurantWebsiteButton = findViewById(R.id.activity_restaurant_button_website);
        mRestaurantWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (restaurantWebsiteUrl.isEmpty()) {
                    showNoWebsiteDialogError();

                }else{

                    Intent webViewIntent = new Intent(RestaurantActivity.this, WebViewActivity.class);
                    webViewIntent.putExtra("RestaurantWebViewUrl", restaurantWebsiteUrl);
                    startActivity(webViewIntent);
                }
            }
        });
    }

    // Like Button
    private void initRestaurantLikeButton(Result result) {

        final String chosenRestaurantId = result.getPlace_id();

        mRestaurantLikeButton = findViewById(R.id.activity_restaurant_button_like);
        mRestaurantLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Fetch Database to check if current Restaurant exists in it
                RestaurantHelper.getRestaurant(chosenRestaurantId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                        // If current restaurant is null, create document
                        if (restaurant == null) {

                            RestaurantHelper.createRestaurant(chosenRestaurantId, 1, 0);

                        // Else, fetch previous value and increment by 1
                        }else{

                            int numberOfLikes = restaurant.getNumberOfLikes();
                            RestaurantHelper.updateNumberOfLikes(chosenRestaurantId, numberOfLikes + 1);

                        }

                    }
                });

            }
        });

    }

    // Choose restaurant (Go) Button
    private void initGoButton(final Result result) {

        final String chosenRestaurantId = result.getPlace_id();

        mGoRestaurantButton = findViewById(R.id.restaurant_activity_go_button);
        mGoRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Fetch and check if Current User has already registered a restaurant, and prevent him from selecting the same one
                UserHelper.getChosenRestaurantId(getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        User currentUser = documentSnapshot.toObject(User.class);
                        String userChosenRestaurantId = currentUser.getChosenRestaurantId();

                        // Prevent user from registering twice on the same restaurant
                        if (userChosenRestaurantId.equals(chosenRestaurantId)) {

                            showGoDialogError();

                        }else{

                            showGoDialogInteractive(result, currentUser);

                        }
                    }

                });
            }


        });
    }

    // ----------
    // RECYCLER VIEW
    // ----------

    private void getAllUsersOnCurrentRestaurant(Result result) {

        final String restaurantId = result.getPlace_id();

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").whereEqualTo("chosenRestaurantId", mPlaceId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        // error
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        workmatesOnThatRestaurant.add(user);
                    }

                    Log.w("U ON THAT RESTAURANT", new GsonBuilder().setPrettyPrinting().create().toJson(workmatesOnThatRestaurant));
                    mWorkmatesAdapter.notifyDataSetChanged();
                    mWorkmatesAdapter.setFinalDisplayedDecidedUserText(222);

                }


        });

    }


    // ------------
    // ALERT DIALOG
    // ------------

    private void showNoWebsiteDialogError() {
        AlertDialogUtil dialogUtil = new AlertDialogUtil();
        dialogUtil.createAlertDialog(RestaurantActivity.this,
                getResources().getString(R.string.alertdialog_error),
                getResources().getString(R.string.alertdialog_content_no_website_error),
                getResources().getString(R.string.alertdialog_button_neutral));
    }

    private void showGoDialogError() {
        AlertDialogUtil dialogUtil = new AlertDialogUtil();
        dialogUtil.createAlertDialog(RestaurantActivity.this,
                getResources().getString(R.string.alertdialog_error),
                "You're already going to that restaurant",
                getResources().getString(R.string.alertdialog_button_neutral));
    }


    private void showGoDialogInteractive(final Result result, final User currentUser) {

        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this, R.style.AlertDialog_Style);
        // Set Title and Message content
        builder.setTitle("Go4Lunch");
        builder.setMessage("Are you sure you want to go4thatLunch?");
        // Positive button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final String chosenRestaurantId = result.getPlace_id();
                        final String chosenRestaurantName = result.getName();
                        final String chosenRestaurantIdField = currentUser.getChosenRestaurantId();

                        if (chosenRestaurantIdField.isEmpty()) {
                            // Push infos on Database (Restaurant ID, Restaurant Name, & Picture URL)
                            UserHelper.updateChosenRestaurantId(chosenRestaurantId, FirebaseAuth.getInstance().getCurrentUser().getUid());
                            UserHelper.updateChosenRestaurantName(chosenRestaurantName, FirebaseAuth.getInstance().getCurrentUser().getUid());
                            UserHelper.updateChosenRestaurantUrlPicture(mPhotoId, FirebaseAuth.getInstance().getCurrentUser().getUid());

                            // Check if current Restaurant is already registered in DB in which case increment numberOfPeopleJoining by 1, else create it anew
                            RestaurantHelper.getRestaurant(chosenRestaurantId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                                    if (restaurant == null) {
                                        RestaurantHelper.createRestaurant(chosenRestaurantId, 0, 1);

                                    } else {

                                        int numberOfPeopleJoining = restaurant.getNumberOfPeopleJoining();
                                        int numberOfLikes = restaurant.getNumberOfLikes();

                                        //RestaurantHelper.updateNumberOfPeopleJoining(chosenRestaurantIdField, numberOfPeopleJoining - 1);
                                        RestaurantHelper.updateNumberOfPeopleJoining(chosenRestaurantId, numberOfPeopleJoining + 1);
                                        RestaurantHelper.updateNumberOfLikes(chosenRestaurantId, numberOfLikes);

                                    }

                                }

                            });

                            // If current chosenRestaurantIdField isn't empty (User has a previously registered restaurant)
                        } else {

                            RestaurantHelper.getRestaurant(currentUser.getChosenRestaurantId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                                    if (restaurant != null) {

                                        int numberOfPeopleJoining2 = restaurant.getNumberOfPeopleJoining();
                                        int numberOfLikes = restaurant.getNumberOfLikes();

                                        // Remove 1 registered user from previous restaurant
                                        RestaurantHelper.updateNumberOfPeopleJoining(chosenRestaurantIdField, numberOfPeopleJoining2 - 1);

                                        // Update new infos into Users
                                        UserHelper.updateChosenRestaurantId(chosenRestaurantId, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        UserHelper.updateChosenRestaurantName(chosenRestaurantName, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        UserHelper.updateChosenRestaurantUrlPicture(mPhotoId, FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        // Create new restaurant entry into DB
                                        RestaurantHelper.createRestaurant(chosenRestaurantId, 0, numberOfPeopleJoining2);


                                    }else{

                                        //RestaurantHelper.createRestaurant(chosenRestaurantId, 0, 1);
                                    }



                                }

                            });


                        }
                    }
                });



        // Negative button
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        builder.show();
    }

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}