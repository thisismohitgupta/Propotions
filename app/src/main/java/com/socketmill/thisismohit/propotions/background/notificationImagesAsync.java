package com.socketmill.thisismohit.propotions.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.socketmill.thisismohit.propotions.Login;

import java.lang.ref.WeakReference;

/**
 * Created by root on 21/12/15.
 */
    public class notificationImagesAsync extends AsyncTask<String, Void, String> {

   private ParseObject notificationObject ;
   private WeakReference<ImageView> profileImageWeakReff;

    private WeakReference<ImageView> photoThumbnailreff;


    Bitmap thumbPro ;
    Bitmap thumbPhoto ;
    String PhotoId ;


   public notificationImagesAsync(ParseObject _notificationObejct,WeakReference<ImageView> _profileImageWeakReddd , WeakReference<ImageView> _photoThumbnailreff){

        notificationObject= _notificationObejct ;
        profileImageWeakReff = _profileImageWeakReddd ;
        photoThumbnailreff = _photoThumbnailreff ;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        if(notificationObject.get("type").toString().equals("follow")) {

            ParseFile UserThub = notificationObject.getParseUser("fromUser").getParseFile("profilePictureSmall") ;

            try {

                byte[] UserThumbByte = UserThub.getData();
                thumbPro = BitmapFactory.decodeByteArray(UserThumbByte,0,UserThumbByte.length);
                thumbPhoto = null ;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            ParseFile PhotoThumbnailFile = notificationObject.getParseObject("photo").getParseFile("thumbnail");
            ParseFile UserThub = notificationObject.getParseUser("fromUser").getParseFile("profilePictureSmall") ;

            try {
                byte[] PhotoThumbnailByte = PhotoThumbnailFile.getData();
                byte[] UserThumbByte = UserThub.getData();
                thumbPro = BitmapFactory.decodeByteArray(UserThumbByte,0,UserThumbByte.length);
                thumbPhoto = BitmapFactory.decodeByteArray(PhotoThumbnailByte,0,PhotoThumbnailByte.length);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

            if (thumbPro != null) {
                ImageView proView = profileImageWeakReff.get();
                proView.setImageBitmap(thumbPro);

                Login.setBitmapMemoryCache(notificationObject.getParseUser("fromUser").getUsername() + "thumb", thumbPro);


            }



            if (thumbPhoto != null) {

                ImageView thumbPhotoView = photoThumbnailreff.get();
                thumbPhotoView.setImageBitmap(thumbPhoto);
                Log.e("Error", "What you know about meeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                Login.setBitmapMemoryCache(PhotoId + "thumb", thumbPhoto);

            }




    }
}
