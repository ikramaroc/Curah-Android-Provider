package com.curahservice.netset.module.portfolio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.gsonModel.PortFolioGson;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class PortfolioFragment extends BaseFragment {

    @BindView(R.id.portfolio_RV)
    RecyclerView portfolio_RV;

    @BindView(R.id.nodata)
    TextView nodata;
    private List<Integer> dataList= new ArrayList<>();
    Call<JsonObject> viewPortFolio;
    public PortFolioGson portfolio;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        setToolBar();
        viewPortFolio();
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(),getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity)getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity)getActivity()).setToolbarAttributes(getString(R.string.portfolio), R.mipmap.ic_menu, true);
    }


    void setAdapter() {
        PortfolioRecyclerAdapter mAdapter = new PortfolioRecyclerAdapter(getActivity(), portfolio.getPorfolio(), this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        portfolio_RV.setLayoutManager(mLayoutManager);
        portfolio_RV.setAdapter(mAdapter);
        portfolio_RV.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    @OnClick(R.id.portfolio_FAB)
    void fab() {
        replaceFragment(R.id.fl_container_home, new AddPortfolioFragment(), "AddPortfolioFragment", null);
    }


    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call==viewPortFolio){
            Gson gson = new GsonBuilder().serializeNulls().create();
             portfolio = gson.fromJson(object.toString(), PortFolioGson.class);
            if (portfolio.getStatus()==200){
                if (portfolio.getPorfolio().size()>0){
                    nodata.setVisibility(View.INVISIBLE);
                    setAdapter();
                }else {
                    nodata.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void viewPortFolio() {
        viewPortFolio = apiInterfaceAuth.viewPortfolio(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(viewPortFolio, this);
    }

}
