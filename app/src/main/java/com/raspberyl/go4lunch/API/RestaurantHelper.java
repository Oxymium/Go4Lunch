package com.raspberyl.go4lunch.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raspberyl.go4lunch.model.firebase.Restaurant;
import com.raspberyl.go4lunch.model.firebase.User;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRestaurant(String restaurantName, int numberOfLikes) {
        // 1 - Create Obj
        Restaurant restaurantToCreate = new Restaurant(restaurantName, numberOfLikes);

        return RestaurantHelper.getRestaurantsCollection().document().set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurant(){
        return RestaurantHelper.getRestaurantsCollection().document().get();
    }

    // --- UPDATE ---

    public static Task<Void> updateRestaurantName(String restaurantName) {
        return RestaurantHelper.getRestaurantsCollection().document().update("restaurantName", restaurantName);
    }

    public static Task<Void> updateNumberOfLikes(int numberOfLikes) {
        return RestaurantHelper.getRestaurantsCollection().document().update("numberOfLikes", numberOfLikes);
    }

}
