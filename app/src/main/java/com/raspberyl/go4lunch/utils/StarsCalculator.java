package com.raspberyl.go4lunch.utils;

public class StarsCalculator {

    // Take int value and convert to single number
    public static int convertLikesToStars(int numberOfLikes) {

        if (numberOfLikes >= 0 && numberOfLikes < 10) {
            return 1;
        }
        if (numberOfLikes >= 10 && numberOfLikes < 20) {
            return 2;
        }
        if (numberOfLikes >= 20) {
            return 3;

        }else{

            return 0;
        }
    }

}
