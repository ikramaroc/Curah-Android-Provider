package com.curahservice.netset.module.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.SettingFragment;
import com.curahservice.netset.module.appointment.AppointmentFragment;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.module.history.HistoryFragment;
import com.curahservice.netset.module.home.HomeFragment;
import com.curahservice.netset.module.message.MessageFragment;
import com.curahservice.netset.module.myProfile_tabs.BarberProfileFragment;
import com.curahservice.netset.module.notification.NotificationFragment;
import com.curahservice.netset.module.portfolio.PortfolioFragment;
import com.curahservice.netset.retrofitManager.networkCheck;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.MenuModel;
import com.jkb.slidemenu.SlideMenuLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class DrawerActivity extends BaseActivity implements networkCheck {

    public SlideMenuLayout slideMenuLayout;
    public Toolbar toolbar;
    private RecyclerView slideMenuRv;
    private FrameLayout homeContainerFl;
    private LinearLayout parentLay;
    private Fragment fragmentToOpen;
    private List<MenuModel> menuModelList = new ArrayList<>();
    private long mBackPressed;
    public TextView toolbarTitleTxt, txt_right_toolbar;
    private LinearLayout parentLayOfToolbar;
    private CircularImageView img_user;
    private TextView userName, address;
    public createAccount.UserInfo userInfo;
    private RelativeLayout rel_user_desc;
    private boolean dragging = true;

    public DrawerActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userInfo = store.getUserPojo(Const.USERDATA);
        ButterKnife.bind(this);
        initViews(savedInstanceState);
        initToolBar();
        initMenuRecyclerView();
        regiterNetwork(DrawerActivity.this);
        checkIntentNotification();

    }

    private void checkIntentNotification() {
        if (getIntent().getExtras().getString("from") != null) {
            if (getIntent().getExtras().getString("from").equals("notificationListner")) {
                if (getIntent().getStringExtra("type").equals("Message-Notification")) {

                    Bundle data=new Bundle();
                    data.putBoolean("fromNotification",true);
                    data.putString("connection_id",getIntent().getExtras().getString("id"));
                    data.putString("senderId",getIntent().getExtras().getString("otherUserId"));
                    fragmentToOpen = new MessageFragment();
                    fragmentToOpen.setArguments(data);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container_home, fragmentToOpen)
                            .addToBackStack(null)
                            .commit();



                } else if (getIntent().getStringExtra("type").equals("Request-Notification") || getIntent().getStringExtra("type").equals("Cancel-Notification")) {

                    try {
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container_home, new HomeFragment(getIntent().getStringExtra("type"), getIntent().getStringExtra("id")))
                            .addToBackStack(null)
                            .commit();

                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMenuDrawer();
        if (!internetConnection) {
            showNoNetworkDialog(internetStatus);
        }
    }

    @OnClick(R.id.logout_Btn)
    void logout() {
        AlertYesNo("Are you sure you want to logout.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getLogoutApi();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == logoutApi) {
            store.setBoolean(Const.LOGIN, false);
            store.setBoolean(Const.SOCIAL_LOGIN, false);
            finish();
            startActivity(new Intent(DrawerActivity.this, IndexActivity.class)
                    .putExtra("from", "logout")
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }

    private void initViews(Bundle savedInstance) {
        slideMenuLayout = findViewById(R.id.mainSlideMenu);
        parentLay = findViewById(R.id.parentLay);
        slideMenuRv = findViewById(R.id.rv_menu);
        homeContainerFl = findViewById(R.id.fl_container_home);
        img_user = findViewById(R.id.img_user);
        userName = findViewById(R.id.userName);
        address = findViewById(R.id.address);
        rel_user_desc=findViewById(R.id.rel_user_desc);

        //call home screen
        if (savedInstance == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container_home, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setMenuDrawer() {
        Glide.with(DrawerActivity.this)
                .load(userInfo.getImgUrl() + userInfo.getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(img_user);
        userName.setText(userInfo.getFirstname() + " " + userInfo.getLastname());
        address.setText(userInfo.getAddress());
        rel_user_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideMenuLayout.isLeftSlideOpen()) {
                    slideMenuLayout.closeLeftSlide();
                }

                fragmentToOpen = new BarberProfileFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container_home, fragmentToOpen)
                        .commit();
            }
        });
    }

    public void initToolBar() {
        toolbar = findViewById(R.id.toolbar_center_title);
        toolbarTitleTxt = findViewById(R.id.toolbar_title);
        txt_right_toolbar = findViewById(R.id.txt_right_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHandle();

            }
        });
    }


    public void backHandle() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
        if (currentFragment instanceof HomeFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                slideMenuLayout.toggleLeftSlide();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                slideMenuLayout.toggleLeftSlide();
            } else {
                if (dragging) {
                    slideMenuLayout.toggleLeftSlide();
                } else {
                    getSupportFragmentManager().popBackStack();
                }

            }
        }
    }

    public void setToolbarVisibilty(int isVisible) {
        parentLayOfToolbar = findViewById(R.id.toolbar_layout);
        toolbar.setVisibility(isVisible);
        parentLayOfToolbar.setVisibility(isVisible);
    }

    public void setToolbarAttributes(String screenTitile, int navicon, boolean setDragingOfMenu) {
        toolbarTitleTxt.setText(screenTitile);
        txt_right_toolbar.setVisibility(View.GONE);
        slideMenuLayout.setAllowTogging(setDragingOfMenu);
        dragging = setDragingOfMenu;
        if (navicon != 0)
            toolbar.setNavigationIcon(navicon);
        else
            toolbar.setNavigationIcon(null);
    }

    public void setToolbarBackground(int backgroundColor) {
        if (backgroundColor != 0) {
            toolbar.setBackgroundColor(backgroundColor);
        }
    }

    public void skipText(int visiblty, String text, View.OnClickListener onClickListener) {
        txt_right_toolbar.setVisibility(visiblty);
        txt_right_toolbar.setText(text);
        txt_right_toolbar.setOnClickListener(onClickListener);
    }

    private void initMenuRecyclerView() {
        menuModelList.add(new MenuModel(getString(R.string.home), R.mipmap.ic_home));
        menuModelList.add(new MenuModel(getString(R.string.myprofile), R.mipmap.ic_profile));
        menuModelList.add(new MenuModel(getString(R.string.history), R.mipmap.ic_history));
        menuModelList.add(new MenuModel(getString(R.string.appointment), R.mipmap.ic_appointment));
        menuModelList.add(new MenuModel(getString(R.string.portfolio), R.mipmap.ic_portfolio));
        menuModelList.add(new MenuModel(getString(R.string.message), R.mipmap.ic_message));
        menuModelList.add(new MenuModel(getString(R.string.notification), R.mipmap.ic_notification));
        menuModelList.add(new MenuModel(getString(R.string.settings), R.mipmap.ic_setting));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        slideMenuRv.setLayoutManager(linearLayoutManager);
        DrawerMenuAdapter menuAdapter = new DrawerMenuAdapter(this, menuModelList);
        slideMenuRv.setAdapter(menuAdapter);
        setMenuDrawer();
    }


    @Override
    public void onBackPressed() {


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
        if (currentFragment instanceof HomeFragment || currentFragment instanceof HistoryDetailFragment) {
            if (slideMenuLayout.isLeftSlideOpen()) {
                slideMenuLayout.closeLeftSlide();
            } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                } else {
                    try {
                        Toast.makeText(DrawerActivity.this, "Tap back button again in order to exit", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mBackPressed = System.currentTimeMillis();
            }
        } else {
            if (slideMenuLayout.isLeftSlideOpen()) {
                slideMenuLayout.closeLeftSlide();
            } else if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                } else {
                    try {
                        Toast.makeText(DrawerActivity.this, "Tap back button again in order to exit", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mBackPressed = System.currentTimeMillis();
            }
        }

    }

    //MenuItemClick
    public void menuItemClick(int adapterPosition) {
        slideMenuLayout.closeLeftSlide();
        switch (adapterPosition) {
            case 0:
                try {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentToOpen = new HomeFragment();
                break;
            case 1:
                fragmentToOpen = new BarberProfileFragment();
                break;
            case 2:
                fragmentToOpen = new HistoryFragment();
                break;
            case 3:
                fragmentToOpen = new AppointmentFragment();
                break;
            case 4:
                fragmentToOpen = new PortfolioFragment();
                break;
            case 5:
                fragmentToOpen = new MessageFragment();
                break;
            case 6:
                fragmentToOpen = new NotificationFragment();
                break;
            case 7:
                fragmentToOpen = new SettingFragment();
                break;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container_home, fragmentToOpen)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container_home, fragmentToOpen)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void internetStatus(boolean connection) {
        Log.e("--------------", connection + "");
        if (connection) {
            if (networkAlertDialog != null && networkAlertDialog.isShowing()) {
                networkAlertDialog.dismiss();
                internetConnection = true;
            }
            try {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container_home);
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(currentFragment).attach(currentFragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
