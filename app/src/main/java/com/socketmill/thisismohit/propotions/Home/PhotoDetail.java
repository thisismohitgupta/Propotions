package com.socketmill.thisismohit.propotions.Home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.jar.Attributes;

import com.socketmill.thisismohit.propotions.Login;

import com.socketmill.thisismohit.propotions.R;

import org.w3c.dom.Comment;

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
        //String ParseUserObjectId = getIntent().getStringExtra("UserObjectID");

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


}
