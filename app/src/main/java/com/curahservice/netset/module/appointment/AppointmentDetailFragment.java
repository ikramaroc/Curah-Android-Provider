package com.curahservice.netset.module.appointment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentDetailFragment extends BaseFragment {

    @BindView(R.id.history_RecyclerView)
    RecyclerView history_RecyclerView;

    @BindView(R.id.buttonOne)
    TextView view_receipt;


    @BindView(R.id.feedBackLayout)
    LinearLayout feedBackLayout;

    @BindView(R.id.buttonTwo)
            Button accept;

    String ctrl_show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle extras = getArguments();
        if (extras != null) {
            ctrl_show = extras.getString("ctrl_show", null);
        }
        return inflater.inflate(R.layout.history_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setToolBar();
        if (ctrl_show.contains("end")) {
            view_receipt.setVisibility(View.VISIBLE);
            feedBackLayout.setVisibility(View.GONE);
            view_receipt.setText(getString(R.string.end_service));
        } else {
            feedBackLayout.setVisibility(View.VISIBLE);
            view_receipt.setVisibility(View.GONE);
        }
        view_receipt.setText("END SERVICE");
    }



    @OnClick(R.id.buttonOne)
    void viewReceipt() {
        if (ctrl_show.contains("end")) {
            final Dialog dialog = baseActivity.showCustomDialog(getActivity(), R.layout.alert_rate_review, "");
            Button submit_Btn = dialog.findViewById(R.id.submit_Btn);
            submit_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            final Dialog dialog = baseActivity.showCustomDialog(getActivity(), R.layout.alert_cancel_appointment, "");
            Button cancel_Btn = dialog.findViewById(R.id.cancel_Btn);
            cancel_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Button submit_Btn = dialog.findViewById(R.id.submit_Btn);
            submit_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        }



    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(),getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity)getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarAttributes(getString(R.string.details), R.mipmap.ic_back, false);
    }


}
