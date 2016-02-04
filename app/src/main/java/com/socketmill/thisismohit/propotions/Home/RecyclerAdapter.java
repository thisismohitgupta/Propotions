package com.socketmill.thisismohit.propotions.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseFile;
import com.parse.ParseObject;

import com.socketmill.thisismohit.propotions.JniBitmapHolder;

import com.socketmill.thisismohit.propotions.Profile;
import com.socketmill.thisismohit.propotions.R;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;
import com.socketmill.thisismohit.propotions.widget.GifMovieView;


import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by thisismohit on 3/1/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {


    private List<ParseObject> listItemsList ;
    private Context context ;
    JniBitmapHolder bitmapHolder ;
    JniBitmapHolder bitmapHolder2;
    boolean likeFlag;



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
    public void onBindViewHolder(final HomeRecyclerViewHolder holder, int position) {
       final ParseObject listItem = listItemsList.get(position);

        holder.getLayoutPosition();


        if (holder.mainimages.getChildCount() > 1) {
            holder.mainimages.removeViewAt(1);


        }
        holder.username.setText( listItem.getParseUser("user").get("displayName").toString() ) ;
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Profile.class);
                i.putExtra("parseUserId", listItem.getParseUser("user").getUsername());
                i.putExtra("parseUserObjectId", listItem.getParseUser("user").getObjectId());

                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        final WeakReference<ImageView> likeReff = new WeakReference<ImageView>(holder.likeButton);
        final WeakReference<TextView> likeCountReff = new WeakReference<TextView>(holder.likeButtonCount);

        final Reactions Reaction = new Reactions(listItem, false);

        likeFlag = Reaction.picLikedorNotCheck(listItem, likeReff, false, true, likeCountReff);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Reactions Reaction = new Reactions(listItem, false);
                Reaction.picLikedorNotCheck(listItem, likeReff, false, false, likeCountReff);

            }
        });
        holder.commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, commentDetail.class);
                i.putExtra("PhotoId", listItem.getObjectId());
                i.putExtra("UserThumb", listItem.getParseUser("user").getUsername());
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

                //Toast.makeText(context, String.valueOf(ShareButton.getId()), Toast.LENGTH_SHORT).show();

            }
        });


        final WeakReference<ImageView> mainImage = new WeakReference<ImageView>(holder.displayImage);

        final WeakReference<CircleImageView> profileImage = new WeakReference<CircleImageView>(holder.ProfileImage);


        final AsyncTask oldTask = (AsyncTask) holder.displayImage.getTag();
        if (oldTask != null) {
            oldTask.cancel(false);
        }
        final AsyncTask oldTask2 = (AsyncTask) holder.ProfileImage.getTag();
        if (oldTask2 != null) {
            oldTask2.cancel(false);
        }

        Bitmap images ;
        final DisplayMetrics matrix = mainImage.get().getContext().getResources().getDisplayMetrics();

            if (ThumbnailCache.cache.get(listItem.getObjectId()) == null) {






                AsyncTask taskAnother = new AsyncTask() {
                    Bitmap images;

                    GifMovieView gif;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        gif = new GifMovieView(mainImage.get().getContext());
                        gif.setMovieResource(R.drawable.loaderimage);

                        gif.setLayoutParams(mainImage.get().getLayoutParams());
                        holder.mainimages.addView(gif, 1);


                    }

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

                        images = bitmapHolder.getBitmapAndFree();

                        holder.mainimages.removeViewAt(1);
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


        if (ThumbnailCache.cache.get(listItem.getParseUser("user").getUsername()) == null) {


            AsyncTask taskAnother2 = new AsyncTask() {

                Bitmap images;

                @Override
                protected Object doInBackground(Object[] params) {
                    images = ThumbnailCache.DiskCache.get(listItem.getObjectId());
                    if (images == null) {
                        Bitmap pro;
                        ParseFile thumObject = (ParseFile) listItem.getParseUser("user").get("profilePictureSmall");
                        try {

                            byte[] datapro = thumObject.getData();
                            if (datapro != null) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = false;
                                images = BitmapFactory.decodeByteArray(datapro, 0, datapro.length, options);
                                bitmapHolder2 = new JniBitmapHolder();
                                bitmapHolder2.storeBitmap(images);
                                //bitmapHolder.convertGreyScale();
                                bitmapHolder2.scaleBitmap(50, 50, JniBitmapHolder.ScaleMethod.BilinearInterpolation);
                                //   pro = BitmapFactory.decodeByteArray(datapro, 0, datapro.length, options);


                                images = null;

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    CircleImageView imageview2 = profileImage.get();
                    if (images == null) {

                        images = bitmapHolder2.getBitmapAndFree();

                        imageview2.setImageDrawable(new BitmapDrawable(profileImage.get().getContext().getResources(), images));
                        ThumbnailCache.cache.put(listItem.getParseUser("user").getUsername(), images);
                        AsyncTask taskmee = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                ThumbnailCache.DiskCache.put(listItem.getParseUser("user").getUsername(), images);
                                return null;
                            }
                        };

                        taskmee.execute();

                    } else {


                        imageview2.setImageDrawable(new BitmapDrawable(profileImage.get().getContext().getResources(), images));


                    }


                }
            };

            profileImage.get().setTag(taskAnother2);

            taskAnother2.execute();

        } else {

            holder.ProfileImage.setImageDrawable(new BitmapDrawable(profileImage.get().getContext().getResources(), ThumbnailCache.cache.get(listItem.getParseUser("user").getUsername())));

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
