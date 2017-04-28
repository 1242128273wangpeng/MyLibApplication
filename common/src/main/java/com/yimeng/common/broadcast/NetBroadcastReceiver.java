package com.yimeng.common.broadcast;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yimeng.common.widget.NoNetWorkNotice;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    private Activity activity;
    private NoNetWorkNotice noNetWorkNotice;

    public NetBroadcastReceiver(Activity activity) {
        this.activity = activity;
    }

    public NetBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (noNetWorkNotice == null) noNetWorkNotice = NoNetWorkNotice.getInstance(activity);
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : networkInfos) {
                NetworkInfo.State state = networkInfo.getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    if (null != noNetWorkNotice && noNetWorkNotice.isShowing()) {
                        noNetWorkNotice.cancel();
                    }
                    return;
                }
            }
            if (null != noNetWorkNotice && !noNetWorkNotice.isShowing()) {
                noNetWorkNotice.show();
            }
        }

    }


    public void onDestroy() {
        if (null != noNetWorkNotice) {
            if (noNetWorkNotice.isShowing()) {
                noNetWorkNotice.cancel();
            }
            activity = null;
            noNetWorkNotice = null;
        }
    }
}
