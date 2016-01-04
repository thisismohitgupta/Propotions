package com.socketmill.thisismohit.propotions.Home;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import com.socketmill.thisismohit.propotions.JniBitmapHolder;
import com.socketmill.thisismohit.propotions.Login;





/**
 * Created by thisismohit on 03/12/15.
 */

public class homeView extends AsyncTask<String, Void, String> {

    private WeakReference<ImageView> imageViewReference;
    //private final WeakReference<TextView> nameViewReference;
    private final WeakReference<ImageView> profileViewReference;
   // private final WeakReference<ImageView>LikeButtonReff ;
   // private final WeakReference<ImageView>CommentButtonReff;
   // private final WeakReference<ImageView>ShareButtonReff;
   // private final WeakReference<TextView> LikeCountREFF;
   // private final WeakReference<TextView> CommentCountREFF;
    ImageView imageView ;

    private Context context ;

    private ParseObject photoObject ;


    Bitmap bitmaps ;

    public Bitmap pro ;
    public String name ;
    ImageView imageview ;
    byte[] dataMain ;
    byte[] datapro ;
    String Username ;
    boolean likeFlag;

    JniBitmapHolder bitmapHolder ;
    public homeView(ParseObject _photoObjec,
                    WeakReference<ImageView> imageView,
                    WeakReference<ImageView> profileImageView,
                    ImageView imageviews
                    //WeakReference<TextView> nameView,
                    //Context _context
                 //   WeakReference<ImageView> _LikeButtonReff,
                  //  WeakReference<ImageView> _CommentButtReff,
                 //   WeakReference<ImageView> _ShareButtonReff,
                 //   WeakReference<TextView> LikeCountReff,
                 //   WeakReference<TextView> CommentCountReff

    )
    {

       // LikeButtonReff = _LikeButtonReff ;
      //  CommentButtonReff = _CommentButtReff ;
      //  ShareButtonReff = _ShareButtonReff ;
        photoObject =_photoObjec ;
        imageViewReference = (imageView);
       // nameViewReference = nameView;
        profileViewReference = (profileImageView);
       // context = _context ;
      //  LikeCountREFF = LikeCountReff ;
      //  CommentCountREFF = CommentCountReff;


        this.imageview = imageviews ;



    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.e("ERROR", getStatus().toString());



    }


    @Override
    protected String doInBackground(String[] Params) {


        ParseFile fileObject = (ParseFile)photoObject.get("image");
        try {
            dataMain =  fileObject.getData() ;
            Log.e("ERROR","Images Were Downloaded :(");
            if(dataMain!= null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
               // options.inJustDecodeBounds = true;






                options.inSampleSize = 1;
             //  BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options);
              //  options.inSampleSize = calculateInSampleSize(options, context.getResources().getDisplayMetrics().widthPixels,
              //          context.getResources().getDisplayMetrics().widthPixels );
//
                options.inJustDecodeBounds = false;
                //Login.setBitmapMemoryCacheLRU(photoObject.getObjectId(),BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options));

                bitmaps = BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options) ;

               //
              // bitmapHolder.storeBitmap(bitmap);

//


//
//                int widthPixels = context.getResources().getDisplayMetrics().widthPixels ;

               // bitmapHolder.scaleBitmap(480 ,480* bitmapHolder.getBitmap().getHeight() / bitmapHolder.getBitmap().getWidth(), JniBitmapHolder.ScaleMethod.NearestNeighbour);

                //bmp = null ;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }







            return "done";


    }



        @Override
        protected void onPostExecute(String bitmap) {


               // Login.setBitmapMemoryCache(photoObject.getObjectId(),Login.getBitmapFromMemoryCacheLRU(photoObject.getObjectId()));


                if(imageview!= null ) {
                   // Bitmap map = bitmapHolder.getBitmapAndFree() ;

                        Log.e("TAG","bit map set");
                        Toast.makeText(imageview.getContext(),"hello niga",Toast.LENGTH_SHORT).show();
                        imageview.setImageBitmap(bitmaps);
                        // bitmapHolder.freeBitmap();
                        //  bitmapHolder = null;
                        //imageViewReference = null;














                //Glide.with(imageView.getContext()).
                //Drawable drawable = new BitmapDrawable(context.getResources(),Login.getBitmapFromMemoryCacheLRU(photoObject.getObjectId())) ;
                //imageView.scale
                //imageView.setBackground(drawable);


                //Perfect space for some optimization
//               final ImageView LikeButton = LikeButtonReff.get();
//                final ImageView CommentButton = CommentButtonReff.get();
//                final ImageView ShareButton = ShareButtonReff.get();
//
//                 Reactions Reaction = new Reactions(photoObject,false);
//                likeFlag = Reaction.picLikedorNotCheck(photoObject, LikeButtonReff,false,true,LikeCountREFF);
//
//
////                CommentButton.setImageResource(R.drawable.chat20);
////                ShareButton.setImageResource(R.drawable.share11);
//                //////
//
//                LikeButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Reactions Reaction = new Reactions(photoObject,false);
//                        Reaction.picLikedorNotCheck(photoObject, LikeButtonReff,false,false,LikeCountREFF) ;
//                            //if the user has not already liked the pic we need to like it for user
//
//                    }
//                });
//
//                ShareButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, String.valueOf(CommentButton.getId()), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//                CommentButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent i = new Intent(context, commentDetail.class);
//                        i.putExtra("PhotoId", photoObject.getObjectId());
//                        i.putExtra("UserThumb", photoObject.getParseUser("user").getUsername());
//                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);
//
//                        //Toast.makeText(context, String.valueOf(ShareButton.getId()), Toast.LENGTH_SHORT).show();
//
//                    }
//                });

            }

//            if(Login.getBitmapFromMemoryCache(Username + "thumb")==null){
//            }else {
//                ImageView proimageView = profileViewReference.get();
//                proimageView.setImageBitmap(Login.getBitmapFromMemoryCache(Username + "thumb"));
//                //Login.setBitmapMemoryCache(photoObject.getParseUser("user").getUsername(), bitmapArray[1]);
//
//                TextView textName = nameViewReference.get() ;
//                if(Username != null) {
//                    textName.setText(Username);
//                }
//            }


            //bitmap.recycle();
            //bmp.recycle();
            //pro.recycle();
        }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.e("ERROR","in sample size is "+String.valueOf(inSampleSize) + " , more required width is "+ String.valueOf(reqWidth) + " bitmap width is "+ String.valueOf(width));




        return inSampleSize;
    }
}