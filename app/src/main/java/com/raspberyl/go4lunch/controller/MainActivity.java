package com.raspberyl.go4lunch.controller;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.fragment.MapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1 - Configuring Toolbar
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();

        MapFragment mMapFragment = new MapFragment();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mMapFragment).commit();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {

            case R.id.drawer_your_lunch:
                Toast.makeText(this, "lunch", Toast.LENGTH_LONG).show();
                break;

            case R.id.drawer_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;

            case R.id.drawer_logout:
                Toast.makeText(this, "logout", Toast.LENGTH_LONG).show();
                break;

            case R.id.bottom_map_view:
                Toast.makeText(this, "buttonmap", Toast.LENGTH_LONG).show();
                mToolbar.setTitle(R.string.toolbar_map_title);
                break;

            case R.id.bottom_list_view:
                Toast.makeText(this, "list VIEW", Toast.LENGTH_LONG).show();
                mToolbar.setTitle(R.string.toolbar_map_title);
                break;

            case R.id.bottom_workmates:
                Toast.makeText(this, "work VIEW", Toast.LENGTH_LONG).show();
                mToolbar.setTitle(R.string.toolbar_workmates_title);
                break;


            default:
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // ----

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.mNavigationView = findViewById(R.id.activity_main_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    // 4 - Configure BottomNavigationView
    private void configureBottomNavigationView() {
        this.mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    // 5 -
}