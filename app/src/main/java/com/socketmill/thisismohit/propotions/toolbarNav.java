package com.socketmill.thisismohit.propotions;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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


        //toolbar.inflateMenu(R.menu.menu_main);
        //final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom) ;


        //toolbar.inflateMenu(R.layout.bottom_toolbar);


        RelativeLayout Rl = (RelativeLayout) toolbar.getChildAt(0);

        Rl.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (HOME != true) {


                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }
            }
        });


        Rl.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (EXPLORE != true) {


                    Intent i = new Intent(context, Explore.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }
            }
        });


        Rl.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (VIDEO != true) {





                    Intent i = new Intent(context, Videos.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }
            }
        });

        Rl.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NOTIFICATION != true) {





                    Intent i = new Intent(context, Notification.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }
            }
        });

        Rl.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (PROFILE != true) {


                    Intent i = new Intent(context, com.socketmill.thisismohit.propotions.Profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);


                }
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
