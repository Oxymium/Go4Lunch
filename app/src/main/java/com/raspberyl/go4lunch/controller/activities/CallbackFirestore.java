package com.raspberyl.go4lunch.controller.activities;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.raspberyl.go4lunch.model.firebase.Restaurant;
import com.raspberyl.go4lunch.model.googleplaces.Result;

import org.w3c.dom.Document;

import io.reactivex.ObservableEmitter;

public class CallbackFirestore implements OnSuccessListener<DocumentSnapshot> {

    private final ObservableEmitter<DocumentSnapshot> emitter;
    private final boolean last;
    private Result result;

    public CallbackFirestore(boolean lastValue, ObservableEmitter<DocumentSnapshot> e, Result result) {
        this.last = lastValue;
        this.emitter = e;
        this.result = result;
    }

    @Override
    public void onSuccess(DocumentSnapshot value) {
        Restaurant restaurant = value.toObject(Restaurant.class);
        if (restaurant != null) {
            result.setNumberOfLikes(restaurant.getNumberOfLikes());
        }
        if (last){
            emitter.onComplete();
        }

    }
}
