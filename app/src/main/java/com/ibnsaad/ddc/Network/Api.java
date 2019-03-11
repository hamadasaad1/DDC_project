package com.ibnsaad.ddc.Network;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class Api extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());
    }
}
