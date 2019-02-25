package com.curahservice.netset.retrofitManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.curahservice.netset.R;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static int TYPE_IS_CONNECTING = 3;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;

            NetworkInfo[] info = cm.getAllNetworkInfo();

            for (int i = 0; i < info.length; i++) {
                if (info[i].getDetailedState() == NetworkInfo.DetailedState.CONNECTING) {
                    return TYPE_IS_CONNECTING;
                }
            }
        }

        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.connection_error_message);
        } else if (conn == NetworkUtil.TYPE_IS_CONNECTING) {
            status = context.getString(R.string.poor_connection);
        }else {
            status = context.getString(R.string.connection_error_message);
        }
        return status;
    }

    public static boolean getConnectivityStatusBoolean(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        boolean status =false;
        if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = true;
        } else if (conn == NetworkUtil.TYPE_IS_CONNECTING) {
            status = false;
        }
        return status;
    }
}
