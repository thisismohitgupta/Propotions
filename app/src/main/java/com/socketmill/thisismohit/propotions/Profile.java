package com.socketmill.thisismohit.propotions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.socketmill.thisismohit.propotions.Home.PhotoDetail;
import com.socketmill.thisismohit.propotions.background.ExploreImageAsync;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    LinearLayout LL;
    CircleImageView circle;
    TextView PostCount;
    TextView FollowersCount;
    TextView FollowingCount;
    ParseUser users;
    String ObjectIdUser;
    TextView displayNameInProfile;
    WeakReference<TextView> displayNameInProfileReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());

        circle = (CircleImageView) findViewById(R.id.profileProfilePic);
        PostCount = (TextView) findViewById(R.id.postscount);
        FollowersCount = (TextView) findViewById(R.id.followersCount);
        FollowingCount = (TextView) findViewById(R.id.followingCount);
        displayNameInProfile = (TextView) findViewById(R.id.displayNameInProfile);


        final WeakReference<CircleImageView> circleImageViewWeakReference = new WeakReference<CircleImageView>(circle);

        final WeakReference<TextView> PostCountReff = new WeakReference<TextView>(PostCount);
        displayNameInProfileReff = new WeakReference<TextView>(displayNameInProfile);

        final WeakReference<TextView> FollowersCountReff = new WeakReference<TextView>(FollowersCount);

        final WeakReference<TextView> FollowingCountReff = new WeakReference<TextView>(FollowingCount);


        String ParseUserID = getIntent().getStringExtra("parseUserId");
        ObjectIdUser = getIntent().getStringExtra("parseUserObjectId");


        ParseQuery<ParseObject> MainQuery;
        LL = (LinearLayout) findViewById(R.id.linearLayout3);
        if (ParseUserID == null) {
            MainQuery = this.TheExploreQuery(null, circleImageViewWeakReference, FollowersCountReff, FollowingCountReff);

        } else {

            MainQuery = this.TheExploreQuery(ParseUserID, circleImageViewWeakReference, FollowersCountReff, FollowingCountReff);
        }


        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {

                if (e == null) {

                    if (!list.isEmpty()) {

                        PostCountReff.get().setText(String.valueOf(list.size()));
                        for (int i = 0; i < list.size(); i = i + 3) {
                            // TextView text = new TextView(getApplicationContext());

                            // ParseObject photo = list.get(i).getParseObject("photo");
                            //text.setText(list.get(i).getObjectId());

                            final int j = i;


                            LinearLayout Rl = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams RLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            // RLParams.setLayoutDirection();
                            Rl.setOrientation(LinearLayout.HORIZONTAL);
                            Rl.setLayoutParams(RLParams);
                            //final float Density = getApplicationContext().getResources().getDisplayMetrics().density;


                            float width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

                            float imageWidth = (width / 3) - 1;
                            float imageHeight = imageWidth;


                            if (i < list.size()) {
                                ImageView image0 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams1 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams1.setMargins(0, 2, 2, 2);

                                image0.setLayoutParams(ImageParams1);

                                image0.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), PhotoDetail.class);
                                        i.putExtra("PhotoId", list.get(j).getObjectId());
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(i);

                                    }
                                });


                                ExploreImageAsync sync1 = new ExploreImageAsync(new WeakReference<ImageView>(image0), list.get(i), getApplicationContext());
                                sync1.execute();
                                Rl.addView(image0);


                            }


                            if (i + 1 < list.size()) {
                                ImageView image1 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams2 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams2.setMargins(2, 2, 0, 2);
                                image1.setLayoutParams(ImageParams2);

                                ExploreImageAsync sync2 = new ExploreImageAsync(new WeakReference<ImageView>(image1), list.get(i + 1), getApplicationContext());
                                sync2.execute();

                                Rl.addView(image1);


                                image1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), PhotoDetail.class);
                                        i.putExtra("PhotoId", list.get(j + 1).getObjectId());
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(i);

                                    }
                                });
                            }


                            if (i + 2 < list.size()) {
                                ImageView image2 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams3 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams3.setMargins(2, 2, 0, 2);
                                image2.setLayoutParams(ImageParams3);

                                ExploreImageAsync sync3 = new ExploreImageAsync(new WeakReference<ImageView>(image2), list.get(i + 2), getApplicationContext());
                                sync3.execute();

                                Rl.addView(image2);
                                image2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), PhotoDetail.class);
                                        i.putExtra("PhotoId", list.get(j + 2).getObjectId());
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(i);

                                    }
                                });
                            }


                            LL.addView(Rl);

                        }


                    } else {

                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });






















    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_profile);
        final android.support.v7.widget.Toolbar topBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_top_profile);


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

    private ParseQuery<ParseObject> TheExploreQuery(final String usernames, final WeakReference<CircleImageView> circleImageViewWeakReference, final WeakReference<TextView> followerReff, final WeakReference<TextView> followingreff) {

        ParseQuery<ParseObject> TheExploreQueryNigga = new ParseQuery<ParseObject>("Photo");
        TheExploreQueryNigga.include("user");

        if (usernames == null) {
            TheExploreQueryNigga.whereEqualTo("user", ParseUser.getCurrentUser());

            setProfilePicture(ParseUser.getCurrentUser(), circleImageViewWeakReference);
            getFollowersandFollowingCount(followerReff, followingreff);


            displayNameInProfileReff.get().setText(ParseUser.getCurrentUser().get("displayName").toString());


        } else {


            ParseQuery<ParseUser> TheExploreQuery = ParseUser.getQuery();
            try {


                TheExploreQuery.whereEqualTo("objectId", ObjectIdUser);

                // List<ParseUser> bling = TheExploreQuery.find() ;


                TheExploreQueryNigga.whereMatchesKeyInQuery("user", "objectId", TheExploreQuery);


                setupProfile(usernames, circleImageViewWeakReference, followerReff, followingreff);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return TheExploreQueryNigga;


    }

    private void setProfilePicture(ParseUser user, final WeakReference<CircleImageView> targetImageView) {


        final int densityPerPixel = (int) (getApplicationContext().getResources().getDisplayMetrics().density + 0.5f);
        final JniBitmapHolder bitmapHolder2 = new JniBitmapHolder();

        displayNameInProfileReff.get().setText(user.get("displayName").toString());


        try {

            if (ThumbnailCache.cache.get(user.fetchIfNeeded().getUsername()) == null) {
                ParseFile profilePic = user.getParseFile("profilePictureSmall");

                profilePic.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {

                        if (bytes != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = false;
                            Bitmap bro = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                            //bitmapHolder2 =
                            bitmapHolder2.storeBitmap(bro);
                            bitmapHolder2.scaleBitmap(densityPerPixel * 75, densityPerPixel * 75, JniBitmapHolder.ScaleMethod.BilinearInterpolation);

                            bro = bitmapHolder2.getBitmapAndFree();


                            targetImageView.get().setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), bro));
                        }
                    }
                });
            } else {

                targetImageView.get().setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), ThumbnailCache.cache.get(user.getUsername())));


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private void getFollowersandFollowingCount(final WeakReference<TextView> followerReff, final WeakReference<TextView> followingreff) {

        ParseUser OurUser;

        OurUser = users;
        if (OurUser == null) {

            OurUser = ParseUser.getCurrentUser();

        }

        final ParseQuery<ParseObject> followingQuery = new ParseQuery<ParseObject>("Activity");
        followingQuery.whereEqualTo("type", "follow");
        followingQuery.whereEqualTo("fromUser", OurUser);
        final ParseQuery<ParseObject> followerQuery = new ParseQuery<ParseObject>("Activity");
        followerQuery.whereEqualTo("type", "follow");
        followerQuery.whereEqualTo("toUser", OurUser);

        AsyncTask activity = new AsyncTask() {
            int folloCount, erCount;


            @Override
            protected Object doInBackground(Object[] params) {
                try {


                    Log.e("ERROR", "peackcock");
                    List<ParseObject> listFollowing = followingQuery.find();
                    List<ParseObject> listFollowers = followerQuery.find();
                    folloCount = listFollowing.size();
                    erCount = listFollowers.size();
                } catch (ParseException e) {
                    Log.e("ERROR", e.getMessage());
                }


                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                followerReff.get().setText(String.valueOf(erCount));
                followingreff.get().setText(String.valueOf(folloCount));

            }
        };

        activity.execute();


    }

    private void setupProfile(final String username, final WeakReference<CircleImageView> targetImageView, final WeakReference<TextView> followerCountReff, final WeakReference<TextView> followingCountReff) {


        final ParseQuery<ParseUser> TheExploreQuery2 = ParseUser.getQuery();
        AsyncTask mello = new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] params) {

                TheExploreQuery2.whereEqualTo("username", username);
                try {
                    users = TheExploreQuery2.getFirst();


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                setProfilePicture(users, targetImageView);
                getFollowersandFollowingCount(followerCountReff, followingCountReff);

            }
        };

        mello.execute();
    }

}
