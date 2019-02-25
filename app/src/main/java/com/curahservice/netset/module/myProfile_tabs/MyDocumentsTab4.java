package com.curahservice.netset.module.myProfile_tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.base.BaseFragment;

import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDocumentsTab4 extends BaseFragment {

    private RecyclerView recyclerView;
    private List<String> name= new ArrayList<>();
    private List<String> image= new ArrayList<>();
    private Button editDoc_TV;
    createAccount.UserInfo userInfo;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     if (view==null){
         view=inflater.inflate(R.layout.service_recycle, container, false);
         recyclerView = view.findViewById(R.id.recycle_item);
         userInfo = store.getUserPojo(Const.USERDATA);
         image.add(userInfo.getImgUrl()+userInfo.getLicense());
         image.add(userInfo.getImgUrl()+userInfo.getIdentification_card());
         name.add("Cosmetology License");
         name.add("Identity Card");
         editDoc_TV=view.findViewById(R.id.editDoc_TV);
         editDoc_TV.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 replaceFragment(R.id.fl_container_home, new EditDocumentFragment(), "EditDocumentFragment", null);

             }
         });
         setAdapter();
     }
        return view;
    }



    void setAdapter() {
        MyDocumentsRecyclerAdapter mAdapter = new MyDocumentsRecyclerAdapter(getActivity(), name,image, this,false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
