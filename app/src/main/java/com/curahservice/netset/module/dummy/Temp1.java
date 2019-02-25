package com.curahservice.netset.module.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseActivity;
import com.curahservice.netset.module.dummy.headerlib.DividerDecoration;
import com.curahservice.netset.module.dummy.headerlib.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

public class Temp1 extends BaseActivity {

    private RecyclerView recyclerView;
    private List<TempGetSet> getSetList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
        recyclerView = findViewById(R.id.recyclerView_items);
        // setAdapter();
        prepareListData();

        final DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen._10sdp)
                .setPadding(R.dimen._20sdp)
                .setColorResource(R.color.green)
                .build();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(divider);
        //recyclerView.setAdapter(adapter);
        // setListAdapter(adapter);
        StickyTestAdapter adapter = new StickyTestAdapter(this, getSetList, 2);
        StickyHeaderDecoration decor = new StickyHeaderDecoration(adapter);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decor, 1);
    }

    private void prepareListData() {
        TempGetSet getSet = new TempGetSet("Hello, how are you?", R.drawable.dummy_image, header);
        getSetList.add(getSet);

        getSet = new TempGetSet("A", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("B", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("C", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("D", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("E", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("B", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("C", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("D", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("E", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("B", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("C", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("D", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("E", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("B", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("C", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("D", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        getSet = new TempGetSet("E", R.drawable.dummy_image, items);
        getSetList.add(getSet);

        //mAdapter.notifyDataSetChanged();
    }

    /*void setAdapter() {
        //Arrays.asList(getResources().getStringArray(R.array.services))

        mAdapter = new TempRecyclerAdapter2(this, getSetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        *//*recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));*//*
        recyclerView.setAdapter(mAdapter);

        *//*RecyclerSectionItemDecoration sectionItemDecoration = new RecyclerSectionItemDecoration(
                this.getResources().getDimensionPixelSize(R.dimen._30sdp),
                true, getSectionCallback(getSetList));

        recyclerView.addItemDecoration(sectionItemDecoration);*//*

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );
        prepareListData();
    }

    *//*private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<TempGetSet> findJobList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
//                Log.d(TAG, "isSection: " + findJobList.size());
//                Log.d(TAG, "isSection:1 " + position);
                if (findJobList.size() > 0 && position >= 0 && findJobList.get(position).getMessage() != null) {
                    return (findJobList.size() > 0 && (position == 0 ||
                            (position >= 0 && findJobList.get(position).getMessage() != null &&
                                    !findJobList.get(position).getMessage().equalsIgnoreCase(findJobList.get(position - 1).getMessage()))));

                } else {
                    return false;
                }
            }

            @Override
            public String getSectionHeader(int position) {
                if (findJobList.size() > 0 && position >= 0 && findJobList.get(position).getMessage() != null)
                    return findJobList.get(position).getMessage() + "";
                else
                    return "-1";
            }
        };
    }*/
}