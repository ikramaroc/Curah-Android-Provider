/*
package com.curahservice.netset.module.myProfile_tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyServicesTab1 tab1 = new MyServicesTab1();
                return tab1;
            case 1:
                BankInfoTab2 tab2 = new BankInfoTab2();
                return tab2;
            case 2:
                ScheduleTab3 tab3 = new ScheduleTab3();
                return tab3;
            case 3:
                MyDocumentsTab4 tab4 = new MyDocumentsTab4();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}*/
