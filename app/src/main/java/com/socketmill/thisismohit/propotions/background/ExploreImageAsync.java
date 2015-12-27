package com.socketmill.thisismohit.propotions.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.parse.GetFileCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by root on 27/12/15.
 */
public class ExploreImageAsync extends AsyncTask<String, Void, String> {

    WeakReference<ImageView> imageViewWeakReference ;

    ParseObject photoObject ;
    Bitmap bmp ;
    Context context ;

    public ExploreImageAsync(WeakReference<ImageView> _imageREFF,ParseObject _photoObject,Context _context){

        photoObject = _photoObject ;
        imageViewWeakReference = _imageREFF ;
        context = _context ;

    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(bmp!=null){
            ImageView imageView = imageViewWeakReference.get();

            Drawable drawable = new BitmapDrawable(context.getResources(),bmp);

            imageView.setBackground(drawable);


        }

    }

    @Override
    protected String doInBackground(String... params) {

        ParseFile bits = photoObject.getParseFile("thumbnail");
        try {
            byte[] bytes = bits.getData();
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
