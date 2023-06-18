package com.bedroom412.ylgy.view;

public interface DurationListener {

    default void onProcessing(int index, long duration) {

    }


    default void onProcessing(long duration) {
        onProcessing(-1, duration);
    }


}
