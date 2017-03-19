package com.group.neusoft.moviesurfer.coco;

import android.content.Context;
import android.content.SharedPreferences;

import com.group.neusoft.moviesurfer.FilmSurferApplication;

/**
 * Created by ttc on 2017/3/15.
 */

public class Settings {
    public static final String SettingStr="AppSettings";
    private static boolean mIsListloadPic;



    public static boolean isListloadPic() {
        return mIsListloadPic;
    }

    public static void setListloadPic(boolean listloadPic) {
        mIsListloadPic = listloadPic;
        SaveSettings();
    }


    public static void LoadSettings(){
        SharedPreferences settingLoader= FilmSurferApplication.getContextObject().getSharedPreferences(SettingStr, Context.MODE_PRIVATE);
        mIsListloadPic=settingLoader.getBoolean("mIsListloadPic",true);
    }

    public static void SaveSettings(){
        SharedPreferences settingLoader= FilmSurferApplication.getContextObject().getSharedPreferences(SettingStr, Context.MODE_PRIVATE);
        SharedPreferences.Editor settingEditor=settingLoader.edit();
        //more added here:
        settingEditor.putBoolean("mIsListloadPic",mIsListloadPic);
        //end
        settingEditor.commit();
    }

}
