/*
package com.curahservice.netset.module;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseActivity;
import com.curahservice.netset.util.MarshMallowPermission;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private Timer timer;
    private SplashActivity context;
    private MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_a);
        this.context=this;
        checkPermissions();
        //nextAction(3000);
    }

    private void nextAction(long delay) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                moveToNextActivity(MainActivity.class, null);
            }
        }, delay);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MarshMallowPermission.MAIN_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    nextAction(3000);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                    System.exit(0);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void checkPermissions() {
        if (marshMallowPermission.CameraPermission() &&
                marshMallowPermission.WriteStoragePermission()) {
            //from here to jump on next action
            nextAction(3000);
        } else {
            marshMallowPermission.requestPermissionDialog();
        }
    }
}
*/
