package com.curahservice.netset.module.myProfile_tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ReviewModel;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.ReviewFragment;
import com.curahservice.netset.util.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class BarberProfileFragment extends BaseFragment {


    private ImageView profileIV;

    private RatingBar ratingRB;
    private TextView rateTV;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.address_TV)
    TextView address_TV;

    @BindView(R.id.experience_detail_TV)
    TextView experience_detail_TV;

    @BindView(R.id.number_TV)
    TextView number_TV;

    @BindView(R.id.fb_IV)
    ImageView fb_IV;
    @BindView(R.id.insta_IV)
    ImageView insta_IV;
    @BindView(R.id.socialMedia_TV)
    TextView socialMedia_TV;

    @BindView(R.id.scroll)
    NestedScrollView scroller;

    @BindView(R.id.backCover)
    ImageView backCover;

    public boolean isVisibleTool;
    int position = 0;

    View view;
    createAccount.UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.barber_profile, container, false);
            ButterKnife.bind(this, view);
            userInfo = store.getUserPojo(Const.USERDATA);
            hideToolbarIcons(view, R.id.scroll);
            profileIV = view.findViewById(R.id.profile_IV);
            ratingRB = view.findViewById(R.id.rating_RB);
            rateTV = view.findViewById(R.id.rate_TV);
            tabLayout = view.findViewById(R.id.tab_layout);
            changeTabsFont(tabLayout, tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));
            setToolBar(userInfo.getFirstname() + " " + userInfo.getLastname());
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab selectedTab) {
                    // Fragment fragment = null;
                    position=selectedTab.getPosition();
                    switch (selectedTab.getPosition()) {
                        case 0:
                            getTabOpen(0);
                            break;
                        case 1:
                            getTabOpen(1);
                            break;
                        case 2:
                            getTabOpen(2);
                            break;
                        case 3:
                            getTabOpen(3);
                            break;

                    }
                    changeTabsFont(tabLayout, selectedTab);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            checkScroller();
        }else {
            ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
        }

        return view;
    }


    private void checkScroller() {
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        isVisibleTool = false;
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
                    }
                    if (scrollY < oldScrollY) {
                        isVisibleTool = false;
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
                    }
                    if (scrollY == 0) {
                        isVisibleTool = true;
                        setToolBar(userInfo.getFirstname() + " " + userInfo.getLastname());
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
                    }
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        isVisibleTool = false;
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
                    }
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        userInfo = store.getUserPojo(Const.USERDATA);
        setData();
        if (position!=3) {
            getTabOpen(position);
        }
        if (isVisibleTool){
            ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
            setToolBar(userInfo.getFirstname() + " " + userInfo.getLastname());
        }


    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == reviewApi) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            ReviewModel reviewModel = gson.fromJson(object.toString(), ReviewModel.class);
            if (reviewModel.getStatus() == 200) {
                if (reviewModel.getMyReviews().size() > 0) {
                    ReviewFragment fragment = new ReviewFragment();
                    Bundle data = new Bundle();
                    data.putSerializable("review", reviewModel);
                    replaceFragment(R.id.fl_container_home, fragment, "", data);
                } else {
                    showMessage(getActivity(), "", "No review found.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        }
    }

    @OnClick(R.id.fb_IV)
    void fbClick() {
        if (URLUtil.isValidUrl(userInfo.getFacebookLink())) {
            isVisibleTool=false;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(userInfo.getFacebookLink()));
            startActivity(i);
        }
    }

    @OnClick(R.id.insta_IV)
    void InstaClick() {
        isVisibleTool=false;
        if (URLUtil.isValidUrl(userInfo.getInstagramLink())) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(userInfo.getInstagramLink()));
            startActivity(i);
        }
    }

    @OnClick(R.id.rate_TV)
    void review()
    {

        getReview(String.valueOf(store.getInt("userId")), BarberProfileFragment.this);
    }

    private void setData() {
        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(profileIV);
        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.ic_camera))
                .into(backCover);


        if (userInfo.getRating() != null) {
            ratingRB.setRating(Float.parseFloat(userInfo.getRating()));
            rateTV.setText(" (" + userInfo.getReviewCount() + "  reviews)");
        } else {
            ratingRB.setRating(Float.valueOf("0"));
            rateTV.setText(" (" + 0 + "  reviews)");
        }

        address_TV.setText(userInfo.getAddress());
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(userInfo.getPhone(), "US");
            //Since you know the country you can format it as follows:
            number_TV.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            // System.out.println(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        experience_detail_TV.setText(userInfo.getExperience());


        if ((!URLUtil.isValidUrl(userInfo.getFacebookLink()) && !(URLUtil.isValidUrl(userInfo.getInstagramLink())))) {
            clickFalse(fb_IV);
            clickFalse(insta_IV);
        }else {
            clickTrue(insta_IV);
            clickTrue(fb_IV);
        }
        if (!URLUtil.isValidUrl(userInfo.getFacebookLink())) {
            clickFalse(fb_IV);
        }else {
            clickTrue(fb_IV);
        }
        if (!URLUtil.isValidUrl(userInfo.getInstagramLink())) {
            clickFalse(insta_IV);
        }else {
            clickTrue(insta_IV);
        }

    }

    private void clickFalse(ImageView img) {
        img.setAlpha(Float.valueOf("0.2"));
        img.setClickable(false);
    }
    private void clickTrue(ImageView img) {
        img.setAlpha(Float.valueOf("1.0"));
        img.setClickable(true);
    }

    public void getTabOpen(int position) {

        switch (position) {


            case 0:
                MyServicesTab1 tab1 = new MyServicesTab1();
                CallFragment(tab1);
                break;
            case 1:

                ScheduleTab3 tab3 = new ScheduleTab3();
                CallFragment(tab3);


                break;
            case 2:
                BankInfoTab2 tab2 = new BankInfoTab2(BarberProfileFragment.this);
                CallFragment(tab2);
                break;
            case 3:
                MyDocumentsTab4 tab4 = new MyDocumentsTab4();
                CallFragment(tab4);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                isVisibleTool = true;
                replaceFragmentKey(R.id.fl_container_home, new EditProfileFragment(), "EditProfileFragment", "back", null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void CallFragment(Fragment fragment) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.services_frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    private void setToolBar(String name) {
        setHasOptionsMenu(true);
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(android.R.color.transparent);
        ((DrawerActivity) getActivity()).userInfo = userInfo;
        ((DrawerActivity) getActivity()).setMenuDrawer();
        ((DrawerActivity) getActivity()).setToolbarAttributes(name, R.mipmap.ic_menu, true);
    }
}
