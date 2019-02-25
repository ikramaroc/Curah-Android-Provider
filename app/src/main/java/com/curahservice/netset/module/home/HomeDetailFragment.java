package com.curahservice.netset.module.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDetailFragment extends BaseFragment {

    @BindView(R.id.accept_Btn)
    Button accept_Btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        accept_Btn.setVisibility(View.VISIBLE);
        setToolBar();
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(),getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity)getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarAttributes(getString(R.string.details), R.mipmap.ic_back, false);
    }
}
