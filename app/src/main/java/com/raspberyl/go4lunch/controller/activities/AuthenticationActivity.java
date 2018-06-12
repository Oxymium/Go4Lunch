package com.raspberyl.go4lunch.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.raspberyl.go4lunch.R;

import java.util.Arrays;

public class AuthenticationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9876;

    private Button mMailButton;
    private Button mFacebookButton;
    private Button mGooglePlusButton;
    private Button mTwitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initButtonDemo();
        setTransparentTest();
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
                startSignInActivityMail();
            }
        });

        mFacebookButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mGooglePlusButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivityGoogle();
            }
        });

        mTwitterButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setTransparentTest() {

        Window w =getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    // Authentication mode: mail & password
    private void startSignInActivityMail(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        // Mail & Password
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_background)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityGoogle(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        // Google Plus
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.go4lunch_background)
                        .build(),
                RC_SIGN_IN);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                startMainActivity();
            } else { // ERRORS
                }
            }
        }

    private void startMainActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
