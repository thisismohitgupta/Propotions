package com.socketmill.thisismohit.propotions;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class Profile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();


        //Toast.makeText(getApplicationContext(), "explore", Toast.LENGTH_SHORT).show();

        //return super.onOptionsItemSelected();


        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_profile);


        toolbarNav navigation = new toolbarNav();
        navigation.PROFILE = true;
        navigation.setNav(getApplicationContext(), toolbar, this);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
