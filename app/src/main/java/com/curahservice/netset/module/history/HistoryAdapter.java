package com.curahservice.netset.module.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.HistoryModel;
import com.curahservice.netset.util.Const;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context context;
    private List<HistoryModel.History> historyList;
    private HistoryFragment fragment;

    HistoryAdapter(Context context, List<com.curahservice.netset.gsonModel.HistoryModel.History> menuModelList, HistoryFragment historyFragment) {
        this.context = context;
        this.historyList = menuModelList;
        fragment = historyFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!dateNotRepeat(position)) {
            holder.dateLl.setVisibility(View.GONE);
        } else {
            holder.dateLl.setVisibility(View.VISIBLE);
        }
        holder.dateTv.setText(fragment.changeDateFormat(historyList.get(position).getDate() ,"yyyy-MM-dd","dd MMM, yyyy"));
        holder.servicePrice.setText("Service Cost: "+ Const.CURRENCY+historyList.get(position).getPrice());
        holder.serviceSchedueTime.setText(fragment.changeDateFormat(historyList.get(position).getStartTime(),"HH:mm:ss","hh:mm a")+" to "+fragment.changeDateFormat(historyList.get(position).getCloseTime(),"HH:mm:ss","hh:mm a"));
        holder.serviceName.setText(historyList.get(position).getServiceName());

        if (historyList.get(position).getRate() != null) {
            holder.rating_RB.setRating(Float.valueOf(historyList.get(position).getRate()));
        } else {
            holder.rating_RB.setRating(0);
        }

        Glide.with(context)
                .load(fragment.history.getImgUrl()+historyList.get(position).getImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(holder.serviceProviderImage);
        holder.clientComment.setText(historyList.get(position).getWorkDescription());
        holder.providerName.setText(historyList.get(position).getFirstname()+" "+historyList.get(position).getLastname());


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout dateLl;
        private TextView dateTv;
        private TextView serviceName;
        private TextView servicePrice;
        private TextView serviceSchedueTime;
        private CardView userCardImage;
        private ImageView serviceProviderImage;
        private RelativeLayout nameRl;
        private TextView providerName;
        private TextView clientComment;
        RatingBar rating_RB;
        private View view;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            dateLl = (LinearLayout) view.findViewById(R.id.date_ll);
            dateTv = (TextView) view.findViewById(R.id.date_tv);
            serviceName = view.findViewById(R.id.service_name);
            servicePrice = view.findViewById(R.id.service_price);
            rating_RB=view.findViewById(R.id.rating_RB);
            serviceSchedueTime = (TextView) view.findViewById(R.id.service_schedue_time);
            userCardImage = (CardView) view.findViewById(R.id.userCardImage);
            serviceProviderImage = (ImageView) view.findViewById(R.id.service_provider_image);
            nameRl = view.findViewById(R.id.name_rl);
            providerName = (TextView) view.findViewById(R.id.providerName);
            clientComment = (TextView) view.findViewById(R.id.client_comment);
            view = (View) view.findViewById(R.id.view);

        }



        @OnClick(R.id.rating_RB)
        void ratingBar() {

        }
    }

    private boolean dateNotRepeat(int position) {
        String datePrev = "";
        String date = historyList.get(position).getDate();

        if (position > 0) {
            datePrev = historyList.get(position-1).getDate();
        }

        if (position != 0 && datePrev.equals(date)) {
            return false;
        } else {
            return true;
        }

    }
}

