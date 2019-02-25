package com.curahservice.netset.module.addServices;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;

import java.util.List;

public class SuggestedRecyclerAdapter extends RecyclerView.Adapter<SuggestedRecyclerAdapter.MyViewHolder> {

    private Activity context;
    private List<createAccount.Service> dataList;

    public SuggestedRecyclerAdapter(Activity context, List<createAccount.Service> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestion_name_TV;

        MyViewHolder(View view) {
            super(view);
            suggestion_name_TV = view.findViewById(R.id.suggestion_name_TV);

        }
    }

    @Override
    public SuggestedRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_suggested, parent, false);

        return new SuggestedRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuggestedRecyclerAdapter.MyViewHolder holder, int position) {
        holder.suggestion_name_TV.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}