package com.group.neusoft.moviesurfer.disordia.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ttc on 2017/3/13.
 */

public class RefreshBroadCastRecivever extends BroadcastReceiver {
    public interface RefreshCallBack{
        public void OnRefresh();
    }
    private RefreshCallBack mRefreshCallBack;
    public RefreshBroadCastRecivever(RefreshCallBack callBack){
        mRefreshCallBack=callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mRefreshCallBack.OnRefresh();
    }
}
