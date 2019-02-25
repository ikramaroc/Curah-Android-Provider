package com.curahservice.netset.module.myProfile_tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.BankGson;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.AddBankDetailFragment;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;


@SuppressLint("ValidFragment")
public class BankInfoTab2 extends BaseFragment {

    Call<JsonObject> bankInfo, addCardApi;
    //TextView name;
    //TextView bankname;
    //TextView accNumber;
    //TextView routingNumber;
    Button addbank;
    RelativeLayout bankInfo_RL;
    ProgressBar progress;
    WebView webview;

    boolean loadingFinished = true;
    boolean redirect = false;
    BarberProfileFragment barberProfileFragment;


    @SuppressLint("ValidFragment")
    BankInfoTab2(BarberProfileFragment fragment){
        this.barberProfileFragment=fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bank_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webview = view.findViewById(R.id.webview);
        progress=view.findViewById(R.id.progress);
        addbank = view.findViewById(R.id.addBank);
        bankInfo_RL = view.findViewById(R.id.bankInfo_RL);
        addbank();
    }


    private void addbank() {
        Log.e("token???????", store.getString("token"));
        addCardApi = apiInterfaceAuth.addBankApi(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(addCardApi, this);
    }


    // {"bankDetails":{"code":"ac_DiVTwr4ffte9oM5FWjg5APAdksoTdDti","user_id":37},"status":200}
    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == addCardApi) {
//{"status":200,"url":"https://connect.stripe.com/express/oauth/authorize?redirect_uri=http://192.168.2.252/curah/webservice/addBank&client_id=ca_BCHvGTfVQIbui6XnVa7lpWgAXDrfxiY6&state=16"}
            //{"status":"false","message":"Individual phone is invalid.\nIndividual postal code is required.\nIndividual locality is required.\nIndividual region is required.\nFunding account number is required."}
            try {
                JSONObject result = new JSONObject(object.toString());
                JSONObject bankDetails;
                if (result.getInt("status") == 200) {
                    if (result.has("bankDetails")) {
                        bankInfo_RL.setVisibility(View.VISIBLE);
                        addbank.setVisibility(View.GONE);
                        bankDetails = result.getJSONObject("bankDetails");
                        webview.getSettings().setJavaScriptEnabled(true);
                       progress.setVisibility(View.VISIBLE);
                        webview.loadUrl(bankDetails.getString("dashboard_link"));
                        webview.setWebViewClient(new WebClient());

                    } else {
                        bankInfo_RL.setVisibility(View.GONE);
                        addbank.setVisibility(View.VISIBLE);
                        bankInfo_RL.setVisibility(View.INVISIBLE);
                        addbank.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                barberProfileFragment.isVisibleTool = false;
                                replaceFragment(R.id.fl_container_home, new AddBankDetailFragment(), "profile", null);
                            }
                        });
                    }


                } else {
                    showMessage(getActivity(), "", result.getString("message"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            Log.e("-------------", object.toString());
        }
    }


    private class WebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingFinished = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){
                //HIDE LOADING IT HAS FINISHED
                progress.setVisibility(View.INVISIBLE);
            } else{
                redirect = false;
            }
            if (url.contains("webservice/addBankDetails?code=")) {
                Activity activity = getActivity();
                if (activity instanceof IndexActivity) {
                    IndexActivity myactivity = (IndexActivity) activity;
                    myactivity.skipText(View.VISIBLE, "Next", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("AdminWait", "wait");
                            baseActivity.moveToNextActivity(IndexActivity.class, bundle);

                        }
                    });
                }
            }
        }


        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            return false;
        }
    }

}
