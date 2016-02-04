package com.socketmill.thisismohit.propotions;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.socketmill.thisismohit.propotions.background.notificationImagesAsync;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;

import java.lang.ref.WeakReference;
import java.util.List;


public class Notification extends AppCompatActivity {

LinearLayout ll ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();

        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());

        ll = (LinearLayout)findViewById(R.id.notificationLL);
        ParseQuery<ParseObject> query = maketheNotificationQueryficationQuery();


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null){

                   // Toast.makeText(getApplicationContext(),String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
                    for(int i = 0;i<list.size();i++){
                        RelativeLayout Rl = new RelativeLayout(getApplicationContext());
                        TextView NameViewText = new TextView(Notification.this);
                        TextView notifiationText = new TextView(Notification.this);
                        ImageView profilePicView = new ImageView(Notification.this);
                        ImageView ThumbPicView = new ImageView(Notification.this);

                        int pixelPerDp = (int) (getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);

                        RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (pixelPerDp * 50));

                        RLParams.setMargins(0, pixelPerDp * 10, 0, pixelPerDp * 20);
                        Rl.setLayoutParams(RLParams);


                        RelativeLayout.LayoutParams nameViewParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        nameViewParam.setMargins(pixelPerDp * 60, pixelPerDp * 20, 0, 0);
                        NameViewText.setLayoutParams(nameViewParam);

                        NameViewText.setTextColor(Color.BLUE);

                        NameViewText.setTextSize(10);

                        NameViewText.setId((i * i * i) + 1);

                        RelativeLayout.LayoutParams ProViewParam = new RelativeLayout.LayoutParams(75, 75);

                        ProViewParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        profilePicView.setLayoutParams(ProViewParam);


                        RelativeLayout.LayoutParams notificationParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                        notifiationText.setLayoutParams(notificationParams);

                        notificationParams.addRule(RelativeLayout.RIGHT_OF, NameViewText.getId());

                        notificationParams.setMargins(0, pixelPerDp * 20, 0, 0);

                        notifiationText.setTextSize(10);

                        RelativeLayout.LayoutParams profilePicParam = new RelativeLayout.LayoutParams(pixelPerDp * 50, pixelPerDp * 50);

                        profilePicParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


                        profilePicView.setId((i * i * i) + 2);
                        profilePicView.setLayoutParams(profilePicParam);

                        RelativeLayout.LayoutParams ThumViewParam = new RelativeLayout.LayoutParams(75, 75);

                        ThumViewParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        ThumbPicView.setLayoutParams(ThumViewParam);




                        final WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(ThumbPicView);
                        final WeakReference<ImageView> ProfilePicReff = new WeakReference<ImageView>(profilePicView);

                        if (list.get(i).get("type").toString().equals("follow")){


                            try {

                                Bitmap UserThumb = null ;
                               // Bitmap UserThumb = diskCache.get(list.get(i).getParseUser("fromUser").getUsername() + "thumb");
                                notificationImagesAsync notificationBacground = new notificationImagesAsync(list.get(i),ProfilePicReff,imageViewReference);


                                if ( UserThumb == null ){


                                    notificationBacground.execute();

                                }else {

                                    profilePicView.setImageBitmap(UserThumb);




                                }



                            }catch (Exception e2 ){


                            }

                        }else {




                            try {

                                Bitmap UserThumb  = null; // = ThumbnailCache.DiskCache.get(list.get(i).getParseUser("fromUser").getUsername() + "thumb");
                                Bitmap PhotoThumb = null ; //`e.get(list.get(i).getParseObject("photo").getObjectId() + "thumb");
                                notificationImagesAsync notificationBacground = new notificationImagesAsync(list.get(i),ProfilePicReff,imageViewReference);


                                    if ( UserThumb == null ||PhotoThumb==null){


                                        notificationBacground.execute();

                                    }else {

                                        profilePicView.setImageBitmap(UserThumb);

                                        ThumbPicView.setImageBitmap(PhotoThumb);


                                    }



                            }catch (Exception e2 ){


                            }


                        }



                        Rl.addView(profilePicView);
                        Rl.addView(NameViewText);
                        Rl.addView(notifiationText);
                        Rl.addView(ThumbPicView);


                        ll.addView(Rl);


                        NameViewText.setText(list.get(i).getParseUser("fromUser").get("displayName").toString());





                        if(list.get(i).get("type").toString().equals("like")){

                            notifiationText.setText(" liked your photo");

                        }
                         if (list.get(i).get("type").toString().equals("follow")){

                            notifiationText.setText(" followed you");

                        }
                        if (list.get(i).get("type").toString().equals("comment")){

                            notifiationText.setText(" commented on your photo");

                        }







                    }

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar2 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_nav);


        final android.support.v7.widget.Toolbar topBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_top_nav);


        toolbarNav navigation2 = new toolbarNav();
        navigation2.NOTIFICATION = true;
        navigation2.setNav(getApplicationContext(), toolbar2, this);
        return true;
    }

    public static ParseQuery<ParseObject> maketheNotificationQueryficationQuery(){

        ParseQuery<ParseObject> NotificationQUery = new ParseQuery("Activity");
        NotificationQUery.whereEqualTo("toUser", ParseUser.getCurrentUser());
        NotificationQUery.whereNotEqualTo("fromUser", ParseUser.getCurrentUser());
        NotificationQUery.whereExists("fromUser");
        NotificationQUery.include("fromUser");
        NotificationQUery.setLimit(70);
        NotificationQUery.orderByDescending("createdAt");


        return NotificationQUery;
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

