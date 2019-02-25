package com.curahservice.netset.module.portfolio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.PortFolioGson;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.Const;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;

public class PortfolioRecyclerAdapter extends RecyclerView.Adapter<PortfolioRecyclerAdapter.MyViewHolder> implements ApiResponse {
    private Context context;
    private List<PortFolioGson.Porfolio> dataList;
    private PortfolioFragment fragment;
    Call<JsonObject> deletePortfolio;
    private int intDelete = -1;

    PortfolioRecyclerAdapter(Context context, List<PortFolioGson.Porfolio> dataList, PortfolioFragment fragment) {
        this.context = context;
        this.dataList = dataList;
        this.fragment = fragment;
    }


    private void deletePortfolio(String portfilioId, ApiResponse response) {
        deletePortfolio = fragment.apiInterfaceAuth.deletePortfolio(String.valueOf(fragment.store.getInt(Const.USER_ID)), portfilioId);
        fragment.apiHitAndHandle.makeApiCall(deletePortfolio, response);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_portfolio, parent, false);

        int width = parent.getMeasuredHeight() / 3;
        view.setMinimumWidth(width);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (dataList.get(position).getType().equals("V")) {

            holder.play_IV.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(fragment.portfolio.getUrl() + dataList.get(position).getVideoThumb())
                    .apply(new RequestOptions()
                            .dontAnimate().
                                    error(R.mipmap.user_img))
                    .into(holder.img_or_video_IV);
        } else {
            Glide.with(context)
                    .load(fragment.portfolio.getUrl() + dataList.get(position).getFile())
                    .apply(new RequestOptions()
                            .dontAnimate().
                                    error(R.mipmap.user_img))
                    .into(holder.img_or_video_IV);
        }

        holder.outRelativeLl.setTag(position);
        //fragment.baseActivity.previewImage(dataList.get(position).toString(), holder.img_or_video_IV, "");
        //holder.img_or_video_IV.setImageResource(dataList.get(position).getFile());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onSuccess(Call call, Object object) {
        if (call == deletePortfolio) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    dataList.remove(intDelete);
                    intDelete = -1;
                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_or_video_IV;
        private ImageView play_IV;
        private RelativeLayout outRelativeLl;

        MyViewHolder(View itemView) {
            super(itemView);
            img_or_video_IV = itemView.findViewById(R.id.img_or_video_IV);
            play_IV = itemView.findViewById(R.id.play_IV);
            outRelativeLl = itemView.findViewById(R.id.outRelativeLl);
            outRelativeLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("ctrl_show", "end");
                    //fragment.replaceFragment(R.id.fl_container_home, new AppointmentDetailFragment(), "AppointmentDetailFragment", bundle);
                    final Dialog dialog = fragment.baseActivity.showCustomDialog(context, R.layout.alert_portfolio, "");
                    ImageView delete_IV = dialog.findViewById(R.id.delete_IV);
                    TextView close = dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    ImageView edit_IV = dialog.findViewById(R.id.edit_IV);
                    TextView description = dialog.findViewById(R.id.description);
                    TextView title = dialog.findViewById(R.id.title);
                    ImageView portImage = dialog.findViewById(R.id.portImage);
                    WebView webview = dialog.findViewById(R.id.portVideo);
                    description.setText(dataList.get((Integer) v.getTag()).getDescription());
                    title.setText(dataList.get((Integer) v.getTag()).getTitle());
                    if (dataList.get((Integer) v.getTag()).getType().equals("V")) {
                        webview.setVisibility(View.VISIBLE);
                        webview.setWebViewClient(new WebViewClient());
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
                        webview.setWebChromeClient(new WebChromeClient());
                        webview.loadUrl(fragment.portfolio.getUrl() + dataList.get((Integer) v.getTag()).getFile());
                       /* portVideo.setVideoPath(fragment.portfolio.getUrl() + dataList.get((Integer) v.getTag()).getFile());
                        portVideo.start();*/
                    } else {
                        portImage.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(fragment.portfolio.getUrl() + dataList.get((Integer) v.getTag()).getFile())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(portImage);
                    }
                    delete_IV.setTag(v.getTag());
                    delete_IV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            dialog.dismiss();
                            intDelete = (int) v.getTag();
                            AlertYesNo("Are you sure to delete this portfolio", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    deletePortfolio(dataList.get((Integer) v.getTag()).getId() + "", PortfolioRecyclerAdapter.this);

                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    edit_IV.setTag(v.getTag());
                    edit_IV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            dialog.dismiss();
                            AlertYesNo("Are you sure to edit this portfolio", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", fragment.portfolio.getUrl());
                                    bundle.putSerializable("portFolio", dataList.get((Integer) v.getTag()));
                                    fragment.replaceFragment(R.id.fl_container_home, new AddPortfolioFragment(), "editPortfolio", bundle);

                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.dismiss();

                        }
                    });
                }
            });
        }
    }

    AlertDialog alertDialog;

    public void AlertYesNo(String message, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener noOnClick) {
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog = null;
        }
        if (alertDialog == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogTheme);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("yes",
                    onClickListener);

            alertDialogBuilder.setNegativeButton("No", noOnClick);
            alertDialog = alertDialogBuilder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

}
