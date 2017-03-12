package com.group.neusoft.moviesurfer.disordia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ttc on 2017/3/12.
 */

public class NetHelper {
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
        new FetchImageTask().setImageView(imageView).execute(url);
    }


    private class FetchImageTask extends AsyncTask<String,Void,Bitmap>{

        ImageView mImageView;

        public FetchImageTask setImageView(ImageView imageView) {
            mImageView = imageView;
            return this;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                byte[] bitmapBytes = getUrlBytes(strings[0]);
                final Bitmap bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
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
        }
    }

}

