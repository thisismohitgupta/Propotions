package com.socketmill.thisismohit.propotions.cache;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thisismohit on 2/1/16.
 */
public class ThumbnailCache extends LruCache<String, Bitmap> {
    public ThumbnailCache(int maxSizeBytes) {
        super(maxSizeBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }

   public static class cache {
       public static ThumbnailCache cachee ;
       public cache() {
           ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();



           long totalSize ;
           try {
               Runtime info = Runtime.getRuntime();
               totalSize = info.totalMemory();
           } catch (Exception e) {
               Runtime info = Runtime.getRuntime();
               totalSize = info.totalMemory();
               e.printStackTrace();
           }



           int size = (int) ( (totalSize / 1024L) / 1024L);

           cachee = new ThumbnailCache(size*1024*1024);

       }


       public static Bitmap get(String key){
           if(cachee.get(key) != null)
           {
               Log.e("ERROR","I get cache i get cahe ");
           return    cachee.get(key);
           }else {

               return null ;
           }


       }

       public static void put(String key,Bitmap bmp){

           if(get(key) == null){
               cachee.put(key,bmp);
           }

       }



   }



}



