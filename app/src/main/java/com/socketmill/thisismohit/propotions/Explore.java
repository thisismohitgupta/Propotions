package com.socketmill.thisismohit.propotions;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Explore extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        getSupportActionBar().hide();


        Toast.makeText(getApplicationContext(), "explore", Toast.LENGTH_SHORT).show();

        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());


        //ParseQuery Query = ParseQuery.getQuery("Activity");
        ParseObject follow = ParseObject.create("Activity");

        follow.put("toUser",ParseObject.createWithoutData(ParseUser.class,"J7tLKZaxJP"));
        follow.put("fromUser",ParseObject.createWithoutData(ParseUser.class,ParseUser.getCurrentUser().getObjectId()));

        follow.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){

                    Log.e("ERROR","fOLLOWED");
                }
            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_explore);


        toolbarNav navigation = new toolbarNav();
        navigation.EXPLORE = true;
        navigation.setNav(getApplicationContext(), toolbar, this);
        return true;
    }


}

