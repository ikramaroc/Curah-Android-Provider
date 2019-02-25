package com.curahservice.netset.socialIntegration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.curahservice.netset.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.BuildConfig;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;


public class FacebookLogin extends Activity {
    private CallbackManager callbackmanager;
    private GraphRequest request;
    private LoginButton fbLoginButton;
    private SocialLogin socialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.social_facebook_login);
        socialLogin = SocialLogin.getInstance();
        callbackmanager = CallbackManager.Factory.create();
        fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFacebook();
            }
        });
        fbLoginButton.callOnClick();
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    public void initFacebook() {
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().logOut();
        fbLoginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("response-----", response.toString());
                        socialLogin.facebookLoginDone(request.getAccessToken(), request.getAccessToken().getToken(), object, response);
                        disconnectFromFacebook();
                        finish();
                    }
                });
                Bundle parameters = new Bundle();


                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                log("Facebook login cancel");
                disconnectFromFacebook();
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                log("Facebook error :" + error.toString());
                disconnectFromFacebook();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void log(String s) {
        if (BuildConfig.DEBUG) {
            Log.e("Facebook Login : >> ", s);
        }
    }
}
