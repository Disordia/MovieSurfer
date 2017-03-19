package com.group.neusoft.moviesurfer.cyf;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.disordia.util.DetailActivity;
import com.group.neusoft.moviesurfer.disordia.util.FilmListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NewFilmIntentService extends IntentService {
    //设置service启动频率
    private static final int POLL_INTERVAL = 1000 * 60; // 60 seconds
    //private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    private static int start = 0;

    public NewFilmIntentService() {
        super("NewFilmIntentService");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NewFilmIntentService.class);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.e("ofj","ok");
            if(start == 0) {
                QueryPreferences.setNewFilm(getApplicationContext(), "com.ofj.null");
                start = 1;
            }

            Resources resources = getResources(); //需要使用资源文件时


            try {
                FilmInfo new_film = new FilmInfo();
                new_film = parseFilm(new_film, fetchNewFilm());

                if(new_film != null) {
                    Intent i = DetailActivity.newIntent(this);
                    i.putExtra(FilmListFragment.INTENT_TAG, JSON.toJSONString(new_film));

                    PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
                    //Log.e("NOTIFICATION",QueryPreferences.getNewFilm(getApplicationContext()));
                    if(!(QueryPreferences.getNewFilm(getApplicationContext()).equals(new_film.getTitle())) || start == 0) {
                        start = 1;
                        QueryPreferences.setNewFilm(getApplicationContext(), new_film.getTitle());
                        //Log.e("NOTIFICATION",new_film.getTitle());
                        Notification notification = new NotificationCompat.Builder(this)
                                .setTicker("有新电影")
                                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                //.setContentTitle(resources.getString(R.string.new_pictures_title))
                                //.setContentText(resources.getString(R.string.new_pictures_text))
                                .setContentTitle(new_film.getTitle())
                                .setContentText("简介:" + new_film.getExtra2().trim())
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .build();
                        NotificationManagerCompat notificationManager =
                                NotificationManagerCompat.from(this);
                        notificationManager.notify(0, notification);
                    }
                }
            }catch (IOException e){
            }catch (JSONException e){
            }
        }
    }


    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = NewFilmIntentService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = NewFilmIntentService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public JSONArray fetchNewFilm() {
        JSONArray jsonArray = new JSONArray();
        try {
            String url = Uri.parse("http://42.121.4.78:9099/filmdata.php?")
                    .buildUpon()
                    .appendQueryParameter("request", "getfilminformation")
                    .appendQueryParameter("uid", "new")
                    .build().toString();
            String jsonString = getUrlString(url);
            jsonArray = new JSONArray(jsonString);
            //Log.i(TAG, "Received url: " + url);
            //Log.i(TAG, "Received JSON: " + jsonString);
        }catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch new film", ioe);
        }
        return jsonArray;
    }

    private FilmInfo parseFilm(FilmInfo film, JSONArray jsonArray)throws IOException, JSONException {
        JSONObject FilmJsonObject = (JSONObject)jsonArray.get(0);

        film.setUrl(FilmJsonObject.getString("url"));
        film.setTitle(FilmJsonObject.getString("title"));
        film.setExtra1(FilmJsonObject.getString("extra1"));
        film.setDownloadUrls(FilmJsonObject.getString("download_urls"));
        film.setCoverImgUrl(FilmJsonObject.getString("coverimg_url"));
        film.setScoreInfo(FilmJsonObject.getString("score_info"));
        film.setDate(FilmJsonObject.getString("date"));
        film.setExtra2(FilmJsonObject.getString("extra2"));

        return film;
    }

    //重复代码，使用时去掉
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
}
