package com.socketmill.thisismohit.propotions;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.View;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.DeleteCallback;
import com.parse.FindCallback;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;




import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import com.socketmill.thisismohit.propotions.Home.Reactions;
import com.socketmill.thisismohit.propotions.Home.commentDetail;
import com.socketmill.thisismohit.propotions.Home.homeView;



public class MainActivity extends AppCompatActivity {

    LinearLayout ll;
    boolean likeFlag;
    boolean didRunOnce ;

     boolean ranonce;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());
        getSupportActionBar().hide();
        ll = (LinearLayout) findViewById(R.id.mainActivityLinearLayout);

        didRunOnce = true ;
        boolean runFromLocalDataBase = true;
        ParseQuery<ParseObject> MainQuery = this.PhotosToShowQueryMake(runFromLocalDataBase);

        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {

                    } else {

                        displayafterQuery(list,didRunOnce);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(didRunOnce|| ranonce) {
            didRunOnce = false ;
            ranonce = false ;
            ll = (LinearLayout) findViewById(R.id.mainActivityLinearLayout);


            ParseQuery<ParseObject> MainQuery = this.PhotosToShowQueryMake(didRunOnce);


            MainQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        if (list.isEmpty()) {

                        } else {
                            if (ll.getChildCount() > 0) {
                               ll.removeAllViews();
                            }
                           displayafterQuery(list,didRunOnce);
                        }


                        ParseObject.unpinAllInBackground("Photo", list, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    ParseObject.pinAllInBackground("Photo", list);
                                }
                            }
                        });
                    }

                }

            });


        } else {

            Toast.makeText(getApplicationContext(),"already ran once",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom);
        toolbarNav navigation = new toolbarNav();
        navigation.HOME = true;
        navigation.setNav(getApplicationContext(), toolbar, this);
        return true;
    }

    public ParseQuery<ParseObject> PhotosToShowQueryMake(boolean runfromlocaldatabase) {


        ParseQuery FollowingActivityQuery = ParseQuery.getQuery("Activity");
        FollowingActivityQuery.whereEqualTo("type", "follow");
        FollowingActivityQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());

        //photos from all the Users previously found
        ParseQuery photosFromFollowedUserQuery = ParseQuery.getQuery("Photo");
        photosFromFollowedUserQuery.whereMatchesKeyInQuery("user", "toUser", FollowingActivityQuery);
        photosFromFollowedUserQuery.whereExists("image");



        //second query for the current users own pictures
        ParseQuery photosFromCurrentUserQuery = ParseQuery.getQuery("Photo");
        photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        photosFromCurrentUserQuery.whereExists("image");

        if (runfromlocaldatabase) {
            photosFromCurrentUserQuery.fromLocalDatastore();
            photosFromFollowedUserQuery.fromLocalDatastore() ;

        }

        // Final mega query
        List<ParseQuery<ParseObject>> QueryList = new ArrayList<ParseQuery<ParseObject>>();
        QueryList.add(photosFromFollowedUserQuery);
        QueryList.add(photosFromCurrentUserQuery);
        ParseQuery<ParseObject> megaQuery = ParseQuery.or(QueryList);

        megaQuery.include("user");
        megaQuery.orderByDescending("createdAt");

        if (runfromlocaldatabase) {

            megaQuery.fromLocalDatastore();

        }
        return megaQuery;

    }




    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void displayafterQuery(final List<ParseObject> list,final boolean didRunonce) {


        for (int i = 0; i < list.size(); i++) {
            RelativeLayout Rl = new RelativeLayout(getApplicationContext());
            final ImageView imageView = new ImageView(MainActivity.this);
            ImageView profilePicView = new ImageView(MainActivity.this);
            TextView NameViewText = new TextView(MainActivity.this);
            final ImageView ShareButton = new ImageView(MainActivity.this);
            final ImageView LikeButton = new ImageView(MainActivity.this);
            final ImageView CommentButton = new ImageView(MainActivity.this);
            TextView LikeCount = new TextView(MainActivity.this);
            TextView CommentCount = new TextView(MainActivity.this);


            RelativeLayout UnderPhoto = new RelativeLayout(MainActivity.this);
            RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Rl.setLayoutParams(RLParams);

            Rl.setBackgroundColor(Color.WHITE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getApplicationContext().getResources().getDisplayMetrics().widthPixels, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams profilePicParam = new RelativeLayout.LayoutParams(75, 75);
            profilePicParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            profilePicParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


            profilePicView.setId((i * i * i) + 2);
            profilePicView.setLayoutParams(profilePicParam);


            NameViewText.setTextColor(Color.BLUE);
            NameViewText.setTextSize(17);
            NameViewText.setId((i * i * i) + 1);


            RelativeLayout.LayoutParams nameViewParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            nameViewParam.addRule(RelativeLayout.RIGHT_OF, profilePicView.getId());
            NameViewText.setLayoutParams(nameViewParam);


            params.addRule(RelativeLayout.BELOW, profilePicView.getId());
            imageView.setLayoutParams(params);


            RelativeLayout.LayoutParams UnderPhotoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            UnderPhotoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imageView.getId());
            UnderPhoto.setLayoutParams(UnderPhotoParams);
            UnderPhoto.setBackgroundColor(Color.WHITE);

            RelativeLayout.LayoutParams LikeButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LikeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LikeButton.setLayoutParams(LikeButtonParams);
            LikeButton.setId((i * i * i) + 5);

            RelativeLayout.LayoutParams LikeCountParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LikeCountParams.addRule(RelativeLayout.RIGHT_OF,LikeButton.getId());
            LikeCount.setLayoutParams(LikeCountParams);
            LikeCount.setId((i * i * i *i*i) + 7);




            RelativeLayout.LayoutParams CommentButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            CommentButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            CommentButton.setLayoutParams(CommentButtonParams);
            CommentButton.setId((i * i * i) * 11);

            RelativeLayout.LayoutParams CommentCountParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            CommentCountParams.addRule(RelativeLayout.RIGHT_OF, CommentButton.getId());
            CommentCount.setLayoutParams(CommentCountParams);
            CommentCount.setId((i * i * i *i*i) + 17);

            RelativeLayout.LayoutParams ShareButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ShareButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ShareButton.setLayoutParams(ShareButtonParams);
            ShareButton.setId((i * i * i) * 7);


            final WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(imageView);
            final WeakReference<ImageView> ProfilePicReff = new WeakReference<ImageView>(profilePicView);
            final WeakReference<TextView> NameTag = new WeakReference<TextView>(NameViewText);
            final WeakReference<ImageView> LikeButtonReff = new WeakReference<ImageView>(LikeButton);
            final WeakReference<ImageView> CommentButtonReff = new WeakReference<ImageView>(CommentButton);
            final WeakReference<ImageView> ShareButtonReff = new WeakReference<ImageView>(ShareButton);
            final WeakReference<TextView>  LikeCountREFF = new WeakReference<TextView>(LikeCount) ;
            final WeakReference<TextView>  CommentCountREFF = new WeakReference<TextView>(CommentCount) ;


            Rl.addView(profilePicView);
            Rl.addView(NameViewText);
            Rl.addView(imageView);
            UnderPhoto.addView(LikeButton);
            UnderPhoto.addView(LikeCount);
            UnderPhoto.addView(CommentCount);
            UnderPhoto.addView(CommentButton);
            UnderPhoto.addView(ShareButton);
            Rl.addView(UnderPhoto);
            ll.addView(Rl);

            Bitmap bitmaps = Login.getBitmapFromMemoryCacheLRU(list.get(i).getObjectId());
            final String ParseObjectId = list.get(i).getObjectId();

             String ParseUsernames;
             String ParseUserObjectIds  ;
            try {

                 ParseUsernames = list.get(i).getParseUser("user").fetchIfNeeded().getUsername();
                ParseUserObjectIds= list.get(i).getParseObject("user").fetchIfNeeded().getObjectId();

            } catch (Exception e){

                ParseUsernames = null ;
                ParseUserObjectIds = null;

            }
            final String ParseUsername = ParseUsernames;
            final String ParseUserObjectId = ParseUserObjectIds ;

            Bitmap ProThumb = Login.getBitmapFromMemoryCacheLRU(ParseUsernames + "thumb");
            String ProfileName = Login.getStringFromMemoryCache(ParseUsernames + "profileName");

            if (bitmaps == null) {

// check the disk
                bitmaps = Login.getBitmapFromMemoryCache(list.get(i).getObjectId());
                ProThumb = Login.getBitmapFromMemoryCache(ParseUsernames + "thumb");

                if (bitmaps == null) {
                    Log.e("Error", "Bitmap is null");
                    homeView myhome = new homeView(list.get(i), imageViewReference, ProfilePicReff, NameTag, MainActivity.this, LikeButtonReff, CommentButtonReff, ShareButtonReff,LikeCountREFF,CommentCountREFF);
                    myhome.execute();

                } else {

                    Login.setBitmapMemoryCacheLRU(list.get(i).getObjectId(), bitmaps);
                    if (ProThumb != null) {
                        Login.setBitmapMemoryCacheLRU(ParseUsername + "thumb", ProThumb);

                    }
                }
            }

            if (bitmaps != null) {


                Log.e("Error", "Bitmap is not null");
                Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), bitmaps);
                Drawable drawablePro = new BitmapDrawable(getApplicationContext().getResources(), ProThumb);
                //imageView.scale

                ImageView viss = imageViewReference.get();

                ImageView vissL = ProfilePicReff.get();

                NameViewText.setText(ProfileName);
                viss.setBackground(drawable);
                vissL.setBackground(drawablePro);


                bitmaps = null;


                //Perfect space for some optimization
                final ImageView LikeButtons = LikeButtonReff.get();
                final ImageView CommentButtons = CommentButtonReff.get();
                final ImageView ShareButtons = ShareButtonReff.get();
                final Reactions Reaction = new Reactions(list.get(i),false);
                Reaction.countTheNumberOfLikesOnParsePhoto(LikeCountREFF,"like");
                Reaction.countTheNumberOfLikesOnParsePhoto(CommentCountREFF,"comment");



                Reactions Reactionn = new Reactions(list.get(i),false);
                Reactionn.picLikedorNotCheck(list.get(i), LikeButtonReff, false, true, LikeCountREFF);

                CommentButton.setImageResource(R.drawable.chat20);
                ShareButton.setImageResource(R.drawable.share11);
                //////
                final int tempI = i;

                final boolean runfromLocalDatabase = false ;
                LikeButtons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                         Reactions Reactionn = new Reactions(list.get(tempI),false);
                        Reactionn.picLikedorNotCheck(list.get(tempI), LikeButtonReff, false, false,LikeCountREFF);
                        Reactionn.countTheNumberOfLikesOnParsePhoto(LikeCountREFF,"like");
                            //if he has not already liked




                    }
                });

                ShareButtons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                CommentButtons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getApplicationContext(), commentDetail.class);
                        i.putExtra("PhotoId", ParseObjectId);
                        i.putExtra("UserThumb", ParseUsername);
                        i.putExtra("UserObjectID", ParseUserObjectId);


//
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);


                    }
                });

            }


        }


    }

}


