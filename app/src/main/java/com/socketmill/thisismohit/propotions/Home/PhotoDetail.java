package com.socketmill.thisismohit.propotions.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.GetFileCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import com.parse.ParseUser;
import com.socketmill.thisismohit.propotions.JniBitmapHolder;
import com.socketmill.thisismohit.propotions.Login;

import com.socketmill.thisismohit.propotions.Profile;
import com.socketmill.thisismohit.propotions.R;
import com.socketmill.thisismohit.propotions.cache.ThumbnailCache;
import com.socketmill.thisismohit.propotions.toolbarNav;
import com.socketmill.thisismohit.propotions.widget.GifMovieView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotoDetail extends AppCompatActivity {

    Bitmap bmp,pro ;
    TextView NameTag ;
    String name;
    ImageView MainImage,ProfileImage ;
    ImageView likeButton;
    TextView likeButtonCount;
    ImageView commentView;
    JniBitmapHolder bitmapHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_photo_detail);
        getSupportActionBar().hide();

        final DisplayMetrics matrix = getApplicationContext().getResources().getDisplayMetrics();
        MainImage = (ImageView)findViewById(R.id.MainImage);
        likeButton = (ImageView) findViewById(R.id.likeButton1);

        ProfileImage = (CircleImageView) findViewById(R.id.ProfilePic);
        NameTag = (TextView)findViewById(R.id.NameTag);
        commentView = (ImageView) findViewById(R.id.commentButton1);
        final WeakReference<CircleImageView> circleImageViewWeakReference = new WeakReference<CircleImageView>((CircleImageView) ProfileImage);

        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(MainImage);
        String ParseObjectID = getIntent().getStringExtra("PhotoId") ;
        //String Username = getIntent().getStringExtra("UserThumb") ;
        //String Fullname = getIntent().getStringExtra("UserObjectID");


        bmp = ThumbnailCache.cache.get(ParseObjectID);
        Log.e("ERROR", ParseObjectID + "this is the id ");

        MainImage.setLayoutParams(new RelativeLayout.LayoutParams(matrix.widthPixels, matrix.widthPixels));
        final WeakReference<ImageView> likeReff = new WeakReference<ImageView>(likeButton);
        final WeakReference<TextView> likeCountReff = new WeakReference<TextView>(likeButtonCount);


        if (bmp == null) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
            query.whereEqualTo("objectId", ParseObjectID);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {

                    if (list != null && !list.isEmpty()) {
                        final ParseFile file = list.get(0).getParseFile("image");
                        setProfilePicture(list.get(0).getParseUser("user"), circleImageViewWeakReference);

                        final Reactions Reaction = new Reactions(list.get(0), false);

                        Boolean likeFlag = Reaction.picLikedorNotCheck(list.get(0), likeReff, false, true, likeCountReff);

                        likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Reactions Reaction = new Reactions(list.get(0), false);
                                Reaction.picLikedorNotCheck(list.get(0), likeReff, false, false, likeCountReff);

                            }
                        });
                        commentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(getApplicationContext(), commentDetail.class);
                                i.putExtra("PhotoId", list.get(0).getObjectId());
                                i.putExtra("UserThumb", list.get(0).getParseUser("user").getUsername());
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(i);

                                //Toast.makeText(context, String.valueOf(ShareButton.getId()), Toast.LENGTH_SHORT).show();

                            }
                        });

                        try {

                            AsyncTask task = new AsyncTask() {

                                GifMovieView gif;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();

                                    gif = new GifMovieView(getApplicationContext());
                                    gif.setMovieResource(R.drawable.loaderimage);

                                    gif.setLayoutParams(MainImage.getLayoutParams());
                                    RelativeLayout holder = (RelativeLayout) findViewById(R.id.HolderOfMainImage);
                                    holder.addView(gif, 1);


                                }

                                @Override
                                protected Object doInBackground(Object[] params) {
                                    try {
                                        byte[] dataMain = file.getData();

                                        if (dataMain != null) {
                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            options.inJustDecodeBounds = false;
                                            bmp = BitmapFactory.decodeByteArray(dataMain, 0, dataMain.length, options);
                                            bitmapHolder = new JniBitmapHolder();
                                            bitmapHolder.storeBitmap(bmp);
                                            bitmapHolder.scaleBitmap(matrix.widthPixels, matrix.widthPixels * bmp.getWidth() / bmp.getHeight(), JniBitmapHolder.ScaleMethod.BilinearInterpolation);

                                            bmp = bitmapHolder.getBitmapAndFree();

                                        }

                                    } catch (Exception e) {

                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    super.onPostExecute(o);

                                    Log.e("ERROR", "herherherhehrehrhehrehrhehrehrhehreh");

                                    imageViewWeakReference.get().setImageBitmap(bmp);


                                    RelativeLayout holder = (RelativeLayout) findViewById(R.id.HolderOfMainImage);
                                    holder.removeViewAt(1);

                                }
                            };
                            task.execute();


                            // byte[]  datapro = thumObject.getData();


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        NameTag.setText(list.get(0).getParseUser("user").get("displayName").toString());
                        NameTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getApplicationContext(), Profile.class);
                                i.putExtra("parseUserId", list.get(0).getParseUser("user").getUsername());
                                i.putExtra("parseUserObjectId", list.get(0).getParseUser("user").getObjectId());

                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(i);
                            }
                        });


                    }
                }
            });



        }else {


            //TODO: implement string cache
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
            query.whereEqualTo("objectId", ParseObjectID.toString());
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    NameTag.setText(list.get(0).getParseUser("user").get("displayName").toString());
                    MainImage.setImageBitmap(bmp);
                    setProfilePicture(list.get(0).getParseUser("user"), circleImageViewWeakReference);


                    final Reactions Reaction = new Reactions(list.get(0), false);

                    Boolean likeFlag = Reaction.picLikedorNotCheck(list.get(0), likeReff, false, true, likeCountReff);

                    likeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Reactions Reaction = new Reactions(list.get(0), false);
                            Reaction.picLikedorNotCheck(list.get(0), likeReff, false, false, likeCountReff);

                        }
                    });
                    commentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(getApplicationContext(), commentDetail.class);
                            i.putExtra("PhotoId", list.get(0).getObjectId());
                            i.putExtra("UserThumb", list.get(0).getParseUser("user").getUsername());
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(i);

                            //Toast.makeText(context, String.valueOf(ShareButton.getId()), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            });


            if (pro == null) {

                Toast.makeText(getApplicationContext(), "Profile pic not Loaded", Toast.LENGTH_SHORT).show();
            } else {


                Toast.makeText(getApplicationContext(), "Profile pic Loaded", Toast.LENGTH_SHORT).show();
            }


            //ProfileImage.setBackground(ProDrawable);
        }
        final LinearLayout LL = (LinearLayout)findViewById(R.id.commentContainer);

        ParseQuery<ParseObject> CommentQuery = ObjectToFind(ParseObjectID);

        CommentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (int i = 0; i < list.size(); i++) {


                    RelativeLayout Rl = new RelativeLayout(getApplicationContext());
                    TextView NameViewText = new TextView(PhotoDetail.this);
                    TextView CommentText = new TextView(PhotoDetail.this);

                    NameViewText.setTextColor(Color.BLUE);
                    NameViewText.setTextSize(12);
                    NameViewText.setId((i * i) + 1);

                    CommentText.setTextColor(Color.BLACK);
                    CommentText.setTextSize(12);
                    NameViewText.setId((i * i * i) + 1);
                    NameViewText.setText(list.get(i).getParseUser("fromUser").getString("displayName"));

                    CommentText.setText(list.get(i).getString("content"));

                    RelativeLayout.LayoutParams commentTextParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    commentTextParam.addRule(RelativeLayout.RIGHT_OF, NameViewText.getId());
                    CommentText.setLayoutParams(commentTextParam);
                    RelativeLayout.LayoutParams nameViewParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    nameViewParam.addRule(RelativeLayout.BELOW, R.id.MainImage);
                    NameViewText.setLayoutParams(nameViewParam);


                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    Rl.setLayoutParams(params);

                    Rl.addView(NameViewText);
                    Rl.addView(CommentText);
                    LL.addView(Rl);


                }

            }


        });



    }


    public ParseQuery<ParseObject>  ObjectToFind(String ParsePhotoObjectId) {

        //Find the Parse Object of photo
        final ParseQuery<ParseObject> ActivityObejctQuery = ParseQuery.getQuery("Activity");
        ParseQuery PhotoObject = ParseQuery.getQuery("Photo") ;
//        PhotoObject.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);

        try {
          ParseObject  parseObject =  PhotoObject.get(ParsePhotoObjectId);
            Log.e("ERROR", parseObject.getObjectId() + "fuck this shit");
            ActivityObejctQuery.whereEqualTo("photo", parseObject);
            ActivityObejctQuery.include("fromUser");
            ActivityObejctQuery.whereEqualTo("type", "comment");

            //ActivityObejctQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);


        }catch (Exception e){


            Log.e("Error",e.getMessage());

        }

                    return  ActivityObejctQuery;
       // return ParseQuery<ParseObject>


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_Bottom_photo);


        toolbarNav navigation = new toolbarNav();

        navigation.setNav(getApplicationContext(), toolbar, this);
        return true;
    }


    private void setProfilePicture(ParseUser user, final WeakReference<CircleImageView> targetImageView) {


        final JniBitmapHolder bitmapHolder2 = new JniBitmapHolder();
        ;

        try {

            if (ThumbnailCache.cache.get(user.fetchIfNeeded().getUsername()) == null) {
                ParseFile profilePic = user.getParseFile("profilePictureSmall");

                profilePic.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {

                        if (bytes != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = false;
                            Bitmap bro = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                            //bitmapHolder2 =
                            bitmapHolder2.storeBitmap(bro);
                            bitmapHolder2.scaleBitmap(75, 75, JniBitmapHolder.ScaleMethod.BilinearInterpolation);

                            bro = bitmapHolder2.getBitmapAndFree();


                            targetImageView.get().setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), bro));
                        }
                    }
                });
            } else {

                targetImageView.get().setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), ThumbnailCache.cache.get(user.getUsername())));


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


}
