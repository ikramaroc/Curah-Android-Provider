package com.curahservice.netset.module.dummy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.dummy.headerlib.StickyHeaderAdapter;
import com.curahservice.netset.module.dummy.headerlib.StickyHeaderDecoration;
import com.curahservice.netset.util.AppConstants;

import java.util.List;

public class StickyTestAdapter extends RecyclerView.Adapter<StickyTestAdapter.ViewHolder> implements
        StickyHeaderAdapter<StickyTestAdapter.HeaderHolder>, AppConstants {

    private LayoutInflater inflater;
    List<TempGetSet> datalist;
    int fisrtlistSize;

    StickyTestAdapter(Context context, List<TempGetSet> mydata, int fisrtlistSize) {
        inflater = LayoutInflater.from(context);
        this.datalist=mydata;
        this.fisrtlistSize=fisrtlistSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view;
        if (datalist.get(i).getTag()==items) {
            view = inflater.inflate(R.layout.my_list_item, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.temp_adapter3, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // viewHolder.item.setText("Item " + i);

        if (datalist.get(i).getTag()==header) {
            //viewHolder.item.setText(datalist.get(i).getMessage());
        } else {
            //viewHolder.item.setText(datalist.get(i).getImage());
        }

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public long getHeaderId(int position) {
        if (position<fisrtlistSize) {
            return StickyHeaderDecoration.NO_HEADER_ID;
        }/*else {
            MyData dataPre=datalist.get(position-1);
            if (dataPre.header.equals(dataNext.header)) {
                return StickyHeaderDecoration.NO_HEADER_ID;
            } else {
                return  (long) position/ 7;
            }
        }*/

        return  (long) position/ datalist.size();

        //return (long) position / 7;
      /*  if (position == 0) { // don't show header for first item

        }
        return (long) position / 7;*/
    }

    @NonNull
    @Override
    public HeaderHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(@NonNull HeaderHolder viewHolder, int position) {
        //viewHolder.header.setText("Header " + getHeaderId(position));
        viewHolder.header.setText(datalist.get(position).getMessage());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;

        ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.text_item);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        HeaderHolder(View itemView) {
            super(itemView);
            header= (TextView) itemView;
            //header = itemView.findViewById(R.id.TV4);
        }
    }
}