package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;

import com.alibaba.fastjson.JSON;
import com.group.neusoft.moviesurfer.FilmInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ttc on 2017/3/12.
 */

public class FilmLab {
    private static FilmLab sFilmLab;
    private static List<FilmInfo> mFilmInfos;
    private static Set<String> mInfosSet;
    private Context mContext;
    private static int mUid=1;
    public static final String ServerUrl="http://42.121.4.78:9099/filmdata.php";
    public static FilmLab getInstance(Context context){
        if(sFilmLab==null){
            sFilmLab=new FilmLab(context);
        }
        return sFilmLab;
    }


    private FilmLab(Context context) {
        mFilmInfos=new ArrayList<>();
        mInfosSet = new HashSet<>();
        mContext=context;
    }




    public List<FilmInfo> getFilmInfos() {
        return mFilmInfos;
    }

    public void GetFilmInfoAsync(RecyclerView view, FilmListFragment.FilmItemAdapter adpater){
        new FetchFilmInfoTask().setAdapter(adpater).setRecyclerView(view).execute(ServerUrl);
    }




    private class FetchFilmInfoTask extends AsyncTask<String,Void,List<FilmInfo>>{

        private FilmListFragment.FilmItemAdapter mAdapter;
        private RecyclerView mRecyclerView;

        public FetchFilmInfoTask setAdapter(FilmListFragment.FilmItemAdapter adapter) {
            mAdapter = adapter;
            return this;
        }

        public FetchFilmInfoTask setRecyclerView(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            return this;
        }

        @Override
        protected List<FilmInfo> doInBackground(String... strings) {
                String RequestUrl = Uri.parse(strings[0])
                    .buildUpon().appendQueryParameter("request","getfilminformation")
                        .appendQueryParameter("uid", String.valueOf(mUid)).build().toString();
                LogUtil.print("Request Url:"+RequestUrl);
            List<FilmInfo> filmInfos=new ArrayList<>();
            String jsonString= null;
            try {
                String resultStr = NetHelper.getInstance().getUrlString(RequestUrl);
                LogUtil.print(resultStr);
                int startPos=resultStr.indexOf("\n");
                LogUtil.print(resultStr.substring(0,startPos));
                LogUtil.print("UID:"+mUid);
                jsonString=resultStr.substring(startPos+("\n").length()-1,resultStr.length());
                LogUtil.print(jsonString);
                filmInfos=JSON.parseArray(jsonString,FilmInfo.class);
                for (int i =0;i<filmInfos.size();i++){
                    LogUtil.print(filmInfos.get(i).toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return filmInfos;
        }


        @Override
        protected void onPostExecute(List<FilmInfo> filmInfos) {
            boolean update=false;
            for(FilmInfo info :filmInfos){
                if(!mInfosSet.contains(info.getUrl())) {
                    LogUtil.print("Add one film");
                    mInfosSet.add(info.getUrl());
                    mFilmInfos.add(info);
                    update=true;
                }
            }
            LogUtil.print("Get over");
            mAdapter.setFilmInfos(mFilmInfos);
            mRecyclerView.setAdapter(mAdapter);
        }
    }


}
