package com.raspberyl.go4lunch.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.raspberyl.go4lunch.R;

public class SettingsActivity extends AppCompatActivity {

    public static boolean receiveNotifications = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch mNotificationsSwitch = findViewById(R.id.activity_settings_switch_notifications);
        mNotificationsSwitch.setChecked(true);

        mNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                receiveNotifications = false;
            }

        });
    }
}
