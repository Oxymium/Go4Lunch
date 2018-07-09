package com.raspberyl.go4lunch.controller.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;
import com.raspberyl.go4lunch.API.GoogleApiInterface;
import com.raspberyl.go4lunch.API.GoogleMapsClient;
import com.raspberyl.go4lunch.API.UserHelper;
import com.raspberyl.go4lunch.R;
import com.raspberyl.go4lunch.controller.activities.MainActivity;
import com.raspberyl.go4lunch.model.firebase.User;
import com.raspberyl.go4lunch.model.googledetails.Details;
import com.raspberyl.go4lunch.model.googledetails.Result;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "FIREBASEOC";
    private Result mMyLunch;
    private List<User> workmatesOnThatRestaurant;

    private String myLunchName, myLunchAddress, myLunchWorkmates;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            // Get message sent by Firebase
            String message = remoteMessage.getNotification().getBody();
            // Show message in console
            Log.e("TAG", message);

            if (SettingsActivity.receiveNotifications) {
                getChosenRestaurantId();
            } else {

            }

        }
    }

    private void sendVisualNotification(String myLunchName, String myLunchAddress) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(myLunchName);
        inboxStyle.addLine(myLunchAddress);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.go4lunch_logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(myLunchName + " " + myLunchAddress)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    private void getChosenRestaurantId() {

        UserHelper.getChosenRestaurantId(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User currentUser = documentSnapshot.toObject(User.class);
                String userChosenRestaurantId = currentUser.getChosenRestaurantId();

                // Retrofit Call + Restaurant ID if it exists
                if (!userChosenRestaurantId.isEmpty()) {
                    checkYourLunchDetails(userChosenRestaurantId);
                    getAllUsersOnCurrentRestaurant(userChosenRestaurantId);
                    // Else, dsplay AlertDialog with no restaurant
                } else {

                }
            }

        });
    }

    private void checkYourLunchDetails(String placeId) {

        GoogleApiInterface service = GoogleMapsClient.getClient().create(GoogleApiInterface.class);

        Call<Details> call = service.getRestaurantDetails(placeId);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                try {

                    mMyLunch = response.body().getResult();
                    //Log.w("RESTAURANT DETAILS", new GsonBuilder().setPrettyPrinting().create().toJson(mMyLunch));
                    sendVisualNotification(mMyLunch.getName(), mMyLunch.getFormattedAddress());

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

    private void getAllUsersOnCurrentRestaurant(String placeId) {

        workmatesOnThatRestaurant = new ArrayList<>();

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").whereEqualTo("chosenRestaurantId", placeId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    // error
                }

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    User user = doc.toObject(User.class);
                    workmatesOnThatRestaurant.add(user);
                }

                Log.w("U ON THAT RESTAURANT", new GsonBuilder().setPrettyPrinting().create().toJson(workmatesOnThatRestaurant));

            }


        });

    }

    private void setMyLunchName(String myLunchName){
        this.myLunchName=myLunchName;
    }

    private void setMyLunchAddress(String myLunchAddress){
        this.myLunchAddress=myLunchAddress;
    }

    private void setMyLunchWorkmates(String myLunchWorkmates){
        this.myLunchWorkmates=myLunchWorkmates;
    }





    }

