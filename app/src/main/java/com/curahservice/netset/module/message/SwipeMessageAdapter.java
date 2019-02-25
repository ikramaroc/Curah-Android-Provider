package com.curahservice.netset.module.message;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import com.curahservice.netset.R;
import com.curahservice.netset.module.chat.ChatFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeMessageAdapter extends RecyclerView.Adapter {
    private List<MessageConnection.Connection> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private MessageFragment messageFragment;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();


    public SwipeMessageAdapter(Context context, List<MessageConnection.Connection> dataSet,MessageFragment messageFragment) {
        mContext = context;
        mDataSet = dataSet;
        this.messageFragment=messageFragment;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.swipe_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final MessageConnection.Connection data = mDataSet.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, position+"");

            // Bind your data here
            holder.bind(data,position);


            if (position==(mDataSet.size()-1)) {
                holder.view.setVisibility(View.GONE);
            } else {
                holder.view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;
        private View deleteLayout;
        private View view;
        CircleImageView userImage;
        private TextView textView,userComment,messageTime;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            textView = (TextView) itemView.findViewById(R.id.userName);
            userComment=itemView.findViewById(R.id.userComment);
            messageTime=itemView.findViewById(R.id.messageTime);
            userImage=itemView.findViewById(R.id.userdp);
            view=itemView.findViewById(R.id.view);
        }

        public void bind(final MessageConnection.Connection data,int position) {

            deleteLayout.setTag(position);
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                   messageFragment.baseActivity.AlertYesNo("Are you sure to delete this conversation", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           messageFragment.deleteMessage(mDataSet.get((Integer) v.getTag()).getConnectionId());
                       }
                   }, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   });




                }
            });

            messageTime.setText(messageFragment.converStringToDate(data.getTime()));

            if (data.getLastmessage()!=null && data.getLastmessage().length()>0){
                userComment.setText(data.getLastmessage());
            }else {
                userComment.setText("Image");
            }

            Glide.with(mContext)
                    .load(messageFragment.messageConnection.getImgUrl()+ data.getUserImage())
                    .apply(new RequestOptions()
                            .dontAnimate().
                                    error(R.mipmap.user_img))
                    .into(userImage);
            textView.setText(data.getUserName());
            frontLayout.setTag(position);
            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ChatFragment fragmentToOpen = new ChatFragment();
                    Bundle data=new Bundle();
                    data.putString("id",mDataSet.get((Integer) view.getTag()).getUserId()+"");
                    data.putString("connectionId",mDataSet.get((Integer) view.getTag()).getConnectionId()+"");
                    fragmentToOpen.setArguments(data);
                    messageFragment.replaceFragment(R.id.fl_container_home, fragmentToOpen, "",data);
                }
            });
        }
    }
}

