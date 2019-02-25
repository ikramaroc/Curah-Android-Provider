package com.curahservice.netset.module;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import retrofit2.Call;

public class ChangePassord extends BaseFragment implements ApiResponse {

    AppCompatEditText oldPass, newPass, confirm_pass;
    Button submit_button;
    Call<JsonObject> changePassApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_pass, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiate(view);
    }

    void intiate(View view){
        oldPass=view.findViewById(R.id.oldPass);
        newPass=view.findViewById(R.id.newPass);
        submit_button=view.findViewById(R.id.submit_button);
        confirm_pass=view.findViewById(R.id.confirm_pass);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        setToolBar("Change Password");
    }



    private void checkValidation() {
        if (oldPass.getText().toString().trim().length() == 0) {
            oldPass.setError("Please enter old password");
            oldPass.requestFocus();
        } else if (newPass.getText().toString().trim().length() == 0) {
            newPass.setError("Please enter new password");
            newPass.requestFocus();
        } else if (newPass.getText().toString().length() < 8) {
            newPass.setError("Password should be at-least 8 characters long.");
            newPass.requestFocus();
        }else if (oldPass.getText().toString().equals(newPass.getText().toString())) {
            newPass.setError("New password does not match to old password");
            newPass.requestFocus();
        } else if (!newPass.getText().toString().trim().equals(confirm_pass.getText().toString().trim())) {
            confirm_pass.setError("Password does not match");
            confirm_pass.requestFocus();
        } else {
            ChangePassApi(true, oldPass.getText().toString().trim(), newPass.getText().toString().trim());
        }
    }

    private void ChangePassApi(boolean loader,String old,String newPass) {
        changePassApi = apiInterfaceAuth.changePasswordApi(String.valueOf(store.getInt("userId")),old, newPass);
        apiHitAndHandle.makeApiCall(changePassApi, loader,this);

    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call==changePassApi){
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt("status") == 200) {
                    Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStack();
                } else {
                    showMessage(getActivity(), "", result.getString("message"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            oldPass.setText("");
                            newPass.setText("");
                            confirm_pass.setText("");
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setToolBar(String title) {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(title, R.mipmap.ic_back, false);
    }
}
