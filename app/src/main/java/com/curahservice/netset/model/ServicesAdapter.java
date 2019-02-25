package com.curahservice.netset.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.base.MyServicesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    public Activity context;
    public ArrayList<createAccount.Service> dataList;
    private boolean edit = false, editNew = false;
    editDeleteClick editDelete;


    public ServicesAdapter(Activity context, ArrayList<createAccount.Service> dataList, boolean edit, boolean editNew, editDeleteClick deleteClick) {
        this.context = context;
        this.dataList = dataList;
        this.edit = edit;
        this.editNew = editNew;
        this.editDelete = deleteClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_TV)
        TextView name_TV;
        @BindView(R.id.price_TV)
        TextView price_TV;
        @BindView(R.id.delete_IV)
        ImageView delete_IV;
        @BindView(R.id.edit_IV)
        ImageView edit_IV;
        @BindView(R.id.LL)
        LinearLayout LL;
        @BindView(R.id.topView)
        View topView;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public ServicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_add_services, parent, false);

        return new ServicesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ServicesAdapter.MyViewHolder holder, int position) {

        if (position % 2 == 0) {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background));

        } else {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background2));

        }
        holder.name_TV.setText(dataList.get(position).getName());
        holder.price_TV.setText("$" + dataList.get(position).getPrice() + "");
        if (!editNew) {
            if (!dataList.get(position).isIs_approved()) {
                holder.name_TV.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.price_TV.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.name_TV.setTextColor(context.getResources().getColor(android.R.color.black));
                holder.price_TV.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            if (dataList.get(position).getType().equals("C")) {
                holder.name_TV.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.price_TV.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.name_TV.setTextColor(context.getResources().getColor(android.R.color.black));
                holder.price_TV.setTextColor(context.getResources().getColor(R.color.green));

            }
        }

        if (position != 0) {
            holder.topView.setVisibility(View.GONE);
        } else {
            holder.topView.setVisibility(View.VISIBLE);
        }
        if (edit) {
            holder.delete_IV.setVisibility(View.VISIBLE);
            holder.edit_IV.setVisibility(View.VISIBLE);
            holder.delete_IV.setTag(position);
            holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editDelete != null) {
                        editDelete.delete((Integer) v.getTag());
                    }

                }
            });
            holder.edit_IV.setTag(position);
            holder.edit_IV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editDelete != null) {
                        editDelete.edit((Integer) v.getTag());
                    }
                }
            });

        } else {
            holder.delete_IV.setVisibility(View.INVISIBLE);
            holder.edit_IV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface editDeleteClick {


        void edit(int position);

        void delete(int position);
    }
}
