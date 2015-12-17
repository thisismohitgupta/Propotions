package com.socketmill.thisismohit.propotions;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by thisismohit on 20/11/15.
 */
public class cameraClasss extends SurfaceView implements SurfaceHolder.Callback {

    //super.cameraClasss();


    private SurfaceHolder mHolder;
    private Camera mCamera;

    public cameraClasss(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        mCamera.setDisplayOrientation(90);

        mHolder = getHolder();

        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            //when the surface is created, we can set the camera to draw images in this surfaceholder
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if (mHolder.getSurface() == null)//check if the surface is ready to receive camera data
            return;

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
          //  safeToTakePicture = true;
        } catch (Exception e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();

        mCamera.release();


        Log.d("ERROR", "camera finnished");
    }


}
