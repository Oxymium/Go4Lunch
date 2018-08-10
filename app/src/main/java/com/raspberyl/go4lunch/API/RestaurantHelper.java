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

    public static Task<Void> createRestaurant(String restaurantName, int numberOfLikes, int numberOfPeopleJoining) {
        // 1 - Create Obj
        Restaurant restaurantToCreate = new Restaurant(restaurantName, numberOfLikes, numberOfPeopleJoining);

        return RestaurantHelper.getRestaurantsCollection().document(restaurantName).set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurant(String restauId){
        return RestaurantHelper.getRestaurantsCollection().document(restauId).get();
    }

    public static Task<DocumentSnapshot> getNumberOfLikes(String restauId){
        return RestaurantHelper.getRestaurantsCollection().document(restauId).get();
    }

    public static Task<DocumentSnapshot> getNumberOfPeopleJoining(String restauId){
        return RestaurantHelper.getRestaurantsCollection().document(restauId).get();
    }


    // --- UPDATE ---

    public static Task<Void> updateRestaurantName(String restaurantName) {
        return RestaurantHelper.getRestaurantsCollection().document().update("restaurantName", restaurantName);
    }

    public static Task<Void> updateNumberOfLikes(String restauId, int numberOfLikes) {
        return RestaurantHelper.getRestaurantsCollection().document(restauId).update("numberOfLikes", numberOfLikes);
    }

    public static Task<Void> updateNumberOfPeopleJoining(String restauId, int numberOfPeopleJoining) {
        return RestaurantHelper.getRestaurantsCollection().document(restauId).update("numberOfPeopleJoining", numberOfPeopleJoining);
    }

}
