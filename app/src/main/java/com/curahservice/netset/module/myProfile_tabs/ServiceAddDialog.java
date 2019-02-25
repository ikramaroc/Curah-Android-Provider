package com.curahservice.netset.module.myProfile_tabs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.curahservice.netset.R;

public class ServiceAddDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button  no;
    public TextView yes;
    public RecyclerView suggestion_services_RV;

    public ServiceAddDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_service_dialog);
        suggestion_services_RV=findViewById(R.id.suggestion_services_RV);
        yes = (TextView) findViewById(R.id.submit_TV);
        no = (Button) findViewById(R.id.cancel_Btn);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_TV:
                c.finish();
                break;
            case R.id.cancel_Btn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
