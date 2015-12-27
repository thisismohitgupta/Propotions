package com.socketmill.thisismohit.propotions.Home;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.socketmill.thisismohit.propotions.R;


import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by thisismohit on 03/12/15.
 */
public class Reactions {

    ParseObject photoObject ;
    boolean runFromlocaldatabase ;





    public Reactions(ParseObject _photoPbject,boolean RunFromLocalDatabase){
        photoObject = _photoPbject ;

        runFromlocaldatabase = RunFromLocalDatabase ;




    }
    public void countTheNumberOfLikesOnParsePhoto(final WeakReference<TextView> _countDisplayReff,String _commentOrLike){

    final String commentorLike = _commentOrLike ;
        ParseQuery<ParseObject> queryExistingLikes = queryMaker(photoObject,runFromlocaldatabase,commentorLike) ;
        queryExistingLikes.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
Log.e("Error",String.valueOf(list.size()) + " thissis the thing");
                    if (!list.isEmpty()) {

                        if(list.size()> 0) {
                            TextView display = _countDisplayReff.get() ;

                            if(display != null) {
                                display.setText(String.valueOf(list.size()));
                            }
                        }else {

                            TextView display = _countDisplayReff.get();
                            Log.e("Error",String.valueOf(list.size()) + " here the thing");

                            if (display != null) {
                                display.setText("");
                            }
                        }

//TODO: get the names of the users and display them if the count if below 10


                    }else {
                        TextView display = _countDisplayReff.get();

                        if (display != null) {
                            Log.e("Error",String.valueOf(list.size()) + " there the thing");

                            display.setText(null);
                        }



                    }
//                    ParseObject.unpinAllInBackground("like", list, new DeleteCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                ParseObject.pinAllInBackground("likeCount", list);
//                            }
//                        }
//                    });

                }else {

                    TextView display = _countDisplayReff.get();

                    if (display != null) {
                        Log.e("Error",String.valueOf(list.size()) + " where the thing");

                        display.setText("");
                    }
                }
            }
        });
    }



    private ParseQuery<ParseObject> queryMaker(ParseObject photoObject,boolean Runfromlocaldatabase,String _commentOrLike) {
        ParseQuery<ParseObject> queryExistingLikes = ParseQuery.getQuery("Activity");
        queryExistingLikes.whereEqualTo("photo", photoObject);
        queryExistingLikes.whereEqualTo("type", _commentOrLike);
        if (Runfromlocaldatabase) {
            queryExistingLikes.fromLocalDatastore();

        }

        return queryExistingLikes;
    }


    public void likeThatpic(ParseObject photoObject,final WeakReference<TextView> LikwCountREFF) {

        ParseObject likeActivity = new ParseObject("Activity");
        likeActivity.put("type", "like");
        likeActivity.put("fromUser", ParseUser.getCurrentUser());
        likeActivity.put("toUser", photoObject.getParseUser("user"));
        likeActivity.put("photo", photoObject);


        //set up the ACL
        ParseACL likeAcl = new ParseACL(ParseUser.getCurrentUser());
        likeAcl.setPublicReadAccess(true);
        likeActivity.setACL(likeAcl);
        likeActivity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                countTheNumberOfLikesOnParsePhoto(LikwCountREFF, "like");
            }
        });


    }

    public void unlikeThatpic(ParseObject photoObject,final WeakReference<TextView> LikwCountREFF) {
        ParseQuery<ParseObject> queryExistingLikes = ParseQuery.getQuery("Activity");
        queryExistingLikes.whereEqualTo("photo", photoObject);
        queryExistingLikes.whereEqualTo("type", "like");
        queryExistingLikes.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        queryExistingLikes.whereEqualTo("toUser", photoObject.getParseUser("user"));

        queryExistingLikes.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    try {

                        if(list.size() > 0) {
                            list.get(0).delete();
                            list.get(0).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    countTheNumberOfLikesOnParsePhoto(LikwCountREFF,"like");
                                }
                            });
                        }
                        } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {

                    Log.e("ERROR", e.getMessage());
                }
            }
        });


    }

    public boolean picLikedorNotCheck(final ParseObject photoObject, final WeakReference<ImageView> imageButtonReff,boolean runFromlocaldatabase,final boolean changeTheLikeButtonInterface,final WeakReference<TextView> LikwCountREFF) {
        final boolean likedorNot[] = new boolean[1];
        likedorNot[0] = false;

        ParseQuery<ParseObject> queryExistingLikes = ParseQuery.getQuery("Activity");
        queryExistingLikes.whereEqualTo("photo", photoObject);
        queryExistingLikes.whereEqualTo("type", "like");
        queryExistingLikes.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        queryExistingLikes.whereEqualTo("toUser", photoObject.getParseUser("user"));
        if (runFromlocaldatabase) {
            queryExistingLikes.fromLocalDatastore();

        }

        queryExistingLikes.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {



                    if(changeTheLikeButtonInterface) {

                        Log.e("Error",String.valueOf(list.size()));
                        if (list.size()>0) {

                            ImageView butt = imageButtonReff.get();
                            if (butt != null) {
                                butt.setImageResource(R.drawable.heart13);
                            }

                            likedorNot[0] = true;


                        } else {
                            ImageView butt = imageButtonReff.get();

                            if (butt != null) {
                                butt.setImageResource(R.drawable.loving34);
                            }
                            likedorNot[0] = false;
                        }


                    } else {
                        Log.e("Error",String.valueOf(list.size()));

                        if (list.isEmpty()) {

                            likedorNot[0] = true;
                            ImageView butt = imageButtonReff.get();
                            if (butt != null) {
                                butt.setImageResource(R.drawable.heart13);



                            }
                            Log.e("Error", "liked that pic");
                            likeThatpic(photoObject, LikwCountREFF);

                        }
                        else {
                            likedorNot[0] = false;
                            ImageView butt = imageButtonReff.get();

                            if (butt != null) {
                                butt.setImageResource(R.drawable.loving34);
                            }
                            Log.e("Error", "unlike that pic");
                            unlikeThatpic(photoObject,LikwCountREFF);




                        }
                    }


//                    ParseObject.unpinAllInBackground("like", list, new DeleteCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                ParseObject.pinAllInBackground("like", list);
//                            }
//                        }
//                    });

                }
            }
        });


        return likedorNot[0];

    }

}
