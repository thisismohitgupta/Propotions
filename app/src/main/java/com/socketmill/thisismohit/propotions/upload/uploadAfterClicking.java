package com.socketmill.thisismohit.propotions.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.facebook.Profile;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.socketmill.thisismohit.propotions.Login;
import com.socketmill.thisismohit.propotions.MainActivity;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by thisismohit on 03/12/15.
 */

public class uploadAfterClicking extends AsyncTask<String, Void, String> {




    public Profile profile ;
    public Context context ;
    public ProgressDialog progess ;
    public RelativeLayout view ;

    private String ThumbnailfileLocation ;
    private String PicturefileLocation ;
    ParseFile ProfileImageFile ;
    final WeakReference<ImageButton> DoneUploadingREFF ;
    final WeakReference<ProgressDialog> ProgressDialogBarREFF ;

   ParseFile ProfileImageThumbFile ;
    public uploadAfterClicking(String _PicturefileLocation,String _ThumbnailfileLocation,WeakReference<ImageButton> _DoneUploadingREFF,WeakReference<ProgressDialog> _ProgressDialogBarREFF){

        PicturefileLocation = _PicturefileLocation ;
        ThumbnailfileLocation = _ThumbnailfileLocation ;
        DoneUploadingREFF = _DoneUploadingREFF ;
        ProgressDialogBarREFF = _ProgressDialogBarREFF ;


    }

    ParseObject Photo ;
    @Override
    protected String doInBackground(String[] Params) {

        Bitmap imageSet =  ThumbnailCache.cache.get("upload" + PicturefileLocation);
        Bitmap thumbSet =  ThumbnailCache.cache.get("thumb"+ThumbnailfileLocation);

        ByteArrayOutputStream ImageStream = new ByteArrayOutputStream();




        ByteArrayOutputStream ThumbStream = new ByteArrayOutputStream();

        byte[] ImageBytearray ;
        byte[] ThumbBytearray ;

        try {
            imageSet.compress(Bitmap.CompressFormat.PNG, 100, ImageStream);
            ImageBytearray = ImageStream.toByteArray();
            thumbSet.compress(Bitmap.CompressFormat.PNG, 100, ThumbStream);
            ThumbBytearray = ThumbStream.toByteArray();
            ImageStream.reset();
            ThumbStream.reset();
            Photo = new ParseObject("Photo") ;




            ProfileImageFile = new ParseFile("Profile_" + profile.getId() + ".jpg", ImageBytearray);
            ProfileImageThumbFile = new ParseFile("thumb_" + profile.getId() + ".jpg", ThumbBytearray);
        }catch (Exception e){

        }


        try {
            ProfileImageFile.save() ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            ProfileImageThumbFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "done" ;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        ImageButton doneAddingComment = DoneUploadingREFF.get();

        Log.e("Error","im here");
        doneAddingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialogBarREFF.get();

                dialog.show();
                Log.e("Error", "im here on click");



            }
        });


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        Photo.put("image", ProfileImageFile);
        Photo.put("thumbnail", ProfileImageThumbFile);


        Photo.put("user", ParseObject.createWithoutData(ParseUser.class, ParseUser.getCurrentUser().getObjectId()));
        ParseACL photoAcl = new ParseACL(ParseUser.getCurrentUser());

        photoAcl.setPublicReadAccess(true);
        photoAcl.setPublicWriteAccess(false);


        Photo.setACL(photoAcl);


        //
        ProgressDialog dialog = ProgressDialogBarREFF.get();

      if (dialog.isShowing()){
          dialog.setMessage("Image Uploaded");
          Photo.saveInBackground();
          Timer timer = new Timer();

          timer.schedule(new TimerTask() {
              public void run() {
                  ProgressDialog dialog = ProgressDialogBarREFF.get();
                  dialog.dismiss();

                  Intent i = new Intent(context, MainActivity.class);
                  i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                  //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(i);
              }
          }, 1000);



      }else {
          ImageButton doneAddingComment = DoneUploadingREFF.get();

          doneAddingComment.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  final ProgressDialog dialog = ProgressDialogBarREFF.get();

                  dialog.show();
                  Photo.saveInBackground(new SaveCallback() {
                      @Override
                      public void done(ParseException e) {
                          if (dialog.isShowing()){
                              dialog.setMessage("Image Uploaded");
                              Photo.saveInBackground();
                              Timer timer = new Timer();

                              timer.schedule(new TimerTask() {
                                  public void run() {
                                      ProgressDialog dialog = ProgressDialogBarREFF.get();
                                      dialog.dismiss();



                                      Intent i = new Intent(context, MainActivity.class);
                                      i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                      //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      context.startActivity(i);
                                  }
                              }, 1000);



                          }                      }
                  });
                  Log.e("Error", "im here on click");


              }
          });

      }




    }











}