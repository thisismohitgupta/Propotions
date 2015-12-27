package com.socketmill.thisismohit.propotions;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Profile;

/**
 * Created by thisismohit on 19/11/15.
 */
public class toolbarNav extends AppCompatActivity {

    public boolean HOME;
    public boolean EXPLORE;
    public boolean NOTIFICATION;
    public boolean VIDEO;
    public boolean PROFILE;
   // ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

    public void setNav(final Context context, final android.support.v7.widget.Toolbar toolbar, final Object activityRunning) {


        toolbar.inflateMenu(R.menu.menu_main);
        //final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom) ;


        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Toast.makeText(getApplicationContext(), String.valueOf(item.getItemId()), Toast.LENGTH_SHORT).show();

                if (item.getItemId() == toolbar.getMenu().getItem(1).getItemId() && EXPLORE != true) {

                    // Toast.makeText(context, "explore", Toast.LENGTH_SHORT).show();


                    //mngr.
                    Intent i = new Intent(context, Explore.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                } else if (item.getItemId() == toolbar.getMenu().getItem(2).getItemId() && VIDEO != true) {
                    //  Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();

                    //Videos vedio = new Videos();


                    Intent i = new Intent(context, Videos.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                } else if (item.getItemId() == toolbar.getMenu().getItem(3).getItemId() && NOTIFICATION != true) {
                    //Toast.makeText(context, "notification", Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(context, Notification.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                } else if (item.getItemId() == toolbar.getMenu().getItem(4).getItemId() && PROFILE != true) {



                    Intent i = new Intent(context, com.socketmill.thisismohit.propotions.Profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                } else if (item.getItemId() == toolbar.getMenu().getItem(0).getItemId() && HOME != true) {



                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }


                return false;
            }
        });

    }


    public void checkIfLoggedIn(Context context, Class aClass) {


        if (Profile.getCurrentProfile() == null) {

           // Toast.makeText(context, "not logged in", Toast.LENGTH_LONG).show();


        } else {

            //Toast.makeText(context, "logged in", Toast.LENGTH_LONG).show();

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }


    }

    public void checkIfLoggedInInternal(Context context) {


        if (Profile.getCurrentProfile() == null) {


            Intent i = new Intent(context, Login.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);


        } else {



        }


    }


}
