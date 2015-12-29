package com.socketmill.thisismohit.propotions;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.socketmill.thisismohit.propotions.background.ExploreImageAsync;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class Explore extends AppCompatActivity {


    LinearLayout LL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        getSupportActionBar().hide();

        LL = (LinearLayout)findViewById(R.id.linearLayout2);



        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());






        ParseQuery<ParseObject> MainQuery = this.TheExploreQuery() ;

        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if(e==null){

                    if (!list.isEmpty()){

                        for(int i = 0 ; i<list.size();i = i + 3 ) {
                           // TextView text = new TextView(getApplicationContext());

                           // ParseObject photo = list.get(i).getParseObject("photo");
                            //text.setText(list.get(i).getObjectId());



                            LinearLayout Rl = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams RLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                           // RLParams.setLayoutDirection();
                            Rl.setOrientation(LinearLayout.HORIZONTAL);
                            Rl.setLayoutParams(RLParams);
                            //final float Density = getApplicationContext().getResources().getDisplayMetrics().density;


                            float width = getApplicationContext().getResources().getDisplayMetrics().widthPixels ;

                            float imageWidth = (width / 3 )  ;
                            float imageHeight = imageWidth  ;





                            if (i<list.size()) {
                                ImageView image0 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams1 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams1.setMargins(0,2,2,2);

                                image0.setLayoutParams(ImageParams1);

                                ExploreImageAsync sync1 = new ExploreImageAsync(new WeakReference<ImageView>(image0), list.get(i).getParseObject("photo"), getApplicationContext());
                                sync1.execute();
                                Rl.addView(image0);

                            }





                            if (i+1<list.size()) {
                                ImageView image1 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams2 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams2.setMargins(2,2,0,2);
                                image1.setLayoutParams(ImageParams2);

                                ExploreImageAsync sync2 = new ExploreImageAsync(new WeakReference<ImageView>(image1), list.get(i + 1).getParseObject("photo"), getApplicationContext());
                                sync2.execute();

                                Rl.addView(image1);
                            }


                            if (i+2<list.size()) {
                                ImageView image2 = new ImageView(getApplicationContext());
                                RelativeLayout.LayoutParams ImageParams3 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
                                ImageParams3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                ImageParams3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                ImageParams3.setMargins(2,2,0,2);
                                image2.setLayoutParams(ImageParams3);

                                ExploreImageAsync sync3 = new ExploreImageAsync(new WeakReference<ImageView>(image2), list.get(i + 2).getParseObject("photo"), getApplicationContext());
                                sync3.execute();

                                Rl.addView(image2);
                            }

//
//                            if (i+3<list.size()) {
//                                ImageView image3 = new ImageView(getApplicationContext());
//                                RelativeLayout.LayoutParams ImageParams4 = new RelativeLayout.LayoutParams((int) (imageWidth), (int) (imageHeight));
//                                ImageParams4.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                                ImageParams4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//                                image3.setLayoutParams(ImageParams4);
//
//
//                                ExploreImageAsync sync4 = new ExploreImageAsync(new WeakReference<ImageView>(image3), list.get(i + 3).getParseObject("photo"), getApplicationContext());
//                                sync4.execute();
//
//                                Rl.addView(image3);
//                            }









                            LL.addView(Rl);

                        }



                    }else {

                        Toast.makeText(getApplicationContext(),"Nothing Found",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

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


    public void FollowUser (String ObjectIdUserToFollow){

        ParseObject follow = ParseObject.create("Activity");

        follow.put("toUser",ParseObject.createWithoutData(ParseUser.class,ObjectIdUserToFollow));
        follow.put("fromUser",ParseObject.createWithoutData(ParseUser.class,ParseUser.getCurrentUser().getObjectId()));

        follow.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    Log.e("ERROR", "fOLLOWED");
                }
            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private ParseQuery<ParseObject> TheExploreQuery () {

        //baseed on people User is following
        ParseQuery FollowingActivityQuery = ParseQuery.getQuery("Activity");
        FollowingActivityQuery.whereEqualTo("type", "follow");
        FollowingActivityQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());

        //photos from all the Users previously found
        ParseQuery<ParseObject> photosLikedByFollowedUserQueryLike = ParseQuery.getQuery("Activity");
        photosLikedByFollowedUserQueryLike.whereMatchesKeyInQuery("fromUser", "toUser", FollowingActivityQuery);
        photosLikedByFollowedUserQueryLike.whereEqualTo("type", "like");
        photosLikedByFollowedUserQueryLike.whereExists("photo");
        //photosLikedByFollowedUserQueryLike.whereNotEqualTo("toUser","");

        ParseQuery<ParseObject> photosLikedByFollowedUserQueryComment = ParseQuery.getQuery("Activity");
        photosLikedByFollowedUserQueryComment.whereMatchesKeyInQuery("fromUser", "toUser", FollowingActivityQuery);
        photosLikedByFollowedUserQueryComment.whereEqualTo("type", "comment");
        photosLikedByFollowedUserQueryComment.whereExists("photo");


        List<ParseQuery<ParseObject>> QueryList = new ArrayList<ParseQuery<ParseObject>>();
        QueryList.add(photosLikedByFollowedUserQueryLike);
        QueryList.add(photosLikedByFollowedUserQueryComment);

        ParseQuery<ParseObject> BasedOnPeopleUserFollows = ParseQuery.or(QueryList);







        //based Photos User has Already Liked
//
        //geting all the photos the user has liked
        ParseQuery LikeActivityQuery = ParseQuery.getQuery("Activity");
        LikeActivityQuery.whereEqualTo("type", "like");
        LikeActivityQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        //get other user who have liked the same pic we have liked
        ParseQuery<ParseObject> ByUsersWhoAlsoLikedThatPhoto = ParseQuery.getQuery("Activity");
        ByUsersWhoAlsoLikedThatPhoto.whereMatchesKeyInQuery("photo", "photo", LikeActivityQuery);
        ByUsersWhoAlsoLikedThatPhoto.whereNotEqualTo("fromUser", ParseUser.getCurrentUser());

        ByUsersWhoAlsoLikedThatPhoto.whereEqualTo("type", "like");
        //find the likes
        ParseQuery<ParseObject> PhotosLikesdBythoseUsers = ParseQuery.getQuery("Activity");
        PhotosLikesdBythoseUsers.whereMatchesKeyInQuery("fromUser","fromUser",ByUsersWhoAlsoLikedThatPhoto);
        PhotosLikesdBythoseUsers.whereEqualTo("type", "like");
        PhotosLikesdBythoseUsers.whereNotEqualTo("toUser", ParseUser.getCurrentUser());




        //getiing photos where user has commented
        ParseQuery CommentActivityQuery = ParseQuery.getQuery("Activity");
        CommentActivityQuery.whereEqualTo("type", "comment");
        CommentActivityQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());

        ParseQuery<ParseObject> ByUsersWhoAlsoCommentedThatPhoto = ParseQuery.getQuery("Activity");
        ByUsersWhoAlsoCommentedThatPhoto.whereMatchesKeyInQuery("photo", "photo", LikeActivityQuery);
        ByUsersWhoAlsoCommentedThatPhoto.whereNotEqualTo("fromUser", ParseUser.getCurrentUser());
        ByUsersWhoAlsoCommentedThatPhoto.whereEqualTo("type", "like");

        ParseQuery<ParseObject> PhotosCommentedBythoseUsers = ParseQuery.getQuery("Activity");
        PhotosCommentedBythoseUsers.whereMatchesKeyInQuery("fromUser", "fromUser", ByUsersWhoAlsoLikedThatPhoto);
        PhotosCommentedBythoseUsers.whereEqualTo("type", "like");
        PhotosCommentedBythoseUsers.whereNotEqualTo("toUser", ParseUser.getCurrentUser());




        List<ParseQuery<ParseObject>> listofQuerysBasedOnUserLikesAndComments = new ArrayList<ParseQuery<ParseObject>>();
        listofQuerysBasedOnUserLikesAndComments.add(ByUsersWhoAlsoCommentedThatPhoto);
        listofQuerysBasedOnUserLikesAndComments.add(PhotosCommentedBythoseUsers);

        ParseQuery<ParseObject> basedOnUsersLikesAndComments = ParseQuery.or(listofQuerysBasedOnUserLikesAndComments);





        List<ParseQuery<ParseObject>> UltraList = new ArrayList<ParseQuery<ParseObject>>();
        UltraList.add(basedOnUsersLikesAndComments);
       UltraList.add(BasedOnPeopleUserFollows);


        ParseQuery<ParseObject> TheExploreQueryNigga = ParseQuery.or(UltraList);

        TheExploreQueryNigga.include("photo");


        return TheExploreQueryNigga ;



    }



}

