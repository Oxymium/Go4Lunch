package com.raspberyl.go4lunch.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.raspberyl.go4lunch.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationActivity extends AppCompatActivity {

    private Button mMailButton;
    private Button mFacebookButton;
    private Button mGooglePlusButton;
    private Button mTwitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initButtonDemo();
    }

    private void initButtonDemo() {

        mMailButton = findViewById(R.id.authentication_button_mail);
        mFacebookButton = findViewById(R.id.authentication_button_facebook);
        mGooglePlusButton = findViewById(R.id.authentication_button_google);
        mTwitterButton = findViewById(R.id.authentication_button_twitter);

        mMailButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(intent);
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
}
