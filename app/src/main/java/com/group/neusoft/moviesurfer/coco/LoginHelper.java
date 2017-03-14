package com.group.neusoft.moviesurfer.coco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group.neusoft.moviesurfer.disordia.util.MainPageActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

import com.group.neusoft.moviesurfer.R;

/**
 * Created by ttc on 2017/3/13.
 */

public class LoginHelper{

    private static final String TAG="LoginHelper";
    private static final String APP_ID="1106028412";
    private Tencent mtencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private  static  Context sContext;
    static User user=new User();
    private boolean isLogin=false;
    private static Semaphore mSemaphore;

    private  static  LoginHelper loginHelper;

    public  static  LoginHelper getLoginHelper(Context context){
        if(loginHelper==null){
            loginHelper=new LoginHelper(context);
        }
        return loginHelper;
    }

    private LoginHelper(Context context){
        sContext=context;
        mtencent= Tencent.createInstance(APP_ID,context);
        mSemaphore=new Semaphore(0);
    }

    public  void   QQlogin(){
        mIUiListener=new BaseUiListener();
        mtencent.login((Activity) sContext,"all", (IUiListener) mIUiListener);
    }
    public  void ShareinTencent(String content,String link,String imgUrl){
        mIUiListener=new BaseUiListener();
        ShareUtil.shareQQ((Activity)sContext,
                content,
                link,
                imgUrl,
                mIUiListener);
    }

    public void LogOut(){
        mtencent.logout(sContext);
        isLogin=false;
    }

    public boolean IsUserLogin(){
        return isLogin;
    }

    private  class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(final Object response) {
            Toast.makeText((Activity)sContext,"sucess",Toast.LENGTH_SHORT).show();
            isLogin=true;
            JSONObject obj=(JSONObject) response;
            try {
                String openID=obj.getString("openid");
                String accessToken=obj.getString("access_token");
                String expires=obj.getString("expires_in");
                mtencent.setOpenId(openID);
                mtencent.setAccessToken(accessToken,expires);
                QQToken qqToken=mtencent.getQQToken();
                mUserInfo=new UserInfo(sContext,qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.e(TAG,"succees"+response.toString());
                        final JSONObject json=(JSONObject) o;
                        try {
                            Log.e("name", json.getString("nickname"));
                            user.setName(json.getString("nickname"));
                            Log.e("address", json.getString("city"));
                            Log.e("sex", json.getString("gender"));
                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        Thread infothread=new Thread() {
                            @Override
                            public void run() {
                                // 返回的arg0 包含QQ个人所有的信息
                                Log.e("json", json + "");
                                // 获取头像
                                if (json.has("figureurl")) {// 判断字段是否为空
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = getbitmap(json
                                                .getString("figureurl_qq_2"));
                                        user.setPicture(bitmap);
                                        // 根据网址加载网络图片
                                    } catch (JSONException e) {

                                    }
                                }
                                Intent refresh_intent=new Intent(MainPageActivity.BroadCastAction);
                                LocalBroadcastManager.getInstance(sContext).sendBroadcast(refresh_intent);
                            }

                        };
                        infothread.start();
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"fail"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"cancle");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText((Activity)sContext,"fail",Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onCancel() {
            Toast.makeText((Activity)sContext,"cancle",Toast.LENGTH_SHORT).show();
        }
    }

    public  User  getUserInformation(){
        return user;
    }



    //加载图片类

    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            Log.e("url", "url=" + imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            // 加载网络图片
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }



    public void  onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode== Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        if(requestCode==Constants.REQUEST_QQ_SHARE){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
    }


}
