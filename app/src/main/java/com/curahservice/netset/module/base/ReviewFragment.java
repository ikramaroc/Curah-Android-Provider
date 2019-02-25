package com.curahservice.netset.module.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ReviewModel;
import com.curahservice.netset.module.ReviewAdapter;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

public class ReviewFragment extends BaseFragment {

    RecyclerView recycle_item;
    ArrayList<String> reviewList;
    public ReviewModel reviewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycle_item = view.findViewById(R.id.recycle_item);
        reviewModel = (ReviewModel) getArguments().getSerializable("review");
        setToolBar();
        setData();
    }


    void setData() {
        ReviewAdapter adapter = new ReviewAdapter(getActivity(), reviewModel.getMyReviews(), ReviewFragment.this);
        LinearLayoutManager grid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycle_item.getContext(), grid.VERTICAL);
        recycle_item.setLayoutManager(grid);
        recycle_item.setAdapter(adapter);
        recycle_item.addItemDecoration(dividerItemDecoration);
        recycle_item.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_LONG).show();
                        // replaceFragment(R.id.fl_container_home, new BarberProfileFragment(), "");
                    }
                })
        );
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.pink));

        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("Reviews", R.mipmap.ic_back, false);
    }


}
