package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import com.example.instagram.models.Post;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UOKQ6id4445rni2fBEpMoKggB2KNjsHVBSxB4JSd")
                .clientKey("F4gt18hFEXcoYrQYXngOLSf0kinhiJyDiBUFgNXt")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
