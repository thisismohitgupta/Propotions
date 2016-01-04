package com.socketmill.thisismohit.propotions;




import android.app.ActivityManager;

import android.content.Context;



import android.os.Bundle;


import android.os.PersistableBundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import android.view.Menu;


import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;



import com.parse.DeleteCallback;
import com.parse.FindCallback;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;




import java.lang.ref.WeakReference;
import java.util.ArrayList;

import java.util.List;




import com.socketmill.thisismohit.propotions.Home.RecyclerAdapter;

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

                        //displayafterQuery(list, didRunOnce);
                    }
                }
            }
        });






        mGridView = (RecyclerView) findViewById(R.id.lists);
        //mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

    }

    @Override
    protected void onResume() {
        super.onResume();


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
                              //  ll.removeAllViews();
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


        } else {

            Toast.makeText(getApplicationContext(), "already ran once", Toast.LENGTH_SHORT).show();
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

        mAdapter = new RecyclerAdapter(getApplicationContext(),list);
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

                       // mAdapter.notifyDataSetChanged();
                      //  text.setText(null);
                    }

                }
            }


        });

       // getLoaderManager().initLoader(LOADER_CURSOR, null, mCursorCallbacks(list));



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




//    private final LoaderManager.LoaderCallbacks<Cursor> mCursorCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
//
//        @Override
//        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//            final String[] columns = { BaseColumns._ID };
//
//            new CursorLoader(MainActivity.this);
//            return new CursorLoader(MainActivity.this,
//                    , columns, null, null,
//                    MediaStore.Images.Media.DATE_ADDED + " DESC");
//        }
//
//        @Override
//        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//            mAdapter.swapCursor(data);
//        }
//
//        @Override
//        public void onLoaderReset(Loader<Cursor> loader) {
//            mAdapter.swapCursor(null);
//        }
//    };


    /**
     * Adapter showing list of photos from
     * {@link android.provider.MediaStore.Images}.
     */
//    private class PhotoAdapter extends CursorAdapter {
//
//
//        public PhotoAdapter(Context context)
//        {
//            super(context, null, false);
//        }
//
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            return LayoutInflater.from(context).inflate(R.layout.newsfeed_item, parent, false);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            final long photoId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
//
//
//            final ImageView imageView = (ImageView) view.findViewById(R.id.displayImage);
//
//
//            // Cancel any pending thumbnail task, since this view is now bound
//            // to new thumbnail
//            final ThumbnailAsyncTask oldTask = (ThumbnailAsyncTask) imageView.getTag();
//            if (oldTask != null) {
//                oldTask.cancel(false);
//            }
//
//                // Cache enabled, try looking for cache hit
//                final Bitmap cachedResult = mCache.get(photoId);
//                if (cachedResult != null) {
//                    imageView.setImageBitmap(cachedResult);
//                    return;
//                }
//
//
//            // If we arrived here, either cache is disabled or cache miss, so we
//            // need to kick task to load manually
//            final ThumbnailAsyncTask task = new ThumbnailAsyncTask(imageView);
//            imageView.setImageBitmap(null);
//            imageView.setTag(task);
//            task.execute(photoId);
//        }
//    }
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
                mCache.evictAll();

            } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
                // Entering list of cached background apps; evict oldest half of our
                // thumbnail cache
                Log.v("trim", "evicting oldest half of thumbnail cache");
                mCache.trimToSize(mCache.size() / 2);
            }
        }


}
