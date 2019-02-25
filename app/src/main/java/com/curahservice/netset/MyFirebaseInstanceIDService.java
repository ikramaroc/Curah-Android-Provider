package com.curahservice.netset;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by netset on 12/8/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: --------------------");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        //Log.e("token-------------",token);

       // Preferences.setFcmToken(getApplicationContext(), token);
    }
    // AIzaSyD4msmqPA3fjNTk0Ihu-NssXXzfuLezC50    server key

    //fcm token//c6Wjb1Tjlt4:APA91bGy5XKieOQcec166LAbNMSSZhms00-mA76JHAW9ZqfzCJVE5tfrXiJlGybE6Tuytx0-oYVPu7DXXHVKPAYhak77BZUqVVmshFuRYlVCxIlfwaCznVk0Cn7srq7LdR0hy5K5P6dC

}
