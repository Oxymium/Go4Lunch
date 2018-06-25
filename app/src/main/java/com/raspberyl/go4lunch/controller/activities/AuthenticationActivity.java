package com.raspberyl.go4lunch.controller.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;

import java.util.Arrays;

public class AuthenticationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9876;

    private Button mMailButton;
    private Button mFacebookButton;
    private Button mGooglePlusButton;
    private Button mTwitterButton;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        this.initButtonDemo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    private void initButtonDemo() {

        mMailButton = findViewById(R.id.authentication_button_mail);
        mFacebookButton = findViewById(R.id.authentication_button_facebook);
        mGooglePlusButton = findViewById(R.id.authentication_button_google);
        mTwitterButton = findViewById(R.id.authentication_button_twitter);

        mMailButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInEmail();
            }
        });

        mFacebookButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInFacebook();
            }
        });

        mGooglePlusButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInGooglePlus();
            }
        });

        mTwitterButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInTwitter();
            }
        });

    }

    // Transparent borders
    private void setTransparentTest() {
        Window w =getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    // Authentication mode: mail & password, Facebook, Google+ & Twitter
    private void startSignInEmail(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        // Mail & Password
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInFacebook(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        // Facebook
                                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                        //new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInGooglePlus(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        // GooglePlus
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        //new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInTwitter(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        // Twitter
                                        new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    // 2 - Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }



    // 3 - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                Toast.makeText(this, this.getString(R.string.authentication_connection_succeed), Toast.LENGTH_LONG).show();
                // createUserInFirestore();
                startMainActivity();
            } else { // ERRORS
                /*
                if (response == null) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.authentication_error_authentication_canceled));
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.authentication_error_no_internet));
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.mCoordinatorLayout, getString(R.string.authentication_error_unknown_error));
                } */
            }
        }
    }

    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.authentication_error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }


    private void startMainActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
