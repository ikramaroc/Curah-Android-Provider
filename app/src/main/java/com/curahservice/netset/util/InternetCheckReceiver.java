package com.curahservice.netset.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseActivity;

public class InternetCheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!BaseActivity.getInstance().isNetworkAvailable()) {
                BaseActivity.getInstance().showAlertDialog(context, context.getResources().getString(R.string.connect_to_internet_first), "11");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*public boolean isNetworkAvailable1(Context context) {
        boolean isMobile = false, isWifi = false;
        NetworkInfo[] infoAvailableNetworks = getConnectivityManagerInstance(
                context).getAllNetworkInfo();
        if (infoAvailableNetworks != null) {
            for (NetworkInfo network : infoAvailableNetworks) {
                if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (network.isConnected() && network.isAvailable())
                        isWifi = true;
                }
                if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (network.isConnected() && network.isAvailable())
                        isMobile = true;
                }
            }
        }
        return isMobile || isWifi;
    }*/
}