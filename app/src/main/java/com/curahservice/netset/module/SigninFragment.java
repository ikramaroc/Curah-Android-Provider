package com.curahservice.netset.module;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.curahservice.netset.MyFirebaseInstanceIDService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.addServices.AddServicesFragment;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;

public class SigninFragment extends BaseFragment implements View.OnClickListener {
    private TextView forgotpass_signin;
    private TextView signup;
    private Button login_button;
    TextInputLayout pass_TL,email_TL;
    AppCompatEditText email,password;
    Call<JsonObject> login;
    boolean once=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideActionBar();
        return inflater.inflate(R.layout.singin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgotpass_signin = view.findViewById(R.id.forgotpass_signin);
        email=view.findViewById(R.id.email_signin);
        password=view.findViewById(R.id.pass_signin);
        signup = view.findViewById(R.id.signup);
        signup.setText(Html.fromHtml("<u>SIGN UP</u>"));
        login_button=view.findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpass_signin.setOnClickListener(this);
        login_button.setOnClickListener(this);

        email_TL=view.findViewById(R.id.email_TL);
        pass_TL=view.findViewById(R.id.pass_TL);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.aeh_regular);
        pass_TL.setTypeface(typeface);
        email_TL.setTypeface(typeface);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle();
        if (getTag().equals("AdminWait") && !once){
            once=true;
            pendingAdminApproval();
        }
    }



    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.INVISIBLE);
            myactivity.skipText(View.INVISIBLE,"",null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                checkLocationPermissionLogin(false);
                break;
            case R.id.forgotpass_signin:
                replaceFragment(R.id.container, new ForgotPassFragment(), "forgot", null);
                break;
            case R.id.login_button:
                checkLocationPermissionLogin(true);

                break;
        }
    }


    private void checkValidation(){
        if (email.getText().toString().trim().length()==0){
            email.setError("Please enter your email address");
            email.requestFocus();
        }else if (password.getText().toString().trim().length()==0){
            password.requestFocus();
            password.setError("Please enter your password");
        }else {
            loginApp(email.getText().toString().trim(),password.getText().toString().trim());
        }
    }



    private void checkLocationPermissionLogin(final boolean login) {
        String[] arrayPermission = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE
        };
        if (checkPermissions(arrayPermission, 120, new PermCallback() {
            @Override
            public void permGranted(int resultCode) {
                if (login) {
                    checkValidation();
                }else {
                    replaceFragment(R.id.container, new SignupFragment(), "signup", null);
                }
            }

            @Override
            public void permDenied(int resultCode) {

            }
        })) {
            if (login) {
                checkValidation();
            }else {
                replaceFragment(R.id.container, new SignupFragment(), "signup", null);
            }
        }
    }




    private void loginApp(String email, String password) {
        if (FirebaseInstanceId.getInstance().getToken()!=null) {
            login = apiInterface.login(email.toLowerCase(), password, FirebaseInstanceId.getInstance().getToken(), Const.DEVICE_TYPE, Const.USER_TYPE, store.getString(getString(R.string.latitude)), store.getString(getString(R.string.longitude)));
            apiHitAndHandle.makeApiCall(login, this);
        }else {
            MyFirebaseInstanceIDService myFireBaseInstanceId=new MyFirebaseInstanceIDService();
            myFireBaseInstanceId.onTokenRefresh();
        }


    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);

        if (call == login) {
            Gson gson = new Gson();
            createAccount userProfile = gson.fromJson(object.toString(), createAccount.class);
            if (userProfile.getStatus() == 200) {
                store.saveString("token", userProfile.getUserInfo().getToken());
                store.setInt(Const.USER_ID, userProfile.getUserInfo().getId());
                if (userProfile.getUserInfo().getFirstname().length() > 0) {
                    store.saveUserPojo(Const.USERDATA, userProfile.getUserInfo());
                    if (userProfile.getServices()==null){
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        replaceFragment(R.id.container, new AddServicesFragment(), "AddServicesFragment", null);
                    }
                    else if (userProfile.getServices().size()==0){
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        replaceFragment(R.id.container, new AddServicesFragment(), "AddServicesFragment", null);
                    }else {
                        store.setBoolean(Const.LOGIN,true);
                        store.setBoolean(Const.SOCIAL_LOGIN, false);
                        store.setSerVices(Const.SERVICES, userProfile.getServices());
                        store.saveUserPojo(Const.USERDATA, userProfile.getUserInfo());
                        baseActivity.moveToNextActivity(DrawerActivity.class, null);
                    }

                } else {
                    Fragment create = new CreateProfileFragment();
                    Bundle b = new Bundle();
                    b.putString("from", "Create");
                    create.setArguments(b);
                    replaceFragmentNull(R.id.container, create, "profile");
                }
            }else if (userProfile.getStatus()==406){
                pendingAdminApproval();
            }else {
                showMessage(getActivity(), "", userProfile.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

        }

    }






}
