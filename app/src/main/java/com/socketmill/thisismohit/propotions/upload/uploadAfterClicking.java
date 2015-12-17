package com.socketmill.thisismohit.propotions.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.Profile;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.junit.runner.Result;

import com.socketmill.thisismohit.propotions.AfterPicTakenImageCommentAddingClass;
import com.socketmill.thisismohit.propotions.Explore;
import com.socketmill.thisismohit.propotions.MainActivity;

/**
 * Created by thisismohit on 03/12/15.
 */

public class uploadAfterClicking extends AsyncTask<String, Void, String> {



    public byte[] bytePicBig ;
    public byte[] bytePicSmall ;
    public Profile profile ;
    public Context context ;
    public ProgressDialog progess ;
    public RelativeLayout view ;
    public boolean flag = false ;


    ParseObject Photo ;
    @Override
    protected String doInBackground(String[] Params) {


        Photo = new ParseObject("Photo") ;




        final ParseFile ProfileImageFile = new ParseFile("Profile_" + profile.getId() + ".jpg", bytePicBig);
        final ParseFile ProfileImageThumbFile = new ParseFile("thumb_" + profile.getId() + ".jpg", bytePicSmall);
        ProfileImageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Photo.put("image", ProfileImageFile);
                Log.e("ERROR", "hello");

                ProfileImageThumbFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Photo.put("thumbnail", ProfileImageThumbFile);
                        //Photo.("user").add(ParseUser.getCurrentUser());

                        Photo.put("user",ParseObject.createWithoutData(ParseUser.class,ParseUser.getCurrentUser().getObjectId()));
                        ParseACL photoAcl = new ParseACL(ParseUser.getCurrentUser());

                        photoAcl.setPublicReadAccess(true);
                        photoAcl.setPublicWriteAccess(false);


                        Photo.setACL(photoAcl);


                        Photo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                Log.e("ERROR","done uploading both files");
                            }
                        });
                    }
                });
            }
        });

        flag = true ;



        return "done";

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.e("ERROR", getStatus().toString());



    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

Log.e("ERROR", getStatus().toString());



        //




    }











}