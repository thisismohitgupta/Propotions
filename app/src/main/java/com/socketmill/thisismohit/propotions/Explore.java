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

                        for(int i = 0 ; i<list.size();i++) {
                           // TextView text = new TextView(getApplicationContext());

                           // ParseObject photo = list.get(i).getParseObject("photo");
                            //text.setText(list.get(i).getObjectId());


                            ImageView image = new ImageView(getApplicationContext());

                            RelativeLayout Rl = new RelativeLayout(getApplicationContext());
                            RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            Rl.setLayoutParams(RLParams);
                            RelativeLayout.LayoutParams ImageParams = new RelativeLayout.LayoutParams(175, 175);
                            ImageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            ImageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                            image.setLayoutParams(ImageParams);

                            ExploreImageAsync sync = new ExploreImageAsync(new WeakReference<ImageView>(image),list.get(i).getParseObject("photo"),getApplicationContext());
                            sync.execute();

                            Rl.addView(image);

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

