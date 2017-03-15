package com.group.neusoft.moviesurfer.coco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.group.neusoft.moviesurfer.FilmSurferApplication;
import com.group.neusoft.moviesurfer.disordia.util.LogUtil;
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

import static com.group.neusoft.moviesurfer.coco.ShareUtil.TAG;

/**
 * Created by ttc on 2017/3/13.
 */

public class LoginHelper{
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private Context mContext;
    static User user=new User();
    private boolean isLogin=false;
    private  static  LoginHelper loginHelper;
    private Long mExpress_in;
    private String mAccessToken;
    private String mOpenId;

    //for single instance to use:
    public  static  LoginHelper getLoginHelper(Context context){
        if(loginHelper==null){
            loginHelper=new LoginHelper(context);
        }
        return loginHelper;
    }

    private LoginHelper(Context context){
        mContext=context;
        //initialize the tencent
        mTencent = Tencent.createInstance(Constant.APP_ID,context);
        //set appID and token:
        SharedPreferences tokenReader= FilmSurferApplication.getContextObject().getSharedPreferences(Constant.TokenKey,Context.MODE_PRIVATE);
        if(tokenReader!=null) {
            mAccessToken = tokenReader.getString(Constant.TokenKey, "");
            if(!mAccessToken.isEmpty()) {
                mExpress_in = tokenReader.getLong(Constant.Express_in, 0);
                mOpenId=tokenReader.getString(Constant.Open_id,"");
                LogUtil.print("AccessToken" + mAccessToken + "Express_in" + mExpress_in);
                Long express_in =(mExpress_in- System.currentTimeMillis() )/ 1000;
                mTencent.setOpenId(mOpenId);
                mTencent.setAccessToken(mAccessToken, express_in.toString());
                isLogin=true;
                if(mTencent.isSessionValid()){
                    GetQQUserInfo();
                }
            }
        }
    }

    //
    public  void   QQlogin(){
        mIUiListener=new BaseUiListener();
        mTencent.login((Activity) mContext, Constant.Scope,mIUiListener);
    }

    public  void ShareinTencent(String content,String link,String imgUrl){
        mIUiListener=new BaseUiListener();
        ShareUtil.shareQQ((Activity)mContext,
                content,
                link,
                imgUrl,
                mIUiListener);
    }

    public void LogOut(){
        mTencent.logout(mContext);
        isLogin=false;
    }

    public boolean IsUserLogin(){
        return isLogin;
    }

    private  class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(final Object response) {
            Toast.makeText((Activity)mContext,"sucess",Toast.LENGTH_SHORT).show();
            isLogin=true;
            JSONObject obj=(JSONObject) response;
            try {
                String openID=obj.getString("openid");
                String accessToken=obj.getString("access_token");
                String expires=obj.getString("expires_in");
                //save shared preference:
                SharedPreferences tokenSaver=FilmSurferApplication.getContextObject().getSharedPreferences(Constant.TokenKey,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=tokenSaver.edit();
                editor.putString(Constant.TokenKey,accessToken);
                editor.putLong(Constant.Express_in,System.currentTimeMillis() + Long.parseLong(expires) * 1000);
                editor.putString(Constant.Open_id,openID);
                editor.commit();
                //end save
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                GetQQUserInfo();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onError(UiError uiError) {
            Toast.makeText((Activity)mContext,"fail",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText((Activity)mContext,"cancle",Toast.LENGTH_SHORT).show();
        }
    }

    private void GetQQUserInfo(){
        QQToken qqToken= mTencent.getQQToken();
        mUserInfo=new UserInfo(mContext,qqToken);
        mUserInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
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
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(refresh_intent);
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
