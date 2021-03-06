package com.socketmill.thisismohit.propotions;


import android.content.Intent;
import android.os.Bundle;


import android.os.PersistableBundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import android.view.Menu;


import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;



import com.parse.DeleteCallback;
import com.parse.FindCallback;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;





import java.util.ArrayList;

import java.util.List;


import com.parse.SaveCallback;
import com.socketmill.thisismohit.propotions.Home.RecyclerAdapter;

import com.socketmill.thisismohit.propotions.Home.RecyclerToListViewScrollListener;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;



public class MainActivity extends AppCompatActivity {

    RecyclerView ll;
    boolean likeFlag;
    boolean didRunOnce;

    boolean ranonce;

    private ThumbnailCache mCache;
    private RecyclerAdapter mAdapter;
    private RecyclerView mGridView;
    private static final int LOADER_CURSOR = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_main);
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());
         getSupportActionBar().hide();


        didRunOnce = true;
        boolean runFromLocalDataBase = true;
        ParseQuery<ParseObject> MainQuery = this.PhotosToShowQueryMake(runFromLocalDataBase);

        MainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {

                    } else {

                        displayafterQuery(list, didRunOnce);

                    }
                }
            }
        });





        mGridView = (RecyclerView) findViewById(R.id.lists);
        //mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        setIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        String reload = getIntent().getStringExtra("PleaseReload");
        if (reload != null) {
            Log.e("ERROR", reload);
            didRunOnce = true;
            ranonce = true;
        }


        if (didRunOnce || ranonce) {
            didRunOnce = false;
            ranonce = false;
            ll = (RecyclerView) findViewById(R.id.lists);


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
                            displayafterQuery(list, didRunOnce);
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
        ParseQuery<ParseObject> FollowingActivityQuery = ParseQuery.getQuery("Activity");
        FollowingActivityQuery.whereEqualTo("type", "follow");
        FollowingActivityQuery.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        //photos from all the Users previously found
        ParseQuery<ParseObject> photosFromFollowedUserQuery = ParseQuery.getQuery("Photo");
        photosFromFollowedUserQuery.whereMatchesKeyInQuery("user", "toUser", FollowingActivityQuery);
        photosFromFollowedUserQuery.whereExists("image");
        //second query for the current users own pictures
        ParseQuery<ParseObject> photosFromCurrentUserQuery = ParseQuery.getQuery("Photo");
        photosFromCurrentUserQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        photosFromCurrentUserQuery.whereExists("image");
        if (runfromlocaldatabase) {
            photosFromCurrentUserQuery.fromLocalDatastore();
            photosFromFollowedUserQuery.fromLocalDatastore();
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

    private void displayafterQuery(final List<ParseObject> list, final boolean didRunonce) {
        mAdapter = new RecyclerAdapter(getApplicationContext(), list, getCacheDir());
        mGridView.setLayoutManager(new LinearLayoutManager(this));
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mGridView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

                final ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.displayImages);

                final TextView text = (TextView) holder.itemView.findViewById(R.id.DisplayName);
                if(imageView != null) {
                    imageView.setImageBitmap(null);
                    if(text != null) {
                        Log.e("ERROR","view deleted");
                    }
                }
            }
        });
        mGridView.setLayoutManager(new RecyclerToListViewScrollListener(this));



//
//                //Perfect space for some optimization
//                final ImageView LikeButtons = LikeButtonReff.get();
//                final ImageView CommentButtons = CommentButtonReff.get();
//                final ImageView ShareButtons = ShareButtonReff.get();
//                final Reactions Reaction = new Reactions(list.get(i), false);
//                Reaction.countTheNumberOfLikesOnParsePhoto(LikeCountREFF, "like");
//                Reaction.countTheNumberOfLikesOnParsePhoto(CommentCountREFF, "comment");
//
//
//                Reactions Reactionn = new Reactions(list.get(i), false);
//                Reactionn.picLikedorNotCheck(list.get(i), LikeButtonReff, false, true, LikeCountREFF);
//
//                CommentButton.setImageResource(R.drawable.chat20);
//                ShareButton.setImageResource(R.drawable.share11);
//                //////
//                final int tempI = i;
//
//                final boolean runfromLocalDatabase = false;
//                LikeButtons.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        Reactions Reactionn = new Reactions(list.get(tempI), false);
//                        Reactionn.picLikedorNotCheck(list.get(tempI), LikeButtonReff, false, false, LikeCountREFF);
//                        Reactionn.countTheNumberOfLikesOnParsePhoto(LikeCountREFF, "like");
//                        //if he has not already liked
//
//
//                    }
//                });
//
//                ShareButtons.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                CommentButtons.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent i = new Intent(getApplicationContext(), commentDetail.class);
//                        i.putExtra("PhotoId", ParseObjectId);
//                        i.putExtra("UserThumb", ParseUsername);
//                        i.putExtra("UserObjectID", ParseUserObjectId);
//
//
////
//                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getApplicationContext().startActivity(i);
//
//
//                    }
//                });
        }
    /**
     * Adapter showing list of photos from
     * {@link android.provider.MediaStore.Images}.
     */

        @Override
        public void onTrimMemory(int level) {
            super.onTrimMemory(level);
            Log.v("trim", "onTrimMemory() with level=" + level);

            // Memory we can release here will help overall system performance, and
            // make us a smaller target as the system looks for memory

            if (level >= TRIM_MEMORY_MODERATE) { // 60
                // Nearing middle of list of cached background apps; evict our
                // entire thumbnail cache
                Log.v("trim", "evicting entire thumbnail cache");
                ThumbnailCache.cache.evitall();

            } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
                // Entering list of cached background apps; evict oldest half of our
                // thumbnail cache
                Log.v("trim", "evicting oldest half of thumbnail cache");
                ThumbnailCache.cache.trimToSize(ThumbnailCache.cache.size() / 2);
            }
        }


}
