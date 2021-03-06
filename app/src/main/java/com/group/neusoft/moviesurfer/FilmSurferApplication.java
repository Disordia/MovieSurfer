package com.group.neusoft.moviesurfer;

import android.app.Application;
import android.content.Context;

import com.group.neusoft.moviesurfer.coco.Settings;

/**
 * 编写自己的Application，管理全局状态信息，比如Context
 * @author yy
 *
 */
public class FilmSurferApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
        Settings.LoadSettings();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}