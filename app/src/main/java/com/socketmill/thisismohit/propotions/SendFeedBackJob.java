package com.socketmill.thisismohit.propotions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by root on 30/11/15.
 */
public class SendFeedBackJob extends AsyncTask<String, Void, String> {

    ParseUser User = new ParseUser();
    public Bitmap myBitmap ;
    public Activity activity;
    ProgressBar progressBar;
    public int invisible ;
    public int show ;
    String AccessToken ;
    String EMAIL ;

    boolean UserFoundOrNot ;

    //Context context ;

    com.facebook.Profile profile ;
    ParseFile ProfileImageFile ;
    ParseFile ProfileImageThumbFile ;

    @Override
    protected String doInBackground(String[] Params) {

        String accessToken = AccessToken ;
        String email = EMAIL;
        profile =  com.facebook.Profile.getCurrentProfile();
        User.put("displayName", profile.getName());
        User.put("facebookId", profile.getId());
        User.setUsername(profile.getId());
        try
        {
                User.put("FBaccessToken", accessToken);
                User.setEmail(email);
        }
        catch (Exception e){

            Log.e("ERROR","Failed");
        }
        User.setPassword(profile.getId() + profile.getId());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");// put name of your Parse class here
        query.whereEqualTo("username", profile.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e)
            {
                if (e == null)
                {
                    //brace the lord no error
                    if (list == null)
                    {
                      //  parseSignUp() ;
                        UserFoundOrNot = false ;

                    }
                    else
                    {
                        if  (list.isEmpty())
                        {


                            UserFoundOrNot = false ;
                        //    parseSignUp();
                        }
                        else
                        {
                            UserFoundOrNot = true ;
                            login(profile.getId(), (profile.getId() + profile.getId()));
                        }
                        for (ParseObject str : list)
                        {
                                Log.e("ERROR", "what ?");
                        }
                    }
                }
                else
                {
                    Log.e("ERROR", "not");

                }
            }
        });



        if (UserFoundOrNot){

        }else {
            parseSignUp() ;

        }


        return "done";
    }






 public byte[] geByteArrayFromURL(String src) {
        Bitmap myBitmap;
        InputStream in =null;
        byte[] ImageBytearray;
        ByteBuffer Imagebuffer = null;
        try
        {
            Log.e("ERROR",src);
            URL aURL = new URL(src);
            Log.e("ERROR","1");

            URLConnection conn = aURL.openConnection();
            conn.setUseCaches(true);
            Log.e("ERROR", "2");
            conn.connect();
            Log.e("ERROR", "3");
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            myBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
            is.close();







            ByteArrayOutputStream ImageStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ImageStream);
             ImageBytearray = ImageStream.toByteArray();

            myBitmap.recycle();


            return ImageBytearray;


        }
        catch(Exception ex)
        {
            Log.e("ERROR", "omg omg "+ex.toString());

        }

        return null ;
 }

    private void login(String lowerCase, String password)
    {
        // TODO Auto-generated method stub
        ParseUser.logInInBackground(lowerCase, password, new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                // TODO Auto-generated method stub
                if(e == null)
                {
                    Log.e("ERROR","Login successful in async");
                    loginSuccessful();
                }
                else
                {
                    Log.e("ERROR","Login Unsuccessful in async");
                    loginUnSuccessful();
                }
            }
        });
    }

    protected void loginSuccessful() {
        // TODO Auto-generated method stub
    }
    protected void loginUnSuccessful() {
        // TODO Auto-generated method stub
    }
    public void parseSignUp()
        {
            Uri ProfilePicBigUri = profile.getProfilePictureUri(500, 500);
            Uri ProfilePicSmallUri = profile.getProfilePictureUri(200, 200);
            byte[] Byte = geByteArrayFromURL(ProfilePicBigUri.toString());
            byte[] Byte2 = geByteArrayFromURL(ProfilePicSmallUri.toString());
            Log.e("ERROR", "got images 2 omg ");
            ProfileImageFile = new ParseFile("Profile_" + profile.getId() + ".jpg", Byte);
            ProfileImageThumbFile = new ParseFile("thumb_" + profile.getId() + ".jpg", Byte2);
            ProfileImageFile.saveInBackground(new SaveCallback() {


                @Override
                public void done(ParseException e) {
                    Log.e("ERROR", "donesavin  omg ");

                    User.put("profilePictureMedium", ProfileImageFile);

                    ProfileImageThumbFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.e("ERROR", "donesavin 2 omg ");

                            User.put("profilePictureSmall", ProfileImageThumbFile);
                            User.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {

                                    Log.e("ERROR", "done signup omg ");



                                    login(profile.getId(), (profile.getId() + profile.getId()));



                                }

                            });

                        }

                    });

                }

            });

        }

     @Override
    protected void onPreExecute() {
        super.onPreExecute();
         if (progressBar.getVisibility() != View.VISIBLE ) {
             progressBar.setVisibility(View.VISIBLE);
        }
     }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressBar.getVisibility() == View.VISIBLE ) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}