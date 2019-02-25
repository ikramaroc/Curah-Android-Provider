package com.curahservice.netset.retrofitManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.curahservice.netset.BuildConfig;
import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseActivity;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.PrefStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Neeraj Narwal on 31/5/17.
 */
public class ApiHitAndHandle implements Callback, networkCheck {

    static final String TAG = ApiHitAndHandle.class.getSimpleName();
    private static ApiHitAndHandle apiHitAndHandle;
    private static Context mContext;

    public ProgressDialog dialog;

    private HashMap<Call, ApiResponse> apiResponseHashMap = new HashMap<>();

    public static ApiHitAndHandle getInstance(Context context) {
        if (apiHitAndHandle == null) {
            apiHitAndHandle = new ApiHitAndHandle();
        }
        mContext = context;
        return apiHitAndHandle;
    }


    public void makeApiCall(Call call, ApiResponse apiResponse) {
        makeApiCall(call, true, apiResponse);

    }


    public void makeApiCall(Call call, boolean showProgress, ApiResponse apiResponse) {
        try {
            if (!NetworkUtil.getConnectivityStatusBoolean(mContext)) {
                apiResponseHashMap.put(call, apiResponse);
                call.enqueue(this);

                if (showProgress) {
                    setProgressDialog(mContext);

                }
                //Logs post URL

                log(call.request().url() + "");
            }else {
                try {
                    BaseActivity activity = (BaseActivity) mContext;
                    if (activity != null && !activity.isFinishing()) {
                        Toast.makeText(mContext,"Please connect to internet",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("e", e.getLocalizedMessage());
        }
    }

    private void log(String s) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, s);
    }

    private String bodyToString(final RequestBody request) {
        try {
            Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    AlertDialog alertDialog;

    public void showMessage(Context context, String buttonText, String message, DialogInterface.OnClickListener onClickListener) {

        try {
            if (!((Activity) context).isFinishing() || !((AppCompatActivity) context).isFinishing() || !((FragmentActivity) context).isFinishing()) {
                //show dialog

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog = null;
                }
                try {
                    if (alertDialog == null) {
                        alertDialog = new AlertDialog.Builder(
                                context, R.style.DialogTheme).create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setMessage(message);
                        alertDialog.setCancelable(false);
                        if (buttonText.length() > 0) {
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonText, onClickListener);
                        } else {
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", onClickListener);
                        }
                        alertDialog.show();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                alertDialog=null;
                            }
                        });
                    } else {
                        alertDialog.dismiss();
                        alertDialog=null;
                        alertDialog = new AlertDialog.Builder(
                                context, R.style.DialogTheme).create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setMessage(message);
                        alertDialog.setCancelable(false);

                        if (buttonText.length() > 0) {
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonText, onClickListener);
                        } else {
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", onClickListener);
                        }
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.body() != null && isJSONValid(response.body().toString())) {
            ApiResponse apiResponse = apiResponseHashMap.get(call);
            try {
                JSONObject object = new JSONObject(response.body().toString());
                cancelDialog();
                if (object.optString("status").equals("401")) {
                    showMessage(mContext, "", object.optString("message"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            PrefStore store = new PrefStore(mContext);
                            store.cleanPref();
                            ((Activity) mContext).finish();
                            mContext.startActivity(new Intent(mContext, IndexActivity.class)
                                    .putExtra("from", "logout")
                                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                } else if (object.optString("status").equals("405")) {
                    showMessage(mContext, "", object.optString("message"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    });
                } else {
                    cancelDialog();
                    Log.i("result------", response.body() + "");
                    apiResponse.onSuccess(call, response.body());
                }
            } catch (JSONException e) {
                cancelDialog();
                Toast.makeText(mContext, "Something went wrong Please Try again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            apiResponseHashMap.remove(call);
        } else {
            cancelDialog();
            showMessage(mContext, "", "Some thing went wrong on server side. Please try later.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }





    @Override
    public void onFailure(final Call call, Throwable t) {



        final ApiResponse apiResponse = apiResponseHashMap.get(call);
        try {
            cancelDialog();
            if (t.getLocalizedMessage().contains("connect timed out")|| t.getLocalizedMessage().contains("timeout")) {
                showMessage(mContext, "Retry", "Please  check your internet connection, Try Again.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apiResponseHashMap.remove(call);
                        if (call.isExecuted()) {
                            call.cancel();
                        }
                        makeApiCall(call, true, apiResponse);
                    }
                });
            }else if (t.getLocalizedMessage().contains("Failed to connect to")){
                showMessage(mContext, "Retry", "You are on local server, Try to connect with local internet and Try again.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apiResponseHashMap.remove(call);
                        if (call.isExecuted()) {
                            call.cancel();
                        }
                        makeApiCall(call, true, apiResponse);
                    }
                });
            }else {
                try {
                    cancelDialog();
                    apiResponse.onError(call, t.getMessage(), apiResponse);
                    apiResponseHashMap.remove(call);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProgressDialog(Context context) {
        BaseActivity activity = (BaseActivity) mContext;
        try {
            if (activity != null && !activity.isFinishing()) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog = new ProgressDialog(context,R.style.DialogTheme);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                try{
                    dialog.show();
                }catch (Exception e){

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelDialog() {
        try {
            BaseActivity activity = (BaseActivity) mContext;
            if (activity != null && !activity.isFinishing()) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void internetStatus(boolean connection) {
        if (connection) {

        }else {
            try {
                BaseActivity activity = (BaseActivity) mContext;
                if (activity != null && !activity.isFinishing()) {
                    Toast.makeText(mContext,"Please connect to internet",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
