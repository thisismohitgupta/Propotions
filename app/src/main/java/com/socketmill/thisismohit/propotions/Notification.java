package com.socketmill.thisismohit.propotions;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class Notification extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();


        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());


        ParseQuery<ParseObject> query = maketheNotimaketheNotificationQueryficationQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar2 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_nav);


        Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_SHORT).show();

        toolbarNav navigation2 = new toolbarNav();
        navigation2.NOTIFICATION = true;
        navigation2.setNav(getApplicationContext(), toolbar2, this);
        return true;
    }

    public static ParseQuery<ParseObject> maketheNotimaketheNotificationQueryficationQuery(){

        ParseQuery<ParseObject> NotificationQUery = new ParseQuery("Activity");
        NotificationQUery.whereEqualTo("toUser", ParseUser.getCurrentUser());
        NotificationQUery.whereNotEqualTo("fromUser", ParseUser.getCurrentUser());
        NotificationQUery.whereExists("fromUser");
        NotificationQUery.include("fromUser");
        NotificationQUery.orderByAscending("createdAt");


        return NotificationQUery;
    }

}

