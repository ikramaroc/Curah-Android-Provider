package com.curahservice.netset.module;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.curahservice.ContactUs;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.Const;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SettingFragment extends BaseFragment implements View.OnClickListener {


    private ImageView notificationSwitch;
    private TextView aboutUsText;
    private TextView privacyPolicy;
    private TextView termsCondition;
    private TextView rateApp;
    private TextView logout, contact_us;
    Call<JsonObject> settingsApi;
    createAccount.UserInfo userInfo;
    boolean notification=false;
    private TextView changePass;
    private View ChangeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        userInfo = store.getUserPojo(Const.USERDATA);

        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        aboutUsText = view.findViewById(R.id.about_us_text);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        contact_us = view.findViewById(R.id.contact_us);
        termsCondition = view.findViewById(R.id.terms_condition);
        rateApp = view.findViewById(R.id.rateApp);
        logout = view.findViewById(R.id.logout);
        changePass=view.findViewById(R.id.changePass);
        ChangeView=view.findViewById(R.id.ChangeView);

        if (store.getBoolean(Const.SOCIAL_LOGIN)){
            changePass.setVisibility(View.GONE);
            ChangeView.setVisibility(View.GONE);
        }else {
            changePass.setVisibility(View.VISIBLE);
            ChangeView.setVisibility(View.VISIBLE);
        }

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.id.fl_container_home, new ChangePassord(), "", null);

            }
        });

        if (userInfo.getNotification_type().equals("ON")) {
            notificationSwitch.setImageResource(R.mipmap.ic_on);
            notification=true;
        }

        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!notification) {
                    userInfo.setNotification_type("ON");
                    changeNotification("ON");
                    notification=true;
                    notificationSwitch.setImageResource(R.mipmap.ic_on);
                } else {
                    userInfo.setNotification_type("OFF");
                    changeNotification("OFF");
                    notification=false;
                    notificationSwitch.setImageResource(R.mipmap.ic_off);
                }
            }
        });


        contact_us.setOnClickListener(this);
        rateApp.setOnClickListener(this);
        aboutUsText.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        termsCondition.setOnClickListener(this);


    }


    public void changeNotification(String changestatus) {
        settingsApi = apiInterfaceAuth.changeNotification(String.valueOf(store.getInt("userId")), changestatus);
        apiHitAndHandle.makeApiCall(settingsApi, SettingFragment.this);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == settingsApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    store.saveUserPojo(Const.USERDATA, userInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private View getViewChangeLl() {
        return (View) getView().findViewById(R.id.viewChange_ll);
    }*/

    @Override
    public void onClick(View v) {

        TermConditions frgment;
        Bundle bundle;
        switch (v.getId()) {

            case R.id.about_us_text:
                frgment = new TermConditions();
                bundle = new Bundle();
                bundle.putString("from", "http://curahapp.com");
                // bundle.putString("from", Const.SERVER_PAGE_URL + "pages/About-Us");
                bundle.putString("title", "About Us");
                frgment.setArguments(bundle);
                replaceFragment(R.id.fl_container_home, frgment, "", bundle);
                break;

            case R.id.terms_condition:
                frgment = new TermConditions();
                bundle = new Bundle();
                bundle.putString("from", Const.SERVER_PAGE_URL + "pages/Terms-Conditions");
                bundle.putString("title", "Terms & Conditions");
                frgment.setArguments(bundle);
                replaceFragment(R.id.fl_container_home, frgment, "", bundle);
                break;

            case R.id.privacyPolicy:
                frgment = new TermConditions();
                bundle = new Bundle();
                bundle.putString("from", Const.SERVER_PAGE_URL + "pages/Privacy-Policy");
                bundle.putString("title", "Privacy Policy");
                frgment.setArguments(bundle);
                replaceFragment(R.id.fl_container_home, frgment, "", bundle);

                break;

            case R.id.contact_us:
                replaceFragment(R.id.fl_container_home, new ContactUs(), "", null);
                break;
            case R.id.rateApp:
                launchMarket();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBar();
    }
    private void launchMarket(){
        final String appPackageName = getActivity().getPackageName(); // package name of the app
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.settings), R.mipmap.ic_menu, true);
    }

    @OnClick(R.id.logout)
    void logout() {
        baseActivity.AlertYesNo("Are you sure you want to logout.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseActivity.getLogoutApi();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}


