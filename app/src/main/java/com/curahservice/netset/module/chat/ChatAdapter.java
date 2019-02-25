package com.curahservice.netset.module.chat;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ConversationModel;
import com.curahservice.netset.util.Const;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<ConversationModel.Conversation> conversationList;
    ChatFragment chatFragment;

    public ChatAdapter(Context context, List<ConversationModel.Conversation> menuModelList, ChatFragment chatFragment) {
        this.context = context;
        this.conversationList = menuModelList;
        this.chatFragment = chatFragment;
        }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder viewHolder, int position) {

        final MyViewHolder holder = (MyViewHolder) viewHolder;
        //Other chat
        if (conversationList.get(position).getUserId() != chatFragment.store.getInt(Const.USER_ID)) {
            holder.otherUserMessageLl.setVisibility(View.VISIBLE);
            holder.mymessageRl.setVisibility(View.GONE);
            holder.timeOthermsg.setText(chatFragment.converStringToDate(conversationList.get(position).getTime()));
            Glide.with(context)
                    .load(chatFragment.conversation.getImgUrl() + conversationList.get(position).getUserImage())
                    .apply(new RequestOptions()
                            .dontAnimate().
                                    error(R.mipmap.user_img))
                    .into(holder.otherUserImage);

            holder.msgOtherName.setText(conversationList.get(position).getUserName());
            if (conversationList.get(position).getImage().length() == 0) {
                holder.otherMessage.setText(conversationList.get(position).getMessage());
                holder.otherMessage.setVisibility(View.VISIBLE);
                holder.otherImageRl.setVisibility(View.GONE);
            } else if (conversationList.get(position).getImage().length() > 0) {
                holder.otherMessage.setVisibility(View.GONE);
                holder.otherImageRl.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(chatFragment.conversation.getUrl() + conversationList.get(position).getImage())
                        .apply(new RequestOptions()
                                .dontAnimate().
                                        error(R.mipmap.ic_camera))
                        .into(holder.otherImagePost);

                holder.otherUserMessageLl.setTag(position);
                holder.otherUserMessageLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = chatFragment.baseActivity.showCustomDialog(context, R.layout.alert_portfolio, "");
                        TextView description = dialog.findViewById(R.id.description);
                        TextView title = dialog.findViewById(R.id.title);
                        ImageView portImage = dialog.findViewById(R.id.portImage);
                        WebView portVideo = dialog.findViewById(R.id.portVideo);
                        ImageView delete_IV = dialog.findViewById(R.id.delete_IV);
                        ImageView edit_IV = dialog.findViewById(R.id.edit_IV);
                        delete_IV.setVisibility(View.INVISIBLE);
                        edit_IV.setVisibility(View.INVISIBLE);
                        description.setText("");
                        title.setText("");
                        TextView close=dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        portVideo.setVisibility(View.GONE);
                        portImage.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(chatFragment.conversation.getUrl() + conversationList.get((Integer) v.getTag()).getImage())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(portImage);
                    }
                });

            }
        } else {
            holder.otherUserMessageLl.setVisibility(View.GONE);
            holder.myMessageLl.setVisibility(View.VISIBLE);
            holder.mymessageRl.setVisibility(View.VISIBLE);
            holder.timeMyMessage.setText(chatFragment.converStringToDate(conversationList.get(position).getTime()));
            Glide.with(context)
                    .load(chatFragment.conversation.getImgUrl() + conversationList.get(position).getUserImage())
                    .apply(new RequestOptions()
                            .dontAnimate().
                                    error(R.mipmap.ic_camera))
                    .into(holder.myImage);

            if (conversationList.get(position).getImage().length() == 0) {
                holder.myMessage.setText(conversationList.get(position).getMessage());
                holder.myImageRl.setVisibility(View.GONE);
                holder.myMessage.setVisibility(View.VISIBLE);
            } else if (conversationList.get(position).getImage().length() > 0) {
                holder.myMessage.setVisibility(View.GONE);
                holder.myImageRl.setVisibility(View.VISIBLE);
                holder.imagePost.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(chatFragment.conversation.getUrl() + conversationList.get(position).getImage())
                        .apply(new RequestOptions()
                                .dontAnimate().
                                        error(R.mipmap.ic_camera))
                        .into(holder.imagePost);
                holder.myMessageLl.setTag(position);
                holder.myMessageLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = chatFragment.baseActivity.showCustomDialog(context, R.layout.alert_portfolio, "");
                        TextView description = dialog.findViewById(R.id.description);
                        TextView title = dialog.findViewById(R.id.title);
                        ImageView delete_IV = dialog.findViewById(R.id.delete_IV);
                        ImageView edit_IV = dialog.findViewById(R.id.edit_IV);
                        delete_IV.setVisibility(View.INVISIBLE);
                        edit_IV.setVisibility(View.INVISIBLE);
                        ImageView portImage = dialog.findViewById(R.id.portImage);
                        WebView portVideo = dialog.findViewById(R.id.portVideo);
                        TextView close=dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        description.setText("");
                        title.setText("");
                        portVideo.setVisibility(View.GONE);
                        portImage.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(chatFragment.conversation.getUrl() + conversationList.get((Integer) v.getTag()).getImage())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(portImage);
                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }


    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout otherUserMessageLl;
        private CircleImageView otherUserImage;
        private LinearLayout otherUserMessagell;
        private RelativeLayout otherImageRl;
        private ImageView otherImagePost;
        private ImageView otherImagePlay;
        private AppCompatTextView otherMessage;
        private TextView timeOthermsg;
        private RelativeLayout mymessageRl;
        private RelativeLayout myMessageLl;
        private RelativeLayout myImageRl;
        private ImageView imagePost;
        private ImageView playImage;
        private AppCompatTextView myMessage;
        private TextView timeMyMessage;
        private CircleImageView myImage;
        private TextView msgOtherName;

        public MyViewHolder(View view) {
            super(view);
            otherUserMessageLl = (RelativeLayout) view.findViewById(R.id.otherUserMessage_ll);
            otherUserImage = (CircleImageView) view.findViewById(R.id.otherUserImage);
            otherUserMessagell = (LinearLayout) view.findViewById(R.id.otherUserMessagell);
            otherImageRl = (RelativeLayout) view.findViewById(R.id.otherImage_rl);
            otherImagePost = (ImageView) view.findViewById(R.id.otherImagePost);
            otherImagePlay = (ImageView) view.findViewById(R.id.otherImagePlay);
            otherMessage = (AppCompatTextView) view.findViewById(R.id.other_message);
            timeOthermsg = (TextView) view.findViewById(R.id.timeOthermsg);
            mymessageRl = view.findViewById(R.id.mymessage_rl);
            myImage = view.findViewById(R.id.myImage);
            myMessageLl = view.findViewById(R.id.myMessage_ll);
            myImageRl = (RelativeLayout) view.findViewById(R.id.image_rl);
            imagePost = (ImageView) view.findViewById(R.id.imagePost);
            playImage = (ImageView) view.findViewById(R.id.playImage);
            msgOtherName = (TextView) view.findViewById(R.id.msgOther);
            myMessage = (AppCompatTextView) view.findViewById(R.id.my_message);
            timeMyMessage = (TextView) view.findViewById(R.id.timeMyMessage);
        }
    }

}


