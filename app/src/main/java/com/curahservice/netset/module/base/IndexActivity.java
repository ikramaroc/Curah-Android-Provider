package com.curahservice.netset.module.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.curahservice.netset.R;
import com.curahservice.netset.module.SigninFragment;
import com.curahservice.netset.module.SplashFragment;
import com.curahservice.netset.retrofitManager.networkCheck;
import com.jaeger.library.StatusBarUtil;

import io.fabric.sdk.android.Fabric;

public class IndexActivity extends BaseActivity implements View.OnClickListener,networkCheck {


    private Toolbar my_toolbar;
    private TextView text, skip;
    private ImageView back;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        StatusBarUtil.setTransparent(IndexActivity.this);
        regiterNetwork(IndexActivity.this);
        setContentView(R.layout.activity_start);
        my_toolbar = findViewById(R.id.my_toolbar);
        skip = findViewById(R.id.skip);

        back = my_toolbar.findViewById(R.id.back);
        my_toolbar.setBackgroundColor(Color.TRANSPARENT);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            addFragment(R.id.container, new SplashFragment(), "SplashFragment");
        }else {
            String val = extras.getString("logout4");
            if (!TextUtils.isEmpty(val)) {
                if (val.contains("123")) {
                    addFragment(R.id.container, new SigninFragment(), "SigninFragment");
                } else {
                    addFragment(R.id.container, new SplashFragment(), "SplashFragment");
                }
            }else if (extras.getString("AdminWait") !=null && extras.getString("AdminWait").equals("wait")){
                addFragment(R.id.container, new SigninFragment(), "AdminWait");
            }else {
                addFragment(R.id.container, new SplashFragment(), "SplashFragment");
            }
        }
        back.setOnClickListener(this);

    }

    public Toolbar getMy_toolbar() {
        return my_toolbar;
    }

    public void hideToolbar(int isShow){
        my_toolbar.setVisibility(isShow);
    }

    public void setTitle(@NonNull String title, boolean isback, View.OnClickListener onClickListener, int backgroundColor) {
        text = my_toolbar.findViewById(R.id.title);
        text.setText(title);
        skip.setVisibility(View.GONE);

        if (isback) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(onClickListener);
        } else {
            back.setVisibility(View.GONE);
        }
        if (backgroundColor!=0) {
            my_toolbar.setBackgroundColor(backgroundColor);
        }
    }

    public void skipText(int visiblty, String text,View.OnClickListener onClickListener) {
        skip.setVisibility(visiblty);
        skip.setText(text);
        skip.setOnClickListener(onClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (getSupportFragmentManager().getFragments().size() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finish();
            } else {
                try {
                    Toast.makeText(IndexActivity.this, "Tap back button again in order to exit", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mBackPressed = System.currentTimeMillis();
        }



        //Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onactivity","-----------------------INdex");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequestPermission","Start-----------");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void internetStatus(boolean connection) {
        if (connection){
            if (networkAlertDialog!=null && networkAlertDialog.isShowing()){
                networkAlertDialog.dismiss();
                internetConnection=true;
            }
            try {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(currentFragment).attach(currentFragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
