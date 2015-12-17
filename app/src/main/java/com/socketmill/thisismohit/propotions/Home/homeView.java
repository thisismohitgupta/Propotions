package com.socketmill.thisismohit.propotions.Home;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


import java.lang.ref.WeakReference;

import com.socketmill.thisismohit.propotions.Login;
import com.socketmill.thisismohit.propotions.MainActivity;
import com.socketmill.thisismohit.propotions.R;


/**
 * Created by thisismohit on 03/12/15.
 */

public class homeView extends AsyncTask<String, Void, String> {

    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<TextView> nameViewReference;
    private final WeakReference<ImageView> profileViewReference;
    private final WeakReference<ImageView>LikeButtonReff ;
    private final WeakReference<ImageView>CommentButtonReff;
    private final WeakReference<ImageView>ShareButtonReff;
    private Context context ;

    private ParseObject photoObject ;


    public Bitmap bmp ;
    public Bitmap pro ;
    public String name ;
    public Bitmap[] bitmapArray;
    byte[] dataMain ;
    byte[] datapro ;
    String Username ;
    
    public homeView(ParseObject _photoObjec,WeakReference<ImageView> imageView,WeakReference<ImageView> profileImageView,WeakReference<TextView> nameView,Context _context,WeakReference<ImageView> _LikeButtonReff,WeakReference<ImageView> _CommentButtReff,WeakReference<ImageView>_ShareButtonReff){

        LikeButtonReff = _LikeButtonReff ;
        CommentButtonReff = _CommentButtReff ;
        ShareButtonReff = _ShareButtonReff ;
        photoObject =_photoObjec ;
        imageViewReference = (imageView);
        nameViewReference = nameView;
        profileViewReference = (profileImageView);
        context = _context ;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.e("ERROR", getStatus().toString());
        bitmapArray = new Bitmap[2];


    }


    @Override
    protected String doInBackground(String[] Params) {
        Log.e("test", "Retrieved the object." + photoObject.getObjectId());
        ParseFile fileObject = (ParseFile)photoObject.get("image");
        try {
            dataMain =  fileObject.getData() ;
            if(dataMain!= null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bmp = BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options);
                bmp = Bitmap.createScaledBitmap(bmp, context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().widthPixels * bmp.getHeight() / bmp.getWidth(), false);
                if (bmp != null) {
                    bitmapArray[0] = bmp;

                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ParseUser user =  photoObject.getParseUser("user");


        pro = Login.getBitmapFromMemoryCache(photoObject.getParseUser("user").getUsername() + "thumb");
        if (pro == null) {

            ParseFile profilePicThumb = user.getParseFile("profilePictureSmall");

            try {
                datapro = profilePicThumb.getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (datapro != null) {
                BitmapFactory.Options optionsPro = new BitmapFactory.Options();
                optionsPro.inSampleSize = 2;
                pro = BitmapFactory.decodeByteArray(datapro, 0, datapro.length, optionsPro);

                Login.setBitmapMemoryCache(photoObject.getParseUser("user").getUsername() + "thumb",pro);

            }

        }

        if (pro != null) {
            bitmapArray[1] = pro;


        }


        String ProfileName = Login.getStringFromMemoryCache(photoObject.getParseUser("user").getUsername()+"profileName");

        if(ProfileName == null) {
            Username = user.getString("displayName");
            Login.setStringMemoryCache(photoObject.getParseUser("user").getUsername() + "profileName", Username );
        }else {

          Username =  Login.getStringFromMemoryCache(photoObject.getParseUser("user").getUsername() + "profileName" );


        }
            return "done";


    }



        @Override
        protected void onPostExecute(String bitmap) {
            if(bitmapArray[0] == null){

                Log.e("ERROR","image not loaded  ");
            }else{
                ImageView imageView = imageViewReference.get();
                Login.setBitmapMemoryCache(photoObject.getObjectId(),bitmapArray[0]);
                Drawable drawable = new BitmapDrawable(context.getResources(),bitmapArray[0]) ;
                //imageView.scale
                imageView.setBackground(drawable);
                Log.e("ERROR", "IMage is loaded");



                //Perfect space for some optimization
               final ImageView LikeButton = LikeButtonReff.get();
                final ImageView CommentButton = CommentButtonReff.get();
                final ImageView ShareButton = ShareButtonReff.get();
                 LikeButton.setImageResource(R.drawable.loving34);
                CommentButton.setImageResource(R.drawable.chat20);
                ShareButton.setImageResource(R.drawable.share11);
                //////

                LikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(context, String.valueOf(LikeButton.getId()), Toast.LENGTH_SHORT).show();

                    }
                });

                ShareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, String.valueOf(CommentButton.getId()), Toast.LENGTH_SHORT).show();

                    }
                });

                CommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, PhotoDetail.class);
                        i.putExtra("PhotoId", photoObject.getObjectId());
                        i.putExtra("UserThumb", photoObject.getParseUser("user").getUsername());
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                        //Toast.makeText(context, String.valueOf(ShareButton.getId()), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            if(bitmapArray[1]==null){
            }else {
                ImageView proimageView = profileViewReference.get();
                proimageView.setImageBitmap(bitmapArray[1]);
                Login.setBitmapMemoryCache(photoObject.getObjectId() + "thumb", bitmapArray[1]);

                TextView textName = nameViewReference.get() ;
                if(Username != null) {
                    textName.setText(Username);
                }
            }


            //bitmap.recycle();
            //bmp.recycle();
            //pro.recycle();
        }
}