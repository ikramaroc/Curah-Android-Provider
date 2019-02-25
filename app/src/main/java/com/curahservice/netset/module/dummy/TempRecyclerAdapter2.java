package com.curahservice.netset.module.dummy;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseActivity;
import com.curahservice.netset.util.AppConstants;

import java.util.List;

public class TempRecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

    private Activity context;
    private BaseActivity activity;
    private List<TempGetSet> dataList;

    TempRecyclerAdapter2(Activity context, List<TempGetSet> dataList) {
        this.context = context;
        this.activity= (BaseActivity) context;
        this.dataList = dataList;
    }

    public void setListData(List<TempGetSet> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class ViewHolder_header extends RecyclerView.ViewHolder {
        //private RecyclerView recyclerView;
        private TextView TV4;

        ViewHolder_header(View view) {
            super(view);
            //recyclerView = view.findViewById(R.id.recycler_frames);
            TV4=view.findViewById(R.id.TV4);

        }
    }

    class ViewHolder_iteams extends RecyclerView.ViewHolder {
        private ImageView ad_img;

        ViewHolder_iteams(View view) {
            super(view);
            ad_img=view.findViewById(R.id.ad_img);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case header:
                View v1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.temp_adapter3, parent, false);
                viewHolder = new ViewHolder_header(v1);
                break;
            case items:
                View v2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.temp_adapter2, parent, false);
                viewHolder = new ViewHolder_iteams(v2);
                break;
            default:
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case header:
                ViewHolder_header holder1 = (ViewHolder_header) holder;
                holder1.TV4.setText(dataList.get(position).getMessage());

                break;
            case items:
                ViewHolder_iteams holder2 = (ViewHolder_iteams) holder;
                holder2.ad_img.setImageResource(dataList.get(position).getImage());

                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("TAG345", "getItemViewType");
        if (dataList.get(position).getTag() == header) {

            return header;
        } else {
            return items;
        }
        //return -1;
    }
}