package com.curahservice.netset.module.base;

public interface InterfaceContainer {

    interface ItemSelectedFromSpinner {

        void selectedItemIds(String Ids);
    }

    interface CalendarClickListener {
        void onCalDateClickListener(int position, String date);
    }
}
