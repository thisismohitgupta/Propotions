package com.socketmill.thisismohit.propotions;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Parse.initialize(this, "id", "key");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "dWGkmeIID8GkTZD07E3EM4if5Fu7nyVM0Av1WqCd", "QyBJNAvOEsIgTbbKlNNOLw3uFJIWArVMs9Wb2QFQ");


    }
}