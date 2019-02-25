package com.curahservice.netset.module.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.curahservice.netset.BuildConfig;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppointmentDetail;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ServicesAdapter;
import com.curahservice.netset.model.ServicesModel;
import com.curahservice.netset.model.ZipPlaceName;
import com.curahservice.netset.module.CompleteProfileFragment;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.retrofitManager.ApiClient;
import com.curahservice.netset.retrofitManager.ApiHitAndHandle;
import com.curahservice.netset.retrofitManager.ApiInterface;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.AppConstants;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.curahservice.netset.util.PrefStore;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.curahservice.netset.util.TimeUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class BaseFragment extends Fragment implements AppConstants, ApiResponse {

    public BaseActivity baseActivity;

    LocationManager locationManager;
    public FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public PrefStore store;
    PlacePicker PlacePicker;
    public ServicesAdapter servicesAdapter;
    public ApiHitAndHandle apiHitAndHandle;
    public ApiInterface apiInterface, apiInterfaceAuth;
    public Call<JsonObject> appointmentDeatil, reviewApi,
            acceptRejectApi, startServiceApi, endServiceApi, cancelServiceApi, historyApi;
    public InputFilter filter;
    public Call<JsonObject> rateUser;


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationRequest = new LocationRequest();
        store = new PrefStore(getActivity());
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        store = new PrefStore(getActivity());
        apiHitAndHandle = ApiHitAndHandle.getInstance(getActivity());
        apiInterface = new ApiClient(null, "1.1").getClient().create(ApiInterface.class);
        apiInterfaceAuth = new ApiClient(store.getString("token"), "1.1").getClient().create(ApiInterface.class);

        filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
    }

    public void editextNoZero(final AppCompatEditText yourTextEdit) {
        yourTextEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (yourTextEdit.getText().toString().length() > 1 && yourTextEdit.getText().toString().startsWith("0")) {
                    yourTextEdit.setText(yourTextEdit.getText().toString().replace("0", ""));
                    yourTextEdit.setSelection(yourTextEdit.getText().length());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    public Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }



    public String converStringToDate(String dtStart) {
        String dateStr = dtStart;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        Date date1 = new Date();
        df.format(date1);
        Log.d("datetee", "onCreate3: " + date);
        Log.d("datetee", "onCreate3: " + TimeUtils.millisToLongDHMS(date1.getTime() - date.getTime()));
        Log.d("datetee", "onCreate3: " + date1);
        return TimeUtils.millisToLongDHMS(date1.getTime() - date.getTime());
    }


    public String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "just now";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public void pendingAdminApproval() {
        final Dialog dialog = baseActivity.showCustomDialog(getActivity(), R.layout.alert_please_wait, "");
        ImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        baseActivity.addTransparency(dialog);
    }

    Dialog dialog;
    public void rateLayout(final String raterId, final String bookingid) {
        if (dialog==null){
            dialog= baseActivity.showCustomDialog(getActivity(), R.layout.alert_rate_review, "");
            dialog.setCancelable(false);
            Button submit_Btn = dialog.findViewById(R.id.submit_Btn);
            final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
            final EditText editText = dialog.findViewById(R.id.editText);
            submit_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().trim().length() == 0) {
                        editText.setError("Please add review comment");
                        editText.requestFocus();
                    } else {
                        addrating(raterId, bookingid, ratingBar.getRating() + "", editText.getText().toString().trim());
                    }
                    dialog.dismiss();
                    dialog=null;
                }
            });
        }

    }

    private void addrating(String raterId, String bookingId, String rating, String rateComment) {
        rateUser = apiInterfaceAuth.rating(String.valueOf(store.getInt("userId")), raterId + "", bookingId + "", rating, rateComment);
        apiHitAndHandle.makeApiCall(rateUser, BaseFragment.this);

    }


    public void getReview(String userid, ApiResponse response) {
        reviewApi = apiInterfaceAuth.reviewList(store.getInt("userId") + "", userid);
        apiHitAndHandle.makeApiCall(reviewApi, response);
    }


    public void startService(String appoinmentId, ApiResponse response) {
        startServiceApi = apiInterfaceAuth.startService(String.valueOf(store.getInt("userId")), appoinmentId);
        apiHitAndHandle.makeApiCall(startServiceApi, response);
    }

    public void endService(String appoinmentId, ApiResponse response) {
        endServiceApi = apiInterfaceAuth.endService(String.valueOf(store.getInt("userId")), appoinmentId);
        apiHitAndHandle.makeApiCall(endServiceApi, response);
    }

    public void cancelService(String appoinmentId, String message, ApiResponse response) {
        cancelServiceApi = apiInterfaceAuth.cancelAppointment(String.valueOf(store.getInt("userId")), appoinmentId, Const.USER_TYPE, message);
        apiHitAndHandle.makeApiCall(cancelServiceApi, response);
    }

    public void acceptRejectApi(String appoinmentId, String status, ApiResponse response) {
        acceptRejectApi = apiInterfaceAuth.acceptRejectAppointments(String.valueOf(store.getInt("userId")), appoinmentId, status);
        apiHitAndHandle.makeApiCall(acceptRejectApi, response);
    }

    public void getHistory(ApiResponse response) {
        historyApi = apiInterfaceAuth.history(String.valueOf(store.getInt("userId")), Const.USER_TYPE);
        apiHitAndHandle.makeApiCall(historyApi, response);
    }

    public void openAppointmentDetail(String bookinId, ApiResponse response) {
        appointmentDeatil = apiInterfaceAuth.appointmentDetail(String.valueOf(store.getInt("userId")), Const.USER_TYPE, bookinId, store.getString(getString(R.string.latitude)), store.getString(getString(R.string.longitude)));
        apiHitAndHandle.makeApiCall(appointmentDeatil, response);
    }


    public static void hideSoftKeyboard(Activity activity) throws Exception {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public boolean isValidMail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("curahLog", msg);
        }
    }

    public void replaceFragmentNull(@IdRes int containerViewId,
                                    @NonNull Fragment fragment,
                                    @NonNull String fragmentTag) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss();
    }

    public void replaceFragmentNull2(@IdRes int containerViewId,
                                     @NonNull Fragment fragment
    ) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment)
                .commitAllowingStateLoss();
    }

    public void replaceFragmentKey(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag, String key,
                                   Bundle args) {
        fragment.setArguments(args);
        getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(key)
                .commitAllowingStateLoss();
    }


    public void replaceFragment(@IdRes int containerViewId,
                                @NonNull Fragment fragment,
                                @NonNull String fragmentTag,
                                Bundle args) {
        fragment.setArguments(args);
        getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void hideActionBar() {
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().hide();
        }
    }

    public void showActionBar() {
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().show();
        }
    }

    public void changeTabsFont(TabLayout tabLayout, TabLayout.Tab selectedTab) {
        Typeface tf = ResourcesCompat.getFont(getActivity(), R.font.aeh_regular);
        Typeface tfb = ResourcesCompat.getFont(getActivity(), R.font.aeh_bold);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            ViewGroup vgTab = (ViewGroup) vg.getChildAt(i);
            int tabChildsCount = vgTab.getChildCount();

            for (int k = 0; k < tabChildsCount; k++) {
                View tabViewChild = vgTab.getChildAt(k);
                if (selectedTab.equals(tab)) {
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTypeface(tfb, Typeface.BOLD);
                        ((TextView) tabViewChild).setGravity(Gravity.BOTTOM);
                        ((TextView) tabViewChild).setTextSize(getResources().getDimension(R.dimen.txt_size_d));
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(android.R.color.white));
                    }
                } else {
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTypeface(tf, Typeface.NORMAL);
                        ((TextView) tabViewChild).setGravity(Gravity.BOTTOM);
                        ((TextView) tabViewChild).setTextSize(getResources().getDimension(R.dimen.txt_size_d));
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(android.R.color.white));
                    }
                }
            }
        }
    }

    public void setAdapter_myServices(RecyclerView recyclerView, List<AppointmentDetail.ServiceName> dataList, boolean edit) {
        MyServicesRecyclerAdapter mAdapter = new MyServicesRecyclerAdapter(getActivity(), dataList, edit);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    public void setServicesAdapter(RecyclerView recyclerView, ArrayList<createAccount.Service> dataList, boolean edit, ServicesAdapter.editDeleteClick editDeleteClick,boolean editnew) {
        servicesAdapter = new ServicesAdapter(getActivity(), dataList, edit,editnew, editDeleteClick);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(servicesAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );

    }


    public void hideToolbarIcons(View view, @IdRes int id) {
        NestedScrollView scroller = view.findViewById(id);
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
                    }
                    if (scrollY < oldScrollY) {

                    }
                    if (scrollY == 0) {
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
                    }
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.INVISIBLE);
                    }
                }
            });
        }
    }

    public LocationCallback mLocationCallback;
    PermCallback permCallback;
    private int reqCode;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "BaseFRgment----------" + requestCode);
        if (requestCode == 4) {
            try {
                hideSoftKeyboard(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                String CurrentString = place.getAddress().toString();
                String[] separated = CurrentString.split(",");
                String locality1 = separated[0];
                String locality2 = "";
                for (int i = 1; i < separated.length; i++) {
                    if (locality2.length() > 0) {
                        locality2 = locality2 + "," + separated[i];
                    } else {
                        locality2 = locality2 + separated[i];
                    }
                }
                ZipPlaceName model = getCompleteAddressString(getActivity(), place.getLatLng().latitude, place.getLatLng().longitude);
                if (PlacePicker != null) {

                    PlacePicker.locationSuccess(model, place.getLatLng());
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                if (PlacePicker != null) {
                    PlacePicker.locationError();
                }
            }
            // super.onActivityResult(requestCode, resultCode, data);
        } else {
            ImageUtils.activityResult(requestCode, resultCode, data);
        }
    }

    public boolean checkPermissions(String[] perms, int requestCode, PermCallback permCallback) {
        this.permCallback = permCallback;
        this.reqCode = requestCode;
        ArrayList<String> permsArray = new ArrayList<>();
        boolean hasPerms = true;
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
                permsArray.add(perm);
                hasPerms = false;
            }
        }
        if (!hasPerms) {
            String[] permsString = new String[permsArray.size()];
            for (int i = 0; i < permsArray.size(); i++) {
                permsString[i] = permsArray.get(i);
            }
            ActivityCompat.requestPermissions(getActivity(), permsString, requestCode);
            return false;
        } else {
            if (requestCode == 120) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    store.saveString(getString(R.string.latitude), location.getLatitude() + "");
                                    store.saveString(getString(R.string.longitude), location.getLongitude() + "");

                                }

                            }
                        });
            }
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("-----------", "onRequestPermissionsResult");

        switch (requestCode) {
            case 120:
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    mLocationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult == null) {
                                return;
                            } else {
                                Log.e("----", "location---" + locationResult.getLastLocation().getLatitude());
                                store.saveString(getString(R.string.latitude), locationResult.getLastLocation().getLatitude() + "");
                                store.saveString(getString(R.string.longitude), locationResult.getLastLocation().getLongitude() + "");
                                permCallback.permGranted(requestCode);
                            }
                        }
                    };
                    mLocationRequest.setInterval(100000);
                    mLocationRequest.setFastestInterval(150000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        store.saveString(getString(R.string.latitude), location.getLatitude() + "");
                                        store.saveString(getString(R.string.longitude), location.getLongitude() + "");

                                    }
                                    permCallback.permGranted(requestCode);

                                }
                            });


                } else {
                    showMessage(getActivity(), "No Access", getString(R.string.nogps), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(
                                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                    permCallback.permDenied(requestCode);
                }

                break;
            case 99:
                permCallback.permGranted(requestCode);
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }


    }

    public AlertDialog alertDialog2;

    public void showMessage(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        try {
            if (alertDialog2 != null && alertDialog2.isShowing()) {
                alertDialog2.dismiss();
                alertDialog2 = null;
            }
            alertDialog2 = new AlertDialog.Builder(
                    context, R.style.DialogTheme).create();
            alertDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog2.setMessage(message);
            alertDialog2.setCancelable(false);
            alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE, "OK", onClickListener);
            alertDialog2.show();
        } catch (Exception e) {

        }

    }

    public ZipPlaceName getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        ZipPlaceName model = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                Log.e("Extras",returnedAddress.getExtras()+"");
                String add = null;
                try {
                    add = returnedAddress.getAddressLine(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    add = "";
                }
                String cityis = "";
                String state = "";
                model = new ZipPlaceName(returnedAddress.getPostalCode(), returnedAddress.getLocality(), returnedAddress.getAdminArea(), add, returnedAddress.getCountryCode());
            } else {
                model = new ZipPlaceName("", "", "", "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model = new ZipPlaceName("", "", "", "", "");
        }
        return model;
    }

    @Override
    public void onSuccess(Call call, Object object) {

    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {

    }

    public interface PermCallback {
        void permGranted(int resultCode);

        void permDenied(int resultCode);
    }


    public interface PlacePicker {
        void locationSuccess(ZipPlaceName zipPlaceName, LatLng userlocation);

        void locationError();
    }

    public void openPlacePicker(final AppCompatEditText addressEdt, final PlacePicker placePicker) {


        addressEdt.setClickable(true);
        addressEdt.setFocusable(false);
        addressEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress(addressEdt, placePicker);

            }
        });


    }

    public void openBankPlacePicker(final AppCompatEditText addressEdt, final PlacePicker placePicker) {

        addressEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.hasFocus()) {
                    getBankAddress(addressEdt, placePicker);
                }
            }
        });
        addressEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    getBankAddress(addressEdt, placePicker);
                }

            }
        });

    }

    public void getAddress(EditText address, PlacePicker locationSelected) {
        this.PlacePicker = locationSelected;

        address.setClickable(false);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_LOCALITY)
                .setCountry("US")

                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(getActivity());
            startActivityForResult(intent, 4);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.


        }

    }


    public void getBankAddress(EditText address, PlacePicker locationSelected) {
        this.PlacePicker = locationSelected;
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_BANK)
                /*.setCountry("US")*/
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(getActivity());
            startActivityForResult(intent, 4);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.


        }
    }


    public String changeDateFormat(String dateString, String sourceDateFormat, String targetDateFormat) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        SimpleDateFormat inputDateFromat = new SimpleDateFormat(sourceDateFormat, Locale.getDefault());
        Date date = new Date();
        try {
            date = inputDateFromat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(targetDateFormat, Locale.getDefault());
        return outputDateFormat.format(date);
    }


    public RequestBody returnRequestBody(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    public RequestBody returnRequestBodyJson(String data) {
        return RequestBody.create(MediaType.parse("application/json"), data);
    }

    public MultipartBody.Part returnRequestFile(File file, String key) {
        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        }
        return body;
    }

}