package com.curahservice.netset.module;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.IndexActivity;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ForgotPassFragment extends BaseFragment {
    Call<JsonObject> forgotPass;
    @BindView(R.id.email_forgot)
    AppCompatEditText email_forgot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgotpass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.submit_Btn)
    void forgotAlert() {

        if (email_forgot.getText().toString().trim().length() == 0) {
            email_forgot.setError("Please enter email address");
            email_forgot.requestFocus();
        } else {
            forgotEmailAddress(email_forgot.getText().toString().trim());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle();
    }

    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundColor(Color.TRANSPARENT);
            myactivity.setTitle("Forgot Password?", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            }, 0);
        }
    }


    private void forgotEmailAddress(String email) {
        forgotPass = apiInterface.forgotPass(email.toLowerCase());
        apiHitAndHandle.makeApiCall(forgotPass, this);

    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == forgotPass && isAdded()) {
            Log.e("--------", object.toString());
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt("status") == 200) {
                    final Dialog dialog = baseActivity.showCustomDialog(getActivity(), R.layout.alert_forgot, "");
                    Button done_Btn = dialog.findViewById(R.id.done_Btn);
                    done_Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().popBackStack();
                            dialog.dismiss();
                        }
                    });
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
}
