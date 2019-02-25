package com.curahservice;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import retrofit2.Call;
//http://192.168.2.252/curah/webservice/createProfile
//http://192.168.2.252/curah/webservice/createProfile
public class ContactUs extends BaseFragment implements View.OnClickListener {

    private AppCompatEditText name;
    private AppCompatEditText subject;
    private AppCompatEditText email;
    private AppCompatEditText message;

    Call<JsonObject> contactUs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_us, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (AppCompatEditText) view.findViewById(R.id.name);
        subject = (AppCompatEditText) view.findViewById(R.id.subject);
        email = (AppCompatEditText) view.findViewById(R.id.email);
        message = (AppCompatEditText) view.findViewById(R.id.message);
        view.findViewById(R.id.submit_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_button:
                //TODO implement
                if (name.getText().toString().length() == 0) {
                    name.setError("Please enter name");
                    name.requestFocus();
                } else if (subject.getText().toString().trim().length() == 0) {
                    subject.setError("Please enter subject");
                    subject.requestFocus();
                } else if (email.getText().toString().trim().length() == 0) {
                    email.setError("Please enter email address");
                    email.requestFocus();
                } else if (!isValidMail(email.getText().toString().trim())) {
                    email.setError("Please enter valid email address");
                    email.requestFocus();
                }else if (message.getText().toString().trim().length()==0) {
                    message.setError("Please enter message");
                    message.requestFocus();
                }else {
                    apiContactUs(name.getText().toString().trim(),email.getText().toString(),subject.getText().toString(),message.getText().toString());
                }

                break;
        }
    }


    private void apiContactUs(String name, String email, String subject, String message) {
        contactUs = apiInterfaceAuth.contactUs(String.valueOf(store.getInt("userId")), name, subject, email, message);
        apiHitAndHandle.makeApiCall(contactUs, ContactUs.this);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call==contactUs){
            try{
                JSONObject result=new JSONObject(object.toString());
                if (result.getInt(Const.STATUS)==200){
                    showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            email.setText("");
                            name.setText("");
                            subject.setText("");
                            message.setText("");
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBar();
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.pink));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("Contact Us", R.mipmap.ic_back, false);
    }
}
