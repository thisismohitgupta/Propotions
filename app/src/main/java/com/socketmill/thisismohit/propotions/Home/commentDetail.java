package com.socketmill.thisismohit.propotions.Home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.socketmill.thisismohit.propotions.Login;
import com.socketmill.thisismohit.propotions.R;

import java.util.List;

public class commentDetail extends AppCompatActivity {

    EditText Comment ;
    Button submitComment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentfragment);

        getSupportActionBar().setTitle("Comments");

       submitComment = (Button)findViewById(R.id.submitComment);
        Comment = (EditText)findViewById(R.id.CommentAdd);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Comment!=null){
                    if(Comment.getText().toString().equals("")  ){}else {
                        ParseObject commentActivity = new ParseObject("Activity");
                        commentActivity.put("type","comment");
                        commentActivity.put("content",Comment.getText().toString());
                        try {
                            ParseQuery PhotoObject = ParseQuery.getQuery("Photo") ;
                            ParseObject parseObject = PhotoObject.get(getIntent().getStringExtra("PhotoId"));
                            commentActivity.put("fromUser", ParseUser.getCurrentUser());
                            commentActivity.put("photo",parseObject);
                            commentActivity.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Comment not Added",Toast.LENGTH_SHORT).show();


                        }
                    }


                }

            }
        });



        String ParseObjectID = getIntent().getStringExtra("PhotoId") ;
        final LinearLayout LL = (LinearLayout)findViewById(R.id.commentContainer);

        ParseQuery<ParseObject> CommentQuery = ObjectToFind(ParseObjectID);

        CommentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for (int i = 0; i < list.size(); i++) {


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