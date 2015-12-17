package com.socketmill.thisismohit.propotions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.*;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.junit.After;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/**
 * Created by thisismohit on 03/12/15.
 */
public class AfterPicTakenImageCommentAddingClass extends AppCompatActivity {


    ImageButton CrossButton;
    ImageButton doneAddingComment;
    ImageView ImageviewInUploadActivity;
    public ProgressDialog progess ;
    com.socketmill.thisismohit.propotions.upload.uploadAfterClicking uploadAfterCLicking ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_pic_taken_image_comment_adding_class);

        //getSupportActionBar().hide();
        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());

        CrossButton = (ImageButton) findViewById(R.id.imgClose_uploadActivity);
        ImageviewInUploadActivity = (ImageView) findViewById(R.id.ImageDisplayForUpload);


        Bundle extras = getIntent().getExtras();
        String PicturefileLocation = extras.getString("picLocation");
        String ThumbnailfileLocation = extras.getString("thumbLocation");


        File imageFile = new File(PicturefileLocation);
        File ThumbFile = new File(ThumbnailfileLocation);

        try {


            if (imageFile.exists()) {


                Bitmap imageSet = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                Bitmap thumbSet = BitmapFactory.decodeFile(ThumbFile.getAbsolutePath());

                ImageviewInUploadActivity.setMinimumHeight(imageSet.getHeight());
                ImageviewInUploadActivity.setImageBitmap(imageSet);

                ParseUser currentUser = ParseUser.getCurrentUser();




                try {
                    final ParseObject imageUpload = new ParseObject("Photo");



                    ByteArrayOutputStream ImageStream = new ByteArrayOutputStream();
                    imageSet.compress(Bitmap.CompressFormat.JPEG,100,ImageStream);
                    byte[] ImageBytearray = ImageStream.toByteArray();

                    ImageStream.reset();

                    ByteArrayOutputStream ThumbStream = new ByteArrayOutputStream();
                    thumbSet.compress(Bitmap.CompressFormat.JPEG,100,ThumbStream);
                    byte[] ThumbBytearray = ThumbStream.toByteArray();

                    ThumbStream.reset();


                    //final ParseFile ImageMainfile = new ParseFile(PicturefileLocation, ImageBytearray);

                    //final ParseFile Thumbfile = new ParseFile(PicturefileLocation, ThumbBytearray);


                    uploadAfterCLicking = new com.socketmill.thisismohit.propotions.upload.uploadAfterClicking();

                    uploadAfterCLicking.bytePicBig = ImageBytearray ;
                    uploadAfterCLicking.bytePicSmall = ThumbBytearray ;
                    uploadAfterCLicking.profile = com.facebook.Profile.getCurrentProfile();
                    uploadAfterCLicking.context = getApplicationContext() ;
                    progess = new ProgressDialog(AfterPicTakenImageCommentAddingClass.this);
                    progess.setMessage("Just a Minute!");
                    progess.setTitle("Uploading");
                    uploadAfterCLicking.progess = progess ;
                    WeakReference<ProgressDialog> DialogReff = new WeakReference<ProgressDialog>(progess);


                    uploadAfterCLicking.view = (RelativeLayout)findViewById(R.id.relitiveLayout) ;

                    uploadAfterCLicking.execute();

                    if (currentUser != null) {
                        // do stuff with the user

                        Toast.makeText(getApplicationContext(), currentUser.getUsername(),Toast.LENGTH_LONG).show();




                    } else {


                    }








                } catch (Exception e) {


                }


            }

        } catch (Exception e) {

            // Log.d("ERROR@",e.getMessage());


        }


        ImageviewInUploadActivity = (ImageView) findViewById(R.id.ImageDisplayForUpload);
        doneAddingComment = (ImageButton)findViewById(R.id.imgTickDone) ;


        doneAddingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // progess = ProgressDialog.show(AfterPicTakenImageCommentAddingClass.this,"Uploading","Please Wait, Uploading!!");
                uploadAfterCLicking.progess.show();
            }
        });

        CrossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Videos.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);


            }
        });


    }

}
