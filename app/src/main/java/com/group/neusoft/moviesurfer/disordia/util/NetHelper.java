package com.group.neusoft.moviesurfer.disordia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ttc on 2017/3/12.
 */

public class NetHelper {
    private LruCache<String, Bitmap> mCache;//LruCache缓存对象
    private Set<FetchImageTask> mTask;//下载任务的集合
    private static NetHelper mNetHelper;
    private NetHelper(){
        mTask = new HashSet<>();
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置缓存的大小
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    public static NetHelper getInstance(){
        if (mNetHelper==null){
            mNetHelper=new NetHelper();
        }
        return mNetHelper;
    }


    /**
     * 将bitmap加入到缓存中
     *
     * @param url LruCache的键，即图片的下载路径
     * @param bitmap LruCache的值，即图片的Bitmap对象
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
    }

    /**
     * 从缓存中获取bitmap
     *
     * @param url LruCache的键，即图片的下载路径
     * @return 对应传入键的Bitmap对象，或者null
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = mCache.get(url);
        return bitmap;
    }


    /**
     * 取消所有下载任务
     */
    public void cancelAllTask() {
        if (mTask != null) {
            for (FetchImageTask task : mTask) {
                task.cancel(false);
            }
        }
    }



    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {

        return new String(getUrlBytes(urlSpec));

    }


    public void FillImageView(String url, ImageView imageView){
        FetchImageTask task= new FetchImageTask().setImageView(imageView);
        mTask.add(task);
        task.execute(url);
    }




    public class FetchImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView mImageView;
        public FetchImageTask setImageView(ImageView imageView) {
            mImageView = imageView;
            return this;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap cacheBitmap=getBitmapFromCache(strings[0]);
            if (cacheBitmap!=null){
                return cacheBitmap;
            }
            try {
                byte[] bitmapBytes = getUrlBytes(strings[0]);
                final Bitmap bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                addBitmapToCache(strings[0],bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
                if(image!=null){
                    mImageView.setImageBitmap(image);
                }
            mTask.remove(this);
        }
    }

}

