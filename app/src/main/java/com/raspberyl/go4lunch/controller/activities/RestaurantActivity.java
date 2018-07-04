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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.GoogleMapsClient;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.googledetails.Details;
import com.raspberyl.go4lunch.model.googledetails.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends AppCompatActivity {

    private Button mRestaurantCallButton, mRestaurantWebsiteButton, mRestaurantLikeButton;
    private FloatingActionButton mGoRestaurantButton;
    private TextView mRestaurantNameText, mRestaurantAddressText;
    private ImageView mRestaurantImageView;

    private Result mResult;

    private String mPlaceId, mPhotoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mPlaceId = getIntent().getStringExtra("restaurantId");
        mPhotoId = getIntent().getStringExtra("restaurantPicture");
        getPlaceDetails(mPlaceId);
        /*mRestaurantNameText = findViewById(R.id.activity_restaurant_name);
        mRestaurantNameText.setText(mResult.getName()); */

    }

    // ------------------
    // API
    // ------------------

    public void getPlaceDetails(String placeId) {

        GoogleApiInterface service = GoogleMapsClient.getClient().create(GoogleApiInterface.class);

        Call<Details> call = service.getRestaurantDetails(placeId);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                try {

                    mResult = response.body().getResult();
                    Log.w("RESTAURANT DETAILS", new GsonBuilder().setPrettyPrinting().create().toJson(mResult));

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
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(restaurantPhoneNumber));

                if (ActivityCompat.checkSelfPermission(RestaurantActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
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
    private void initRestaurantLikeButton() {}

    // Choose restaurant (Go) Button
    private void initGoButton(Result result) {

        final String chosenRestaurantId = result.getPlace_id();

        mGoRestaurantButton = findViewById(R.id.restaurant_activity_go_button);
        mGoRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelper.updateChosenRestaurantId(chosenRestaurantId, FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

        });
    }


    // ------------
    // ERROR DIALOG
    // ------------

    public void showNoWebsiteDialogError() {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this, R.style.AlertDialog_Style);
        // Set Title and Message content
        builder.setTitle(R.string.alertdialog_error);
        builder.setMessage(getText(R.string.alertdialog_content_no_website_error));
        // Neutral button
        builder.setNeutralButton(R.string.alertdialog_button_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }



}