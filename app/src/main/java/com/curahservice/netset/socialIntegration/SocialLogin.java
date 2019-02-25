package com.curahservice.netset.socialIntegration;

import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;


public class SocialLogin {

    private SocialLoginListener listener;
    private static final SocialLogin instance = new SocialLogin();


    public void setListener(SocialLoginListener listener) {
        this.listener = listener;
    }

    public static SocialLogin getInstance() {
        return instance;
    }

    public void facebookLoginDone(AccessToken accessToken,String token, JSONObject object, GraphResponse response) {
        if (listener != null)
            listener.onFacebookLoginDone(accessToken,token,object, response);
    }

    public void gPlusLoginDone(GoogleSignInAccount acct, Task<GoogleSignInAccount> currentPerson, String idToken) {
        if (listener != null)
            listener.onGPlusLoginDone(acct,currentPerson,idToken);
    }

    public interface SocialLoginListener {
        void onFacebookLoginDone(AccessToken accessToken, String token, JSONObject object, GraphResponse response);

        void onGPlusLoginDone(GoogleSignInAccount acct, Task<GoogleSignInAccount> currentPerson, String idToken);
    }
}
