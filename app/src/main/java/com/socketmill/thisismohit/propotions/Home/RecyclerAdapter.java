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
import java.lang.ref.WeakReference;
import java.util.List;



/**
 * Created by thisismohit on 3/1/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {


    private List<ParseObject> listItemsList ;
    private Context context ;
    JniBitmapHolder bitmapHolder ;


    static ThumbnailCache.DiskCache diskCache;

    static ThumbnailCache.cache cache;

    public RecyclerAdapter(Context context, List<ParseObject> listItemsList, File cacheFile) {
        this.context = context;
        this.listItemsList = listItemsList ;

        cache =  new ThumbnailCache.cache();
        diskCache = new ThumbnailCache.DiskCache(cacheFile);


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

        final DisplayMetrics matrix = mainImage.get().getContext().getResources().getDisplayMetrics();

            if (cache.get(listItem.getObjectId()) == null) {


                if (ThumbnailCache.DiskCache.get(listItem.getObjectId()) == null) {


                    AsyncTask task = new AsyncTask() {

                        byte[] datapro;
                        byte[] dataMain;
                        Bitmap bitmaps;
                        Bitmap pro;

                        @Override
                        protected Object doInBackground(Object[] params) {


                            ParseFile fileObject = (ParseFile) listItem.get("image");

                            ParseFile thumObject = (ParseFile) listItem.getParseUser("user").get("profilePictureSmall");
                            try {
                                dataMain = fileObject.getData();
                                Log.e("ERROR", "Images Were Downloaded :(");
                                datapro = thumObject.getData();
                                if (dataMain != null) {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inJustDecodeBounds = false;
                                    bitmaps = BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options);
                                    bitmapHolder = new JniBitmapHolder();
                                    bitmapHolder.storeBitmap(bitmaps);
                                    bitmapHolder.scaleBitmap(matrix.widthPixels, matrix.widthPixels * bitmaps.getWidth() / bitmaps.getHeight(), JniBitmapHolder.ScaleMethod.NearestNeighbour);
                                    pro = BitmapFactory.decodeByteArray(datapro, 0, datapro.length, options);
                                    bitmaps.recycle();
                                    cache.put(listItem.getObjectId(), bitmapHolder.getBitmapAndFree());
                                    ThumbnailCache.DiskCache.put(listItem.getObjectId(), cache.get(listItem.getObjectId()));

                                }


                            } catch (Exception e) {

                                Log.e("ERROR", e.getMessage());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            ImageView imageview = mainImage.get();
                            ImageView imageview2 = profileImage.get();
                            if (imageview != null) {
                                if (listItem.getObjectId() == null) {
                                    imageview.setImageBitmap(null);
                                } else {
                                    if (bitmapHolder != null) {

                                        imageview.setImageBitmap(cache.get(listItem.getObjectId()));
                                        imageview2.setImageBitmap(pro);
                                    } else {
                                        imageview.setImageBitmap(null);

                                    }
                                }

                            }

                            ImageView proimageView = profileImage.get();
                            if (proimageView != null) {
                                proimageView.setImageBitmap(pro);
                            }
                            // RecyclerAdapter.this.notifyDataSetChanged();
                        }
                    };

                    holder.displayImage.setTag(task);

                    task.execute();


                } else {
                    //disk is not null


                    cache.put(listItem.getObjectId(), ThumbnailCache.DiskCache.get(listItem.getObjectId()));

                    holder.displayImage.setImageBitmap(cache.get(listItem.getObjectId()));

                }


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
