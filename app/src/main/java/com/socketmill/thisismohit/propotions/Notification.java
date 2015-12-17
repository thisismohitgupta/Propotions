package com.socketmill.thisismohit.propotions;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;


public class Notification extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();

        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());


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


}

