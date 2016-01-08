package com.socketmill.thisismohit.propotions.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.parse.ParseFile;
import com.parse.ParseObject;

import com.socketmill.thisismohit.propotions.JniBitmapHolder;

import com.socketmill.thisismohit.propotions.R;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;


import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;



/**
 * Created by thisismohit on 3/1/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {


    private List<ParseObject> listItemsList ;
    private Context context ;
    JniBitmapHolder bitmapHolder ;


    static ThumbnailCache.cache cache;

    public RecyclerAdapter(Context context, List<ParseObject> listItemsList, File cacheFile) {
        this.context = context;
        this.listItemsList = listItemsList ;

        cache =  new ThumbnailCache.cache();

        ThumbnailCache.DiskCache.file = cacheFile ;
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsfeed_item, viewGroup, false);

        HomeRecyclerViewHolder holder = new HomeRecyclerViewHolder(v);


        return holder ;




    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewHolder holder, int position) {
       final ParseObject listItem = listItemsList.get(position);

        holder.getLayoutPosition();


        holder.username.setText( listItem.getParseUser("user").get("displayName").toString() ) ;
       final WeakReference<ImageView> mainImage = new WeakReference<ImageView>(holder.displayImage);

        final WeakReference<ImageView> profileImage = new WeakReference<ImageView>(holder.ProfileImage);


        final AsyncTask oldTask = (AsyncTask) holder.displayImage.getTag();
        if (oldTask != null) {
            oldTask.cancel(false);
        }

        Bitmap images ;
        final DisplayMetrics matrix = mainImage.get().getContext().getResources().getDisplayMetrics();

            if (ThumbnailCache.cache.get(listItem.getObjectId()) == null) {






                AsyncTask taskAnother = new AsyncTask() {
                    Bitmap images;
                    @Override
                    protected Object doInBackground(Object[] params) {
                        images = ThumbnailCache.DiskCache.get(listItem.getObjectId());
                        if(images == null ) {

                            Bitmap pro;
                            ParseFile fileObject = (ParseFile) listItem.get("image");
                            ParseFile thumObject = (ParseFile) listItem.getParseUser("user").get("profilePictureSmall");
                            try {
                                byte[] dataMain = fileObject.getData();
                                // byte[]  datapro = thumObject.getData();
                                if (dataMain != null) {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inJustDecodeBounds = false ;
                                    images = BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options);
                                    bitmapHolder = new JniBitmapHolder();
                                    bitmapHolder.storeBitmap(images);
                                    //bitmapHolder.convertGreyScale();
                                    bitmapHolder.scaleBitmap(matrix.widthPixels, matrix.widthPixels * images.getWidth() / images.getHeight(), JniBitmapHolder.ScaleMethod.BilinearInterpolation);
                                    //   pro = BitmapFactory.decodeByteArray(datapro, 0, datapro.length, options);



                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("ERROR", "Disk cache used");
                            //ThumbnailCache.cache.put(listItem.getObjectId(), images);
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        ImageView imageview = mainImage.get();
                        ImageView imageview2 = profileImage.get();
                        images = bitmapHolder.getBitmapAndFree();

                        if (images != null) {
                            if (imageview != null) {
                                imageview.setImageBitmap(images);
                                //imageview2.setImageBitmap(pro);
                                ThumbnailCache.cache.put(listItem.getObjectId(), images);
                                AsyncTask  taskmee = new AsyncTask() {
                                    @Override
                                    protected Object doInBackground(Object[] params) {
                                        ThumbnailCache.DiskCache.put(listItem.getObjectId(), ThumbnailCache.cache.get(listItem.getObjectId()));

                                        return null;
                                    }
                                };

                                taskmee.execute();
                            }
                        } else {
                            imageview.setImageBitmap(null);
                        }



//                        ImageView proimageView = profileImage.get();
//                        if (proimageView != null) {
//                            // proimageView.setImageBitmap(pro);
//                        }
                            // RecyclerAdapter.this.notifyDataSetChanged();

                    }




                } ;








                    mainImage.get().setTag(taskAnother);

                taskAnother.execute();



            } else {

                //bitmap is not null
                Log.e("ERROR","Cache used");


                holder.displayImage.setImageBitmap(cache.get(listItem.getObjectId()));

            }

    }

    public void clearAdapter( ){
        listItemsList.clear();
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return (listItemsList.size() );
    }
}
