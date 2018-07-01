package com.raspberyl.go4lunch.controller.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.GoogleMapsClient;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.model.googledetails.Details;
import com.raspberyl.go4lunch.model.googledetails.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends AppCompatActivity {

    private Button mRestaurantCallButton, mRestaurantWebsiteButton, mRestaurantLikeButton;
    private TextView mRestaurantNameText, mRestaurantAddressText;

    private Result mResult;

    private String mPlaceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mPlaceId = getIntent().getStringExtra("restaurantId");
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

                    // set: name
                    setRestaurantName(mResult);
                    // set: address
                    setRestaurantAddress(mResult);
                    // init: restaurant call button (phone number)
                    initRestaurantCallButton(mResult);
                    // init: restaurant website button (webview)
                    initRestaurantWebsiteButton(mResult);




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

    // ------------------
    // TextViews
    // ------------------

    private void setRestaurantName(Result result) {
        mRestaurantNameText = findViewById(R.id.activity_restaurant_name);
        mRestaurantNameText.setText(result.getName());

    }

    private void setRestaurantAddress(Result result) {
        mRestaurantAddressText = findViewById(R.id.activity_restaurant_address);
        mRestaurantAddressText.setText(result.getFormattedAddress());
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

    private void initRestaurantLikeButton() {}

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