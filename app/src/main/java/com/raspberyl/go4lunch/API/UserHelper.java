package com.raspberyl.go4lunch.API;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raspberyl.go4lunch.model.firebase.User;

import java.util.List;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String chosenRestaurantId, String chosenRestaurantName, String chosenRestaurantUrlPicture) {

        User userToCreate = new User(uid, username, urlPicture, chosenRestaurantId, chosenRestaurantName, chosenRestaurantUrlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<DocumentSnapshot> getChosenRestaurantId(String chosenRestaurantId) {
        return UserHelper.getUsersCollection().document(chosenRestaurantId).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateChosenRestaurantId(String chosenRestaurantId, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("chosenRestaurantId", chosenRestaurantId);
    }

    public static Task<Void> updateChosenRestaurantName(String chosenRestaurantName, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("chosenRestaurantName", chosenRestaurantName);
    }

    public static Task<Void> updateChosenRestaurantUrlPicture(String chosenRestaurantUrlPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("chosenRestaurantUrlPicture", chosenRestaurantUrlPicture);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
