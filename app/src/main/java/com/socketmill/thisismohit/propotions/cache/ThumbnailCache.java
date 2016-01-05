package com.socketmill.thisismohit.propotions.cache;


import android.graphics.Bitmap;
import android.util.Log;

import com.socketmill.thisismohit.propotions.SimpleDiskCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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


    public static class DiskCache {


        private static SimpleDiskCache StringCache;


        public DiskCache(File file)

        {
            long totalSize;
            try {
                Runtime info = Runtime.getRuntime();
                totalSize = info.totalMemory();
            } catch (Exception e) {
                Runtime info = Runtime.getRuntime();
                totalSize = info.totalMemory();
                e.printStackTrace();
            }
            int size = (int) ((totalSize / 1024L) / 1024L);


            try {
                StringCache = SimpleDiskCache.open(file, 1, size / 5); // 10 MB

            } catch (Exception e) {

                Log.e("ERROR", "FUCKED UP");
                e.printStackTrace();
            }


        }


        public static Bitmap get(String key) {


            try {
                return StringCache.getBitmap(key).getBitmap();
            } catch (Exception e) {


            }
            return null;

        }

        public static void put(String key, Bitmap bitmap) {
            if (get(key) == null) {


                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    bos.close();
                    InputStream bs = new ByteArrayInputStream(bitmapdata);
                    StringCache.put(key, bs);
                    bitmapdata = null;
                    bitmap = null;
                    bs.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

   public static class cache {
       public static ThumbnailCache cachee ;
       public cache() {


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



