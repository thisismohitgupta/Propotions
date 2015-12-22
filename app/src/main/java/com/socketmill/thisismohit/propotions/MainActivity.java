package com.socketmill.thisismohit.propotions;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import org.w3c.dom.Comment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.socketmill.thisismohit.propotions.Home.PhotoDetail;
import com.socketmill.thisismohit.propotions.Home.commentDetail;
import com.socketmill.thisismohit.propotions.Home.homeView;


public class MainActivity extends AppCompatActivity {

     LinearLayout ll ;
    boolean likeFlag ;
    private static LruCache<String,Bitmap> MemCache ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ll = (LinearLayout)findViewById(R.id.mainActivityLinearLayout);

        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());

        ParseQuery<ParseObject> MainQuery = this.PhotosToShowQueryMake();

        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                        Log.e("ERROR", "PAPU");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            RelativeLayout Rl = new RelativeLayout(getApplicationContext());
                            final ImageView imageView = new ImageView(MainActivity.this);
                            ImageView profilePicView = new ImageView(MainActivity.this);
                            TextView NameViewText = new TextView(MainActivity.this);
                            final ImageView ShareButton = new ImageView(MainActivity.this);
                            final ImageView LikeButton = new ImageView(MainActivity.this);
                            final ImageView CommentButton = new ImageView(MainActivity.this);


                            RelativeLayout UnderPhoto = new RelativeLayout(MainActivity.this);
                            RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            //RLParams.addRule(RelativeLayout);
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


                            RelativeLayout.LayoutParams CommentButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            CommentButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            CommentButton.setLayoutParams(CommentButtonParams);
                            CommentButton.setId((i * i * i) * 11);


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


                            Rl.addView(profilePicView);
                            Rl.addView(NameViewText);
                            Rl.addView(imageView);
                            UnderPhoto.addView(LikeButton);
                            UnderPhoto.addView(CommentButton);
                            UnderPhoto.addView(ShareButton);
                            Rl.addView(UnderPhoto);
                            ll.addView(Rl);

                            Bitmap bitmaps = Login.getBitmapFromMemoryCache(list.get(i).getObjectId());
                            final String ParseObjectId = list.get(i).getObjectId() ;
                            final String ParseUsername = list.get(i).getParseUser("user").getUsername() ;
                            final String ParseUserObjectId = list.get(i).getParseObject("user").getObjectId();
                            Bitmap ProThumb = Login.getBitmapFromMemoryCache(list.get(i).getParseUser("user").getUsername() + "thumb");
                            String ProfileName = Login.getStringFromMemoryCache(list.get(i).getParseUser("user").getUsername()+"profileName");

                            if (bitmaps == null) {
                                homeView myhome = new homeView(list.get(i), imageViewReference, ProfilePicReff, NameTag, MainActivity.this, LikeButtonReff, CommentButtonReff, ShareButtonReff);
                                myhome.execute();

                            } else {

                                Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), bitmaps);
                                Drawable drawablePro = new BitmapDrawable(getApplicationContext().getResources(), ProThumb);
                                //imageView.scale

                                ImageView viss = imageViewReference.get() ;

                                ImageView vissL = ProfilePicReff.get() ;

                                NameViewText.setText(ProfileName);
                                viss.setBackground(drawable);
                                vissL.setBackground(drawablePro);



                                Log.e("ERROR", "Cache Bitmap");


                                //Perfect space for some optimization
                                final ImageView LikeButtons = LikeButtonReff.get();
                                final ImageView CommentButtons = CommentButtonReff.get();
                                final ImageView ShareButtons = ShareButtonReff.get();

                                likeFlag = picLikedorNotCheck(list.get(i),getApplicationContext(),LikeButtonReff);


                                CommentButton.setImageResource(R.drawable.chat20);
                                ShareButton.setImageResource(R.drawable.share11);
                                //////
                                final int tempI = i ;

                                LikeButtons.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (!picLikedorNotCheck(list.get(tempI),getApplicationContext(),LikeButtonReff)) {
                                            LikeButton.setImageResource(R.drawable.heart13);
                                            likeFlag = true ;
                                            //user has liked the photo :)
                                            likeThatpic(list.get(tempI),getApplicationContext());


                                        }else {
                                            LikeButton.setImageResource(R.drawable.loving34);

                                            //user has unliked the photo :(
                                           unlikeThatpic(list.get(tempI),getApplicationContext());

                                        }


                                        Toast.makeText(getApplicationContext(), String.valueOf(LikeButton.getId()), Toast.LENGTH_SHORT).show();

                                    }
                                });

                                ShareButtons.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), String.valueOf(CommentButton.getId()), Toast.LENGTH_SHORT).show();

                                    }
                                });

                                CommentButtons.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent i = new Intent(getApplicationContext(), commentDetail.class);
                                        i.putExtra("PhotoId",ParseObjectId);
                                        i.putExtra("UserThumb",ParseUsername);
                                        i.putExtra("UserObjectID",ParseUserObjectId);


//
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(i);


                                    }
                                });

                            }


                            Toast.makeText(getApplicationContext(), list.get(i).getObjectId(), Toast.LENGTH_LONG);
                            Log.e("ERROR", list.get(i).getObjectId());
                        }

                        }

                } else {

                    Log.e("ERROR", e.getMessage());

                }


            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom);
        toolbarNav navigation = new toolbarNav();
        navigation.HOME = true;
        navigation.setNav(getApplicationContext(), toolbar, this);
        return true;
    }

    public ParseQuery<ParseObject> PhotosToShowQueryMake() {


        //First query for Users all followed friend's photos

        //FIRST find all the users , current user has followed from the activity class
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

        // Final mega query
        List<ParseQuery<ParseObject>> QueryList = new ArrayList<ParseQuery<ParseObject>>();
        QueryList.add(photosFromFollowedUserQuery);
        QueryList.add(photosFromCurrentUserQuery);
        ParseQuery<ParseObject> megaQuery = ParseQuery.or(QueryList);
        megaQuery.include("user");
        megaQuery.orderByDescending("createdAt");

        return megaQuery ;

    }

    public static void likeThatpic (ParseObject photoObject,Context context){

        ParseObject likeActivity = new ParseObject("Activity");
        likeActivity.put("type", "like");
        likeActivity.put("fromUser",ParseUser.getCurrentUser());
        likeActivity.put("toUser", photoObject.getParseUser("user"));
        likeActivity.put("photo", photoObject);

        //set up the ACL
        ParseACL likeAcl = new ParseACL(ParseUser.getCurrentUser());
        likeAcl.setPublicReadAccess(true);
        likeActivity.setACL(likeAcl);
        likeActivity.saveInBackground();

        Toast.makeText(context,"liked",Toast.LENGTH_SHORT).show();

    }
    public static void unlikeThatpic (ParseObject photoObject,final Context context){
        ParseQuery<ParseObject> queryExistingLikes = ParseQuery.getQuery("Activity");
        queryExistingLikes.whereEqualTo("photo", photoObject);
        queryExistingLikes.whereEqualTo("type", "like");
        queryExistingLikes.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        queryExistingLikes.whereEqualTo("toUser", photoObject.getParseUser("user"));
        Toast.makeText(context, "Ungagaliked", Toast.LENGTH_SHORT).show();

        queryExistingLikes.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    try {

                        list.get(0).delete();
                        list.get(0).saveInBackground();
                        Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {

                    Log.e("ERROR", e.getMessage());
                }
            }
        });



    }
    public static boolean picLikedorNotCheck (ParseObject photoObject,Context context,final WeakReference<ImageView> imageButtonReff ){
        final boolean likedorNot[] = new boolean[1] ;
        likedorNot[0] = false;

        ParseQuery<ParseObject> queryExistingLikes = ParseQuery.getQuery("Activity");
        queryExistingLikes.whereEqualTo("photo",photoObject);
        queryExistingLikes.whereEqualTo("type","like");
        queryExistingLikes.whereEqualTo("fromUser",ParseUser.getCurrentUser());
        queryExistingLikes.whereEqualTo("toUser", photoObject.getParseUser("user"));
        Toast.makeText(context, "Ungagaliked", Toast.LENGTH_SHORT).show();

        queryExistingLikes.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    if (!list.isEmpty()) {

                        ImageView butt = imageButtonReff.get() ;

                        butt.setImageResource(R.drawable.heart13);


                        likedorNot[0] = true ;





                    } else {
                        ImageView butt = imageButtonReff.get() ;

                        butt.setImageResource(R.drawable.loving34);
                        likedorNot[0] = false;
                    }

                }
            }
        });



 return likedorNot[0];

    }



}