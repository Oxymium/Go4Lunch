package com.raspberyl.go4lunch.controller.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.RestaurantHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.MainActivity;
import com.raspberyl.go4lunch.controller.activities.RestaurantActivity;
import com.raspberyl.go4lunch.model.firebase.Restaurant;
import com.raspberyl.go4lunch.model.firebase.User;
import com.raspberyl.go4lunch.model.googleplaces.Example;
import com.raspberyl.go4lunch.model.googleplaces.Photo;
import com.raspberyl.go4lunch.model.googleplaces.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
    private int PROXIMITY_RADIUS = 800;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    private List<Result> restaurantsList;
    private List<User> mWorkmates;

    private HashMap<Marker, String> mHashMapId = new HashMap<Marker,String>();
    private HashMap<Marker, String> mHashMapPhoto = new HashMap<Marker,String>();



    private int increment;
    private int listSize;

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

        longitude = MainActivity.longitude;
        latitude = MainActivity.latitude;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setOnMyLocationButtonClickListener(this);
                mGoogleMap.setOnMyLocationClickListener(this);
                callRetrofitMaps(longitude, latitude, PROXIMITY_RADIUS);


            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(this);
            mGoogleMap.setOnMyLocationClickListener(this);
            callRetrofitMaps(longitude, latitude, PROXIMITY_RADIUS);



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

        latitude = MainActivity.latitude;
        longitude = MainActivity.longitude;

        LatLng latLng = new LatLng(latitude, longitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        callRetrofitMaps(longitude, latitude, PROXIMITY_RADIUS);
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
        latitude = MainActivity.latitude;
        longitude = MainActivity.longitude;
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

    public void callRetrofitMaps(double latitude, double longitude, int PROXIMITY_RADIUS) {

        mGoogleMap.clear();

        String type = "restaurant";
        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleApiInterface service = retrofit.create(GoogleApiInterface.class);

          /*
        longitudeTest = 0.107929;
        latitudeTest = 49.49437; */

        Call<Example> call = service.getNearbyRestaurants(type, 49.4944 + "," + 0.1079, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                try {

                    restaurantsList = response.body().getResults();
                    Log.w("MapsMarker LIST", new GsonBuilder().setPrettyPrinting().create().toJson(restaurantsList));
                    // increment = 1;
                    listSize = restaurantsList.size();
                    int listSizeTest = restaurantsList.size();
                    System.out.println("LISTSIZETEST" + listSizeTest);

                    Observable<DocumentSnapshot> observable = Observable.create(new ObservableOnSubscribe<DocumentSnapshot>() {
                        @Override
                        public void subscribe(ObservableEmitter<DocumentSnapshot> e) throws Exception {
                            for (Result result : restaurantsList) {
                                CallbackFirestore callbackInstance = new CallbackFirestore(e, result, "join");
                                RestaurantHelper.getNumberOfPeopleJoining(result.getPlaceId()).addOnSuccessListener(callbackInstance);
                            }
                        }
                    });
                    observable.subscribe(new Observer<DocumentSnapshot>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d("MapsCall::onComplete", "MapCall::OnSubscribe");

                        }

                        @Override
                        public void onNext(DocumentSnapshot documentSnapshot) {
                            Log.d("MapsCall::onNext", "MapCall::OnNext");

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("MapsCall::onError", "MapsCall::onError");
                        }

                        @Override
                        public void onComplete() {
                            Log.d("MapsCall::onComplete", "MapCall::OnComplete");
                            increment = 1;
                            generateMarkerOnMaps(restaurantsList);
                        }
                    });

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

    private class CallbackFirestore implements OnSuccessListener<DocumentSnapshot> {
        private final ObservableEmitter<DocumentSnapshot> emitter;
        private Result result;
        private String function;

        private CallbackFirestore(ObservableEmitter<DocumentSnapshot> e, Result result, String function) {
            this.emitter = e;
            this.result = result;
            this.function = function;
        }

        @Override
        public void onSuccess(DocumentSnapshot value) {
            Log.e("Success", value.toString());
            Restaurant restaurant = value.toObject(Restaurant.class);
            if (restaurant != null){
                if(function.equals("like"))
                    result.setNumberOfLikes(restaurant.getNumberOfLikes());
                else if(function.equals("join"))
                    result.setNumberOfPeopleJoining(restaurant.getNumberOfPeopleJoining());
            }
            if (increment == listSize) {
                Log.e("Complete", "Complete");
                emitter.onComplete();
            }
            increment++;
        }
    }


    private void generateMarkerOnMaps(List<Result> results) {

            int listSizeTest2 = results.size();
            System.out.println("ListSIZE for markers" + listSizeTest2);

            for (int i = 0; i < results.size(); i++) {
            Double lat = results.get(i).getGeometry().getLocation().getLat();
            Double lng = results.get(i).getGeometry().getLocation().getLng();
            String placeId = results.get(i).getPlaceId();
            String photoId = results.get(i).getPhotos().get(0).getPhotoReference();

            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if (results.get(i).getNumberOfPeopleJoining() > 0) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            Marker m = mGoogleMap.addMarker(markerOptions);

            mHashMapId.put(m, placeId);
            mHashMapPhoto.put(m, photoId);

        }
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

        String placeId = mHashMapId.get(marker);
        String photoId = mHashMapPhoto.get(marker);

        Intent startRestaurantActivity = new Intent(getContext(), RestaurantActivity.class);
        startRestaurantActivity.putExtra("restaurantId", placeId);
        startRestaurantActivity.putExtra("restaurantPicture", photoId);
        startActivity(startRestaurantActivity);

        return false;
    }

}




