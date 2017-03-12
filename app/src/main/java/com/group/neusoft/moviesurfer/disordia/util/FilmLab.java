package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Context;

import com.group.neusoft.moviesurfer.FilmInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttc on 2017/3/12.
 */

public class FilmLab {
    private static FilmLab sFilmLab;
    private List<FilmInfo> mFilmInfos;
    private Context mContext;
    public static FilmLab get(Context context){
        if(sFilmLab==null){
            sFilmLab=new FilmLab(context);
        }
        return sFilmLab;
    }


    private FilmLab(Context context) {
        mFilmInfos = new ArrayList<>();
        mContext=context;
        //for test
        for(int i=0;i<20;i++) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setUrl("http://www.dytt8.net/html/gndy/dyzz/20170310/53447.html");
            filmInfo.setDownloadUrls("ftp://ygdy8:ygdy8@y153.dydytt.net:9212/[阳光电影www.ygdy8.com].刺客信条.HD.720p.中英双字幕.rmvb");
            filmInfo.setCoverImgUrl("http://image18.poco.cn/mypoco/myphoto/20170310/22/185036732201703102220023586320669253_000.jpg");
            filmInfo.setTitle("Assasine");
            filmInfo.setScoreInfo("IBSN score 7.0/10");
            filmInfo.setDate("2016");
            //end
            mFilmInfos.add(filmInfo);
        }

    }

    public List<FilmInfo> getFilmInfos() {
        return mFilmInfos;
    }
}
