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
    private static final String testJsonString="[{\"id\":0,\"url\":\"www.baidu.com\",\"title\":\"\\u767e\\u5ea6\",\"downloadUrls\":\"www.hao123.com\",\"coverImgUrl\":\"www.qq.com\",\"scoreInfo\":\"3.4\\/10.0\",\"extra1\":\"baidu\",\"date\":\"2014\\u5e74\",\"extra2\":\"\\u641c\\u7d22\\u8d3c\\u5feb\"},{\"id\":1,\"url\":\"www.dwaw.com\",\"title\":\"sadsa\",\"downloadUrls\":\"afd\",\"coverImgUrl\":\"fdgf\",\"scoreInfo\":\"hdh\",\"extra1\":\"jf\",\"date\":\"jfj\",\"extra2\":\"tdh\"},{\"id\":2,\"url\":\"sdfa\",\"title\":\"asge\",\"downloadUrls\":\"gsw\",\"coverImgUrl\":\"gasg\",\"scoreInfo\":\"egsa\",\"extra1\":\"agsg\",\"date\":\"ea\",\"extra2\":\"gasdg\"},{\"id\":3,\"url\":\"25\",\"title\":null,\"downloadUrls\":null,\"coverImgUrl\":null,\"scoreInfo\":null,\"extra1\":null,\"date\":null,\"extra2\":null},{\"id\":4,\"url\":\"452\",\"title\":null,\"downloadUrls\":null,\"coverImgUrl\":null,\"scoreInfo\":null,\"extra1\":null,\"date\":null,\"extra2\":null},{\"id\":5,\"url\":\"gjh\",\"title\":null,\"downloadUrls\":null,\"coverImgUrl\":null,\"scoreInfo\":null,\"extra1\":null,\"date\":null,\"extra2\":null}]\n";


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
        //for test


    }

    private void addTestData(){
        mFilmInfos=new ArrayList<>();
        for(int i=0;i<20;i++) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setUrl("http://www.dytt8.net/html/gndy/dyzz/20170310/53447.html");
            filmInfo.setDownloadUrls("ftp://ygdy8:ygdy8@y153.dydytt.net:9212/[阳光电影www.ygdy8.com].刺客信条.HD.720p.中英双字幕.rmvb");
            filmInfo.setCoverImgUrl("http://image18.poco.cn/mypoco/myphoto/20170310/22/185036732201703102220023586320669253_000.jpg");
            filmInfo.setTitle("Assasine");
            filmInfo.setScoreInfo("IBSN score 7.0/10");
            filmInfo.setDate("2016");
            filmInfo.setExtra2("卡勒姆·林奇（迈克尔·法斯宾德 饰）在死刑即将执行之前清醒过来，发现他被索菲娅（玛丽昂·歌迪亚 饰）选中，来参加一个能让人类摆脱暴力冲动的计划。虚拟现实机器Animus能让用户体验祖先的记忆，被绑在机器上之后，卡勒姆·林奇意识到他是生活在西班牙宗教法庭时期一位刺客阿圭拉的后裔，他们寻找的是可以控制自由意志的伊甸园苹果。索菲娅在父亲艾伦（杰瑞米·艾恩斯 饰）施加的压力下不情愿地操纵着卡勒姆·林奇回到过去寻找伊甸园苹果在现代世界的下落，威胁着他身体和心理的健康。但在杀手同行穆萨（迈克尔·威廉姆斯 饰）暗示了卡勒姆·林奇，提醒他艾伦有可能动机不纯之后，卡勒姆·林奇开始重新考虑他的行为和动机，而人类自由意志的命运也变得悬而未决……");
            //end
            mFilmInfos.add(filmInfo);
        }
    }


    public static final String ServerUrl="";

    public List<FilmInfo> getFilmInfos() {
        return mFilmInfos;
    }

    public void GetFilmInfoAsync(RecyclerView view, FilmListFragment.FilmItemAdapter adpater){
        //addTestData();
        //adpater.setFilmInfos(mFilmInfos);
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
            String url = Uri.parse(strings[0])
                    .buildUpon().appendQueryParameter("page","100").build().toString();

                //String jsonString=NetHelper.getInstance().getUrlString(url);
                String jsonString=testJsonString;
                List<FilmInfo> filmInfos=JSON.parseArray(jsonString,FilmInfo.class);
                for (int i =0;i<filmInfos.size();i++){
                    LogUtil.print(filmInfos.get(i).toString());
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
