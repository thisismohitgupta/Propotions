package com.socketmill.thisismohit.propotions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.socketmill.thisismohit.propotions.cameraClasss;
import com.socketmill.thisismohit.propotions.MainActivity;
import com.socketmill.thisismohit.propotions.R;
import com.socketmill.thisismohit.propotions.toolbarNav;

public class Videos extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public ImageButton ImageCaptureButton;
    ImageView closeImage;
    FrameLayout camera_view;
    private Camera mCamera = null;
    private cameraClasss mCameraView = null;
    private boolean safeToTakePicture = true;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type, String Type) {
        return Uri.fromFile(getOutputMediaFile(type, Type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type, String Type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + Type + "_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        // getSupportActionBar().hide();
        setContentView(R.layout.activity_videos);


        toolbarNav NavController = new toolbarNav();
        NavController.checkIfLoggedInInternal(getApplicationContext());
        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new cameraClasss(this, mCamera);//create a SurfaceView to show camera data
            camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }


        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Matrix matrix = new Matrix();

                matrix.postRotate(90);


                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE, "CROP");
                camera.startPreview();


                Bitmap bitmap;
                if (pictureFile == null) {
                    //no path to picture, return

                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.flush();

                    fos.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();              //<-------- show exception
                } catch (IOException e) {
                    e.printStackTrace();              //<-------- show exception
                }

                //finished saving picture



                try {


                    BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use


                    bitmap = BitmapFactory.decodeByteArray(data,0,data.length,options);
//

                    bitmap = Bitmap.createScaledBitmap(bitmap,750,750*bitmap.getHeight()/bitmap.getWidth(),false);

                    //Bitmap cropImg = Bitmap.createScaledBitmap(bitmap,500,500*bitmap.getHeight()/bitmap.getWidth(),false);


                    int stuff, stuff2;

                    if (bitmap.getHeight() > bitmap.getWidth()){
                         stuff = bitmap.getWidth() ;

                    }
                    else {
                         stuff = bitmap.getHeight() ;
                    }
                   Bitmap cropImg = Bitmap.createBitmap(bitmap, 0, 0, stuff, stuff, matrix, true);






                    Bitmap Thumnail = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() / 4) , (bitmap.getWidth() / 4) * bitmap.getHeight()/bitmap.getWidth(), true);

                    bitmap.recycle();

                    if (Thumnail.getHeight() > Thumnail.getWidth()) {
                        stuff2 = Thumnail.getWidth();

                    } else {
                        stuff2 = Thumnail.getHeight();
                    }
                    Thumnail = Bitmap.createBitmap(Thumnail, 0, 0, stuff2, stuff2, matrix, true);




                    File thumbnail = getOutputMediaFile(MEDIA_TYPE_IMAGE, "THUMB");

                    FileOutputStream fOut = new FileOutputStream(pictureFile);


                    FileOutputStream ThumbnailOutput = new FileOutputStream(thumbnail);


                    Thumnail.compress(Bitmap.CompressFormat.PNG, 100, ThumbnailOutput);


                    cropImg.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    //    cropImg.prepareToDraw();


                    fOut.flush();
                    fOut.close();
                    Thumnail.recycle();
                    cropImg.recycle();

                    ThumbnailOutput.flush();
                    ThumbnailOutput.close();

                    Log.e("ERROR","Muffle");
                    Intent i = new Intent(getApplicationContext(), AfterPicTakenImageCommentAddingClass.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("picLocation", pictureFile.toString());
                    i.putExtra("thumbLocation", thumbnail.toString());

                    getApplicationContext().startActivity(i);

                    //Toast.makeText(getApplicationContext(),pictureFile.toString(),Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }

                safeToTakePicture = true ;

            }

//                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//                if (pictureFile == null){
//
//                    return;
//                }
//
//                try {
//                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                    fos.write(data);
//                    fos.close();
//                } catch (FileNotFoundException e) {
//
//                   Log.e("hello","error not writting " + e.toString());
//
//                } catch (IOException e) {
//
//                    Log.e("helo", "error not writting " + e.toString());
//
//                }

        };


        ImageCaptureButton = (ImageButton) findViewById(R.id.imgCap);
        ImageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (safeToTakePicture) {

                    //ImageCaptureButton.setEnabled(false);
                    safeToTakePicture = false ;
                    mCamera.takePicture(null, null, mPicture);




                }
                //ImageCaptureButton.setEnabled(true);
            }
        });


        closeImage = (ImageView) findViewById(R.id.imgClose);



        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }


}
