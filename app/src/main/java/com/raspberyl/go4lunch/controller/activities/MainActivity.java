package com.raspberyl.go4lunch.controller.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.GoogleMapsClient;
import com.raspberyl.go4lunch.API.RetrofitCall;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.fragment.MapFragment;
import com.raspberyl.go4lunch.fragment.RestaurantsFragment;
import com.raspberyl.go4lunch.fragment.WorkmatesFragment;
import com.raspberyl.go4lunch.model.User;
import com.raspberyl.go4lunch.model.googlemaps.Example;
import com.raspberyl.go4lunch.model.googlemaps.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 9876;
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;

    private FirebaseFirestore mFirebaseFirestore;
    private List<User> mWorkmates;

    private TextView mHeaderTextUsername, mHeaderTextUsermail;
    private ImageView mHeaderImageUserpicture;

    private static final int SIGN_OUT_TASK = 10;

    private static final int REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 333;
    private static final int REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE = 444;
    private static final int REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE = 555;

    private FusedLocationProviderClient mFusedLocationClient;

    protected Location mLastLocation;

    // Minimum recommended by Android = 1min
    protected static long MIN_UPDATE_INTERVAL = 60 * 1000;
    private int PROXIMITY_RADIUS = 10000;

    LocationRequest locationRequest;
    Location lastLocation = null;
    Location currentLocation = null;

    public double latitudeTest;
    private double longitudeTest;

    public List<Result> listTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitudeTest = 0.107929;
        latitudeTest = 49.49437;

        //1 - Configuring Toolbar
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.updateDrawerWithPersonalData();
        this.getAllUsers();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkForLocationRequest();
        checkForLocationSettings();
        callCurrentLocation();

        this.initMapFragment();
    }

    // NAVIGATION

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

                // Drawer: Your Lunch
            case R.id.drawer_your_lunch:
                Toast.makeText(this, "lunch", Toast.LENGTH_LONG).show();
                break;

                // Drawer: Settings
            case R.id.drawer_settings:
                startSettingsActivity();
                break;

                // Drawer: Logout
            case R.id.drawer_logout:
                Toast.makeText(this, "logout", Toast.LENGTH_LONG).show();
                this.logoutUserFromFirebase();
                break;

                // Bottom Toolbar: MapView
            case R.id.bottom_map_view:
                initMapFragment();
                Toast.makeText(this, "buttonmap", Toast.LENGTH_LONG).show();
                mToolbar.setTitle(R.string.toolbar_map_title);
                break;

                // Bottom Toolbar: ListView
            case R.id.bottom_list_view:
                callRetrofit(latitudeTest, longitudeTest, PROXIMITY_RADIUS);
                Toast.makeText(this, "list VIEW", Toast.LENGTH_LONG).show();
                mToolbar.setTitle(R.string.toolbar_map_title);
                break;

                // Bottom Toolbar: Workmates
            case R.id.bottom_workmates:
                getAllUsers();
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

    // Start SettingsActivity
    private void startSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void logoutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                }
            }
        };
    }

    //////////////
    ///// Username, Addresse & Picture
    /////////////

    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    // Updates NavigationDrawer's header with user infos (username, mail & profile picture)
    private void updateDrawerWithPersonalData() {

        View header = mNavigationView.getHeaderView(0);
        mHeaderTextUsername = header.findViewById(R.id.header_user_name);
        mHeaderTextUsermail = header.findViewById(R.id.header_user_mail);
        mHeaderImageUserpicture = header.findViewById(R.id.header_user_picture);

        if (this.getCurrentUser().getPhotoUrl() != null) {
            Glide.with(this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mHeaderImageUserpicture);
        }

        if (this.getCurrentUser() != null) {
            String username = getCurrentUser().getDisplayName();
            String usermail = getCurrentUser().getEmail();
            mHeaderTextUsername.setText(username);
            mHeaderTextUsermail.setText(usermail);
        }
    }

    //////////////
    ///// LIST GETTER TEST
    ////////////

    private void getAllUsers() {

        mWorkmates = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mFirebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    // error
                }

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    User user = doc.getDocument().toObject(User.class);
                    mWorkmates.add(user);

                }

                Bundle testbundle = new Bundle();
                testbundle.putString("userListTest", new Gson().toJson(mWorkmates));
                Log.w("USERLISTTEST", new GsonBuilder().setPrettyPrinting().create().toJson(mWorkmates));


                WorkmatesFragment mWorkmatesFragment = new WorkmatesFragment();
                mWorkmatesFragment.setArguments(testbundle);
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mWorkmatesFragment).commit();

            }

    });


    }



    //////////////
    ///// FRAGMENTS
    /////////////

    private void initMapFragment() {
        MapFragment mMapFragment = new MapFragment();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mMapFragment).commit();
    }

    private void initRestaurantsFragment() {
        RestaurantsFragment mRestaurantFragment = new RestaurantsFragment();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mRestaurantFragment).commit();
    }

    private void initWorkmatesFragment() {
        WorkmatesFragment mWorkmatesFragment = new WorkmatesFragment();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mWorkmatesFragment).commit();
    }

    /////////
    //// LOCATION
    /////////

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS && result != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Toast.makeText(this, "Are you running in Emulator ? try a real device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Last Known Location
    public void callLastKnownLocation(View view) {
        try {
            if (
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE);
                return;
            }

            getLastLocation();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Current Location
    public void callCurrentLocation() {
        try {
            if (
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE);
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    currentLocation = (Location) locationResult.getLastLocation();

                    String result = "Current Location Latitude is " +
                            currentLocation.getLatitude() + "\n" +
                            "Current location Longitude is " + currentLocation.getLongitude();

                    //////////////////// resultTextView.setText(result);
                    latitudeTest = currentLocation.getLatitude();
                    longitudeTest = currentLocation.getLongitude();
                    System.out.println("LONG & LATITUDE" + "" + latitudeTest + "" + longitudeTest);
                    callRetrofit(latitudeTest, longitudeTest, PROXIMITY_RADIUS);
                }
            }, Looper.myLooper());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            String result = "Last known Location Latitude is " +
                                    mLastLocation.getLatitude() + "\n" +
                                    "Last known longitude Longitude is " + mLastLocation.getLongitude();

                            /////////////////// resultTextView.setText(result);
                            latitudeTest = currentLocation.getLatitude();
                            longitudeTest = currentLocation.getLongitude();
                            System.out.println("LONG & LATITUDE" + "" + latitudeTest + "" + longitudeTest);
                            callRetrofit(latitudeTest, longitudeTest, PROXIMITY_RADIUS);
                        } else {
                            showSnackbar("No Last known location found. Try current location..!");
                        }
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }


    private void showSnackbar(final String mainTextString, final String actionString,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                mainTextString,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionString, listener).show();
    }

    private void startLocationPermissionRequest(int requestCode) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    private void requestPermissions(final int requestCode) {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackbar("Permission is must to find the location", "Ok",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest(requestCode);
                        }
                    });

        } else {
            startLocationPermissionRequest(requestCode);
        }
    }

    public void checkForLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(MIN_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //Check for location settings.
    public void checkForLocationSettings() {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.addLocationRequest(locationRequest);
            SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);

            settingsClient.checkLocationSettings(builder.build())
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            //Setting is success...
                            Toast.makeText(MainActivity.this, "Enabled the Location successfully. Now you can press the buttons..", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(MainActivity.this, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                                    } catch (IntentSender.SendIntentException sie) {
                                        sie.printStackTrace();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    Toast.makeText(MainActivity.this, "Setting change is not available.Try in another device.", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            }
        }

        if (requestCode == REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCurrentLocation();
            }
        }
    }

    ////////////////
    //// RETROFIT
    ////////////////

    private void buildRetrofitAndGetResponse() {
        // Test variables
        double longitude = 0.107929;
        double latitude = 49.49437;


    }

    public void callRetrofit(double latitudeTest, double longitudeTest, int PROXIMITY_RADIUS) {

        GoogleApiInterface service = GoogleMapsClient.getClient().create(GoogleApiInterface.class);

        Call<Example> call = service.getNearbyRestaurants("restaurant", latitudeTest + "," + longitudeTest, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                List<Result> listTest = response.body().getResults();
                // Log.w("Nearby Restaurants", new GsonBuilder().setPrettyPrinting().create().toJson(listTest));

                Bundle bundle = new Bundle();
                bundle.putString("valuesArray", new Gson().toJson(listTest));

                RestaurantsFragment mRestaurantsFragment = new RestaurantsFragment();
                mRestaurantsFragment.setArguments(bundle);
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction().replace(R.id.activity_main_frame_layout, mRestaurantsFragment).commit();



                try {


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });

    }



}