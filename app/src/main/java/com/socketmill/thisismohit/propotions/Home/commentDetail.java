package com.socketmill.thisismohit.propotions.Home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.socketmill.thisismohit.propotions.Login;
import com.socketmill.thisismohit.propotions.R;

import java.util.Date;
import java.util.List;


public class commentDetail extends AppCompatActivity {

    EditText Comment ;
    Button submitComment ;
    Date LastCommentDate ;
    private static Handler handler ;
    Boolean isRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.commentfragment);

        getSupportActionBar().setTitle("Comments");
        handler= new Handler();

       submitComment = (Button)findViewById(R.id.submitComment);
        Comment = (EditText)findViewById(R.id.CommentAdd);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Comment!=null){
                    if(Comment.getText().toString().trim().equals("")  ){}else {
                        String comment = Comment.getText().toString().trim() ;
                        Comment.setText("");
                        ParseObject commentActivity = new ParseObject("Activity");
                        commentActivity.put("type","comment");
                        commentActivity.put("content",comment);
                        try {
                            ParseQuery PhotoObject = ParseQuery.getQuery("Photo") ;
                            ParseObject parseObject = PhotoObject.get(getIntent().getStringExtra("PhotoId"));
                            commentActivity.put("toUser", parseObject.getParseUser("user"));
                            commentActivity.put("fromUser", ParseUser.getCurrentUser());
                            commentActivity.put("photo", parseObject);
                            ParseACL acl = new ParseACL(ParseUser.getCurrentUser()) ;
                            acl.setPublicReadAccess(true);
                            acl.setWriteAccess(parseObject.getParseUser("user"),true);
                            commentActivity.setACL(acl);
                            commentActivity.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                   // Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_SHORT).show();


                                    if(e!=null&&e.getCode() == 101){
                                    //  apologize to user since photo is deleted and comment not posted

                                    }

                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Comment Could not be Added",Toast.LENGTH_SHORT).show();


                        }
                    }


                }

            }
        });

        findcomments();




    }


    public ParseQuery<ParseObject>  ObjectToFind(String ParsePhotoObjectId) {

        //Find the Parse Object of photo
        final ParseQuery<ParseObject> ActivityObejctQuery = ParseQuery.getQuery("Activity");
        ParseQuery PhotoObject = ParseQuery.getQuery("Photo") ;
//        PhotoObject.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);

        try {
          ParseObject  parseObject =  PhotoObject.get(ParsePhotoObjectId);

            if (LastCommentDate != null) {
                ActivityObejctQuery.whereEqualTo("photo", parseObject);
                ActivityObejctQuery.whereGreaterThan("createdAt",LastCommentDate);
                ActivityObejctQuery.include("fromUser");
                ActivityObejctQuery.whereEqualTo("type", "comment");


                //ActivityObejctQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);

            }else {

                ActivityObejctQuery.whereEqualTo("photo", parseObject);

                ActivityObejctQuery.include("fromUser");
                ActivityObejctQuery.whereEqualTo("type", "comment");
            }

        }catch (Exception e){


            Log.e("Error",e.getMessage());

        }

                    return  ActivityObejctQuery;
       // return ParseQuery<ParseObject>


    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning=true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isRunning){
            isRunning = false ;
        }

    }


    public void findcomments (){


        String ParseObjectID = getIntent().getStringExtra("PhotoId") ;
        final LinearLayout LL = (LinearLayout)findViewById(R.id.commentlayout);

        ParseQuery<ParseObject> CommentQuery = ObjectToFind(ParseObjectID);

        CommentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if(e==null){

                    for (int i = 0; i < list.size(); i++) {

                        if(LastCommentDate == null || LastCommentDate.before(list.get(i).getCreatedAt() )){

                            LastCommentDate = list.get(i).getCreatedAt();
                        }

                        Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();

                        RelativeLayout Rl = new RelativeLayout(getApplicationContext());
                        TextView NameViewText = new TextView(commentDetail.this);
                        TextView CommentText = new TextView(commentDetail.this);

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

                        NameViewText.setLayoutParams(nameViewParam);


                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        Rl.setLayoutParams(params);

                        Rl.addView(NameViewText);
                        Rl.addView(CommentText);
                        LL.addView(Rl);


                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(isRunning){
                            findcomments();

                        }

                    }
                }, 3000);


            }


        });


    }


}
