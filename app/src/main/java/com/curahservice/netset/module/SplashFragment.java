package com.curahservice.netset.module;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.PlayGifView;

public class SplashFragment extends BaseFragment {
    private Runnable splashTask;
    private Handler handler;
    private PlayGifView loader;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideActionBar();
        return inflater.inflate(R.layout.splash, container, false);
    }

    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle();
        loader = view.findViewById(R.id.loader);
        loader.setImageResource(R.drawable.loader);
        handler = new Handler();
        splashTask();
    }

    private void splashTask() {
        if (!store.getBoolean(Const.LOGIN)) {
            splashTask = new Runnable() {
                @Override
                public void run() {
                    baseActivity.replaceFragment(R.id.container, new SigninFragment(), "SigninFragment");
                    //replaceFragmentNull(R.id.container, new SigninFragment(), "");
                }
            };
        }else {
            baseActivity.moveToNextActivity(DrawerActivity.class, null);
        }
        handler.postDelayed(splashTask, 1000);
    }
}
