package com.curahservice.netset.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ReviewModel;
import com.curahservice.netset.module.base.ReviewFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private Context context;
    private List<ReviewModel.MyReview> myReviewList;
    ReviewFragment fragment;


    public ReviewAdapter(Context context, List<ReviewModel.MyReview> menuModelList, ReviewFragment fragment) {
        this.context = context;
        this.myReviewList = menuModelList;
        this.fragment=fragment;

    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        holder.providerName.setText(myReviewList.get(position).getFirstname() + " " + myReviewList.get(position).getLastname());
        holder.userComment.setText(myReviewList.get(position).getDescription());
        if (myReviewList.get(position).getRating() != null) {
            holder.ratingBar.setRating(Float.valueOf(myReviewList.get(position).getRating()));
        } else {
            holder.ratingBar.setRating(0);
        }
        Glide.with(context)
                .load(fragment.reviewModel.getImgUrl()+myReviewList.get(position).getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(holder.userdp);
    }

    @Override
    public int getItemCount() {
        return myReviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView providerName;
        RatingBar ratingBar;
        CircleImageView userdp;
        TextView userComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            providerName = itemView.findViewById(R.id.userName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userComment = itemView.findViewById(R.id.userComment);
            userdp=itemView.findViewById(R.id.userdp);

        }
    }
}