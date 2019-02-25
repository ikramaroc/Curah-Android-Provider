package com.curahservice.netset.module;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.model.ZipPlaceName;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.module.myProfile_tabs.BankInfoTab2;
import com.curahservice.netset.util.Const;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddBankDetailFragment extends BaseFragment {


    Call<JsonObject> addCardApi;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.loader)
    ProgressBar loader;
    boolean loadingFinished = true;
    boolean redirect = false;
    @BindView(R.id.scroll)
    ScrollView scroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_bank_detail, container, false);
    }


    private void setToolBar(String name) {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(name, R.mipmap.ic_back, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setSaveFormData(false);
        addbank();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getTag().equals("profile")) {
            setToolBar("Add Bank");
        } else {
            setToolbarTitle();

        }
    }


    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == addCardApi) {
           try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt("status") == 200) {
                    clearCache();
                    loader.setVisibility(View.VISIBLE);
                    if (result.has("bankDetails")) {
                        loadingFinished = true;
                        redirect = false;
                        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.reload();
                        webview.loadUrl(result.getJSONObject("bankDetails").getString("dashboard_link"));
                        webview.setWebViewClient(new WebClient());
                        webview.invalidate();
                        scroll.fullScroll(ScrollView.FOCUS_UP);

                    } else {
                        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        loadingFinished = true;
                        redirect = false;
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.loadUrl(result.getString("url"));
                        webview.setWebViewClient(new WebClient());
                        webview.invalidate();
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
        }
    }



    public static void clearCache() {
        new WebView(getApplicationContext()).clearCache(true);
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

            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
                loader.setVisibility(View.INVISIBLE);
            } else {
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
                addbank();
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


    private void addbank() {
        Log.e("token???????", store.getString("token"));
        addCardApi = apiInterfaceAuth.addBankApi(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(addCardApi, this);
    }


    private void setToolbarTitle() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));

        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundResource(R.color.colorPrimary);
            myactivity.setTitle(getString(R.string.add_bank_detail), false, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();

                }
            }, 0);
            myactivity.skipText(View.VISIBLE, "Skip", new View.OnClickListener() {
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
