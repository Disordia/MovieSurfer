package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.cyf.NetHelper;

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
    private static List<FilmInfo> mSearchResults;
    private static Set<String> mInfosSet;
    private Context mContext;
    private static int mUid=1;

    private void initUID(){
        mUid=1;
    }


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

    public void SearchFilmAsync(RecyclerView view,FilmListFragment.FilmItemAdapter adpater,String searchTitle){
        if(searchTitle==null||searchTitle.isEmpty()){
            return;
        }else {
            LogUtil.print("search task excute");
            new FetchFilmInfoTask().setAdapter(adpater).setTitle(searchTitle).setRecyclerView(view).execute(ServerUrl);
        }

    }


    public void GetFilmInfoAsync(RecyclerView view, FilmListFragment.FilmItemAdapter adpater,boolean refresh){
        if(refresh==true){
            initUID();
            mFilmInfos.clear();
            mInfosSet.clear();
        }
        LogUtil.print("get infos task ");
        adpater.setFilmInfos(mFilmInfos);
        view.setAdapter(adpater);
        if(mFilmInfos.size()>0&&refresh==false){
            return;
        }
        new FetchFilmInfoTask().setAdapter(adpater).setRecyclerView(view).execute(ServerUrl);
    }

    public void GetFilmInfoAsyncMore(RecyclerView view, FilmListFragment.FilmItemAdapter adpater){
        new FetchFilmInfoTask().setAdapter(adpater).setRecyclerView(view).execute(ServerUrl);
    }




    private class FetchFilmInfoTask extends AsyncTask<String,Void,List<FilmInfo>>{

        private FilmListFragment.FilmItemAdapter mAdapter;
        private RecyclerView mRecyclerView;
        private String mSearchTitle;


        public FetchFilmInfoTask setAdapter(FilmListFragment.FilmItemAdapter adapter) {
            mAdapter = adapter;
            return this;
        }

        public FetchFilmInfoTask setTitle(String searchTitle){
            mSearchTitle=searchTitle;
            return this;
        }

        public FetchFilmInfoTask setRecyclerView(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            return this;
        }

        @Override
        protected List<FilmInfo> doInBackground(String... strings) {
            String RequestUrl="";
            List<FilmInfo> filmInfos=new ArrayList<>();
            String jsonString= null;
            if(mSearchTitle==null||mSearchTitle.isEmpty()) {
                RequestUrl = Uri.parse(strings[0])
                        .buildUpon().appendQueryParameter("request", "getfilminformation")
                        .appendQueryParameter("uid", String.valueOf(mUid)).build().toString();
                LogUtil.print("Request Url:" + RequestUrl);
                try {
                    String resultStr = NetHelper.getInstance().getUrlString(RequestUrl);
                    LogUtil.print(resultStr);
                    int startPos=resultStr.indexOf("\n");
                    LogUtil.print(resultStr.substring(0,startPos));
                    mUid= Integer.parseInt(resultStr.substring(0,startPos));
                    LogUtil.print("UID:"+mUid);
                    jsonString=resultStr.substring(startPos+("\n").length()-1,resultStr.length());
                    LogUtil.print(jsonString);
                    filmInfos=JSON.parseArray(jsonString,FilmInfo.class);
                    for (int i =0;i<filmInfos.size();i++){
                        LogUtil.print(filmInfos.get(i).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                RequestUrl = Uri.parse(strings[0])
                        .buildUpon().appendQueryParameter("request", "getfilminformation")
                        .appendQueryParameter("title", mSearchTitle).build().toString();
                LogUtil.print("Request Url:" + RequestUrl);
                try {
                    String resultStr = NetHelper.getInstance().getUrlString(RequestUrl);
                    jsonString=resultStr;
                    LogUtil.print(jsonString);
                    filmInfos=JSON.parseArray(jsonString,FilmInfo.class);
                    for (int i =0;i<filmInfos.size();i++){
                        LogUtil.print(filmInfos.get(i).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return filmInfos;
        }


        @Override
        protected void onPostExecute(List<FilmInfo> filmInfos) {
            if(mSearchTitle==null||mSearchTitle.isEmpty()) {
                for (FilmInfo info : filmInfos) {
                    if (!mInfosSet.contains(info.getUrl())) {
                        LogUtil.print("Add one film");
                        mInfosSet.add(info.getUrl());
                        mFilmInfos.add(info);
                    }
                }
                LogUtil.print("Get over");
                mAdapter.setFilmInfos(mFilmInfos);
                mAdapter.notifyDataSetChanged();
                //mRecyclerView.setAdapter(mAdapter);
            }else {
                LogUtil.print("search over");
                mSearchResults=filmInfos;
                mAdapter.setFilmInfos(mSearchResults);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }//end FethchInfo class


}
