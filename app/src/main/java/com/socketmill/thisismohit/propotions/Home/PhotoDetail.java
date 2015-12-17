package com.socketmill.thisismohit.propotions.Home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.jar.Attributes;

import com.socketmill.thisismohit.propotions.Login;
import com.socketmill.thisismohit.propotions.R;

public class PhotoDetail extends AppCompatActivity {

    Bitmap bmp,pro ;
    TextView NameTag ;
    String name;
    ImageView MainImage,ProfileImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        MainImage = (ImageView)findViewById(R.id.MainImage);
        ProfileImage = (ImageView)findViewById(R.id.ProfilePic);
        NameTag = (TextView)findViewById(R.id.NameTag);

        String ParseObjectID = getIntent().getStringExtra("PhotoId") ;
        String Username = getIntent().getStringExtra("UserThumb") ;
        String ParseUserObjectId = getIntent().getStringExtra("UserObjectID");

        bmp = Login.getBitmapFromMemoryCache(ParseObjectID);
        pro = Login.getBitmapFromMemoryCache(Username + "thumb");
        name = Login.getStringFromMemoryCache(Username + "profileName");

        Drawable MainImageDrawable = new BitmapDrawable(getApplicationContext().getResources(),bmp);
        Drawable ProDrawable = new BitmapDrawable(getApplicationContext().getResources(),pro);

        if(pro==null){

            Toast.makeText(getApplicationContext(),"Profile pic not Loaded",Toast.LENGTH_SHORT).show();
        }else {


            Toast.makeText(getApplicationContext(),"Profile pic Loaded",Toast.LENGTH_SHORT).show();
        }

        NameTag.setText(name);
        MainImage.setBackground(MainImageDrawable);
        ProfileImage.setBackground(ProDrawable);

        //bmp.recycle();
        //pro.recycle();






        Log.e("ERROR"," Real Lay Back");

    }


    public void  ObjectToFind(String ParsePhotoObjectId,String ParseObjectID) {

        //Find the Parse Object of photo
        final ParseQuery ActivityObejctQuery = ParseQuery.getQuery("Activity");
        ParseQuery PhotoObject = ParseQuery.getQuery("Photos") ;
        PhotoObject.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        PhotoObject.getInBackground(ParseObjectID, new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if(e==null){


                    ActivityObejctQuery.whereEqualTo("photo", parseObject);
                    ActivityObejctQuery.whereEqualTo("type","comment");
                    ActivityObejctQuery.include("fromUser");
                    ActivityObejctQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);

                }

            }


        });





       // return ParseQuery<ParseObject>







    }


}
