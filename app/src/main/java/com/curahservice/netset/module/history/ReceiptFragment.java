package com.curahservice.netset.module.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppointmentDetail;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiptFragment extends BaseFragment {


    private AppointmentDetail detail;
    @BindView(R.id.servicesList)
    RecyclerView servicesList;

    @BindView(R.id.totalDollar_TV)
    TextView totalDollar_TV;

    @BindView(R.id.taxprice)
    TextView taxprice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setToolBar();
        if (getArguments().getSerializable("service") != null) {
            detail = (AppointmentDetail) getArguments().getSerializable("service");
            addData();

        }
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.receipt), R.mipmap.ic_back, false);
    }


    void addData() {
        Double balance=Double.valueOf(detail.getPrice())-Double.valueOf(detail.getGet_tax_price());
        taxprice.setText(Const.CURRENCY+String.format("%.2f",balance)+"");
        totalDollar_TV.setText(Const.CURRENCY+detail.getGet_tax_price());
        RecieptAdapter adapter = new RecieptAdapter(getActivity(), detail.getServiceName(), ReceiptFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        servicesList.setLayoutManager(linearLayoutManager);
        servicesList.setAdapter(adapter);


    }

}
