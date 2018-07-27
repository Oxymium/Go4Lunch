package com.raspberyl.go4lunch.controller.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.MainActivity;
import com.raspberyl.go4lunch.controller.activities.RestaurantActivity;
import com.raspberyl.go4lunch.model.googleplaces.Example;
import com.raspberyl.go4lunch.model.googleplaces.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;

    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 500;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    List<Result> restaurantsList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate map
        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        longitude = MainActivity.longitudeTest;
        latitude = MainActivity.latitudeTest;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setOnMyLocationButtonClickListener(this);
                mGoogleMap.setOnMyLocationClickListener(this);
                build_retrofit_and_get_response(longitude, latitude);

            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(this);
            mGoogleMap.setOnMyLocationClickListener(this);
            build_retrofit_and_get_response(longitude, latitude);


        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    // INTERFACE ///////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onLocationChanged", "entered");

        latitude = MainActivity.latitudeTest;
        longitude = MainActivity.longitudeTest;

        LatLng latLng = new LatLng(latitude, longitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        build_retrofit_and_get_response(longitude, latitude);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        /*
        latitude = location.getLatitude();
        longitude = location.getLongitude(); */
        latitude = MainActivity.latitudeTest;
        longitude = MainActivity.longitudeTest;
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(getString(R.string.marker_me));

        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_menu_white_24dp));
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_icon)));


        // Adding Marker to the Map
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        Log.d("onLocationChanged", "Exit");

    }

    //////////////////////////////////////////////////////////////////////////////////////////////


    private void build_retrofit_and_get_response(double longitude, double latitude) {

        String type = "restaurant";

        /*
        RetrofitCall retrofitCall = new RetrofitCall();
        retrofitCall.callRetrofit(latitude, longitude, PROXIMITY_RADIUS); */


        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleApiInterface service = retrofit.create(GoogleApiInterface.class);

        Call<Example> call = service.getNearbyRestaurants(type, latitude + "," + longitude, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                restaurantsList = response.body().getResults();
                // Log.w("Full「Top Stories」json", new GsonBuilder().setPrettyPrinting().create().toJson(response));

                try {

                    mGoogleMap.clear();

                    for (int i = 0; i < response.body().getResults().size(); i++) {

                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        final String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName + " : " + vicinity);
                        // Icon
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                        // Adding Marker to the Camera.
                        Marker m = mGoogleMap.addMarker(markerOptions);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                        /*mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                 final String markerId = marker.getId();

                                 Toast.makeText(getContext(), placeName, Toast.LENGTH_LONG).show();

                                return false;
                            }
                        }); */
                    }

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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String result = marker.getSnippet();
        String placeId = restaurantsList.get(0).getPlaceId();
        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        //Intent startRestaurantActivity = new Intent(getContext(), RestaurantActivity.class);
        return false;
    }

}




