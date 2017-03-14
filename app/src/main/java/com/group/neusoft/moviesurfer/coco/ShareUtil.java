package com.group.neusoft.moviesurfer.coco;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.qqconnect.dataprovider.datatype.TextAndMediaPath;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.List;

/**
 * Created by ttc on 2017/3/13.
 */

public class ShareUtil {
    public static  final  String QQ_APP_ID="1106028412";
    public static  final String TAG="ShareUtil";
    public  static Tencent mTencent;
    public  static  void shareQQ(Activity activity, String content , String sharePath,String imgUrl, IUiListener listener ){

        mTencent=Tencent.createInstance(QQ_APP_ID,activity);
        if (isQQClientAvailable(activity)) {

//      String content = activity.getResources().getString(R.string.ivQrcode_content);

            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, "MovieSurfer");
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imgUrl);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, sharePath);
            //params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://www.baidu.com/img/bd_logo1.png");

            mTencent.shareToQQ(activity, params, listener);
        } else {
            // UIUtils.showToast(activity, "请检查是否安装最新版QQ！");
        }


    }

    private static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager=context.getPackageManager();
        List<PackageInfo> pinfo=packageManager.getInstalledPackages(0);
        if(pinfo!=null){
            for(int i=0;i<pinfo.size();i++){
                String pn=pinfo.get(i).packageName;
                if(pn.equals("com.tencent.mobileqq")){
                    return true;
                }
            }
        }
        return true;
    }

    private static TextAndMediaPath getTextObj(Activity activity, String sharePath) {
        TextAndMediaPath textObject = new TextAndMediaPath("##################...@" + sharePath,
                "picture URL");
        return textObject;
    }


}
