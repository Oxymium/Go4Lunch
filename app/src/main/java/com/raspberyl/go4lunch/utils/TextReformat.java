package com.raspberyl.go4lunch.utils;

public class TextReformat {

    public static String reformatWorkmateText(String userName, String cuisineType, String restaurantName) {

        String reformatedString = userName + " is eating" + cuisineType + restaurantName;
        String defaultString = userName + " hasn't decided yet";

        if (restaurantName.isEmpty()) {
            return defaultString;
        }

        else
            return reformatedString;
    }


}
