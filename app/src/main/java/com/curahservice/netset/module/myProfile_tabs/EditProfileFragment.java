package com.curahservice.netset.module.myProfile_tabs;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ZipPlaceName;
import com.curahservice.netset.module.addServices.AddServicesFragment;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class EditProfileFragment extends BaseFragment implements BaseFragment.PermCallback, BaseFragment.PlacePicker {

    File imageFile;
    @BindView(R.id.profile_IV)
    CircleImageView profile_IV;
    @BindView(R.id.from_time_ET)
    TextView from_date_ET;
    @BindView(R.id.to_time_ET)
    TextView to_date_ET;


    @BindView(R.id.fname_EDT)
    AppCompatEditText fnameEdt;

    @BindView(R.id.lastName_EDT)
    AppCompatEditText lname;

    @BindView(R.id.mobile_Num)
    AppCompatEditText mobile_Num;

    @BindView(R.id.fb_link)
    AppCompatEditText fb_link;

    @BindView(R.id.insta_link)
    AppCompatEditText insta_link;

    @BindView(R.id.yelp_link)
    AppCompatEditText yelp_link;

    @BindView(R.id.experince)
    Spinner experince;

    @BindView(R.id.location_ET)
    AppCompatEditText location_ET;

    @BindView(R.id.city_ET)
    AppCompatEditText city_ET;

    private String experienceIs;

    @BindView(R.id.backCover)
    ImageView backCover;


    private int clickFrom = 0;
    private int hr;
    private int min;
    ArrayAdapter<CharSequence> experienAdapter;
    createAccount.UserInfo userInfo;
    LatLng userLocation;
    ZipPlaceName model;
    Call<JsonObject> updateApi;
    File image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        userInfo = store.getUserPojo(Const.USERDATA);
        setToolBar();
        hideToolbarIcons(view, R.id.scroll);
        setSpinnerExperience();
        setUserData();
        //city_spinner.setAdapter(baseActivity.setSpinnerAdapter(initialiseSpinnerData()));
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(android.R.color.transparent);
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.editProfile), R.mipmap.ic_back, false);

    }


    private void setUserData() {
        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.ic_camera))
                .into(profile_IV);

        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.ic_camera))
                .into(backCover);


        fnameEdt.setText(userInfo.getFirstname());
        lname.setText(userInfo.getLastname());
        mobile_Num.setText(userInfo.getPhone());
        fb_link.setText(userInfo.getFacebookLink());
        insta_link.setText(userInfo.getInstagramLink());
        yelp_link.setText(userInfo.getYelpLink());
        location_ET.setText(userInfo.getAddress());
        city_ET.setText(userInfo.getCity());
        from_date_ET.setText(changeDateFormat(userInfo.getAppointmentFrom(), "HH:mm:ss", "hh:mm a"));
        to_date_ET.setText(changeDateFormat(userInfo.getAppointmentTo(), "HH:mm:ss", "hh:mm a"));
        experince.setSelection(matchValue(userInfo.getExperience()));
        openPlacePicker(city_ET, this);
        openPlacePicker(location_ET, this);
        model = new ZipPlaceName("", userInfo.getCity(), userInfo.getState(), userInfo.getAddress(), "");

        if (userInfo.getLatitude() !=null && userInfo.getLongitude()!=null && userInfo.getLatitude().length() > 0 && userInfo.getLongitude().length() > 0) {
            userLocation = new LatLng(Double.valueOf(userInfo.getLatitude()), Double.valueOf(userInfo.getLongitude()));

        }
    }


    private void setSpinnerExperience() {

// Create an ArrayAdapter using the string array and a default spinner layout
        experienAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.experience, R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        experienAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        experince.setAdapter(experienAdapter);
        experince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] some_array = getResources().getStringArray(R.array.experience);
                experienceIs = some_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int matchValue(String value) {
        List list = Arrays.asList(getResources().getStringArray(R.array.experience));
        System.out.println(list.indexOf(value));
        return list.indexOf(value);
    }


    @OnClick(R.id.updateProfile_TV)
    void updateProfile() {
        if (fnameEdt.getText().toString().trim().length() == 0) {
            fnameEdt.setError("Please enter first name");
            fnameEdt.requestFocus();
        } else if (lname.getText().toString().trim().length() == 0) {
            lname.setError("Please enter last name");
            lname.requestFocus();
        } else if (mobile_Num.getText().toString().trim().length() == 0) {
            mobile_Num.setError("Please enter mobile number");
            mobile_Num.requestFocus();
        } else if (insta_link.getText().toString().length() > 0 && !URLUtil.isValidUrl(insta_link.getText().toString())) {
            insta_link.setError("Please enter valid link");
            insta_link.requestFocus();
        } else if (fb_link.getText().toString().length() > 0 && !URLUtil.isValidUrl(fb_link.getText().toString())) {
            fb_link.setError("Please enter valid link");
            fb_link.requestFocus();
        }  else if (yelp_link.getText().toString().length() > 0 && !URLUtil.isValidUrl(yelp_link.getText().toString())) {
            yelp_link.setError("Please enter valid link");
            yelp_link.requestFocus();
        } else if (experienceIs.length() == 0) {
            Toast.makeText(getContext(), "Please add experience", Toast.LENGTH_LONG).show();
        } else if (location_ET.getText().toString().trim().length() == 0) {
            location_ET.setError("Please add location");
            location_ET.requestFocus();
        } else if (city_ET.getText().toString().trim().length() == 0) {
            city_ET.setError("Please add city");
            city_ET.requestFocus();
        } else if (from_date_ET.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please add working hours", Toast.LENGTH_LONG).show();
        } else if (to_date_ET.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please add working hours", Toast.LENGTH_LONG).show();
        } else if (experienceIs.equals("Enter Your Experience")) {
            Toast.makeText(getContext(), "Please add your experience", Toast.LENGTH_LONG).show();
        } else if (userLocation == null) {
            Toast.makeText(getActivity(), "Please select address", Toast.LENGTH_LONG).show();
        } else {
            updateUserDetail(fnameEdt.getText().toString().trim(), lname.getText().toString().trim(), model.getAdress(), mobile_Num.getText().toString().trim(), model.getCity(), String.valueOf(userLocation.latitude), String.valueOf(userLocation.longitude), fb_link.getText().toString().trim(), insta_link.getText().toString().trim(), yelp_link.getText().toString().trim(), from_date_ET.getText().toString(), to_date_ET.getText().toString(), experienceIs, imageFile);
        }
    }


    private void updateUserDetail(String fname, String lname, String address, String phone, String city, String lattitude, String longitude, String fblink, String instalink, String yelplink, String appointfrom, String appointto, String experinec, File imageFile) {
        if (imageFile != null) {
            updateApi = apiInterfaceAuth.createProfile(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(fname), returnRequestBody(lname), returnRequestBody(address), returnRequestBody(phone), returnRequestBody(city), returnRequestBody(lattitude), returnRequestBody(longitude), returnRequestBody(fblink), returnRequestBody(instalink), returnRequestBody(yelplink), returnRequestBody(changeDateFormat(appointfrom.toLowerCase(), "hh:mm a", "HH:mm:ss")), returnRequestBody(changeDateFormat(appointto.toLowerCase(), "hh:mm a", "HH:mm:ss")), returnRequestBody(experinec), returnRequestFile(imageFile, "profile_image"));
        } else {
            updateApi = apiInterfaceAuth.createProfile(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(fname), returnRequestBody(lname), returnRequestBody(address), returnRequestBody(phone), returnRequestBody(city), returnRequestBody(lattitude), returnRequestBody(longitude), returnRequestBody(fblink), returnRequestBody(instalink), returnRequestBody(yelplink), returnRequestBody(changeDateFormat(appointfrom.toLowerCase(), "hh:mm a", "HH:mm:ss")), returnRequestBody(changeDateFormat(appointto.toLowerCase(), "hh:mm a", "HH:mm:ss")), returnRequestBody(experinec));
        }
        apiHitAndHandle.makeApiCall(updateApi, this);

    }


    @OnClick(R.id.profile_IV)
    void imagePick() {
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        String[] arrayPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (checkPermissions(arrayPermission, 99, this)) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {
                    imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Glide.with(getActivity())
                                .load("file://" + imageFile.getAbsolutePath())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(profile_IV);


                        Glide.with(getActivity())
                                .load("file://" + imageFile.getAbsolutePath())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.ic_camera))
                                .into(backCover);

                        //backCover.setImageBitmap(myBitmap);
                    }
                }
            }, 200).crop().start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            ImageUtils.activityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void permGranted(int resultCode) {
        if (resultCode == 99) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {
                    imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Glide.with(getActivity())
                                .load("file://" + imageFile.getAbsolutePath())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(profile_IV);

                        Glide.with(getActivity())
                                .load("file://" + imageFile.getAbsolutePath())
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.ic_camera))
                                .into(backCover);


                    }
                }
            }, 200).crop().start();
        }
    }

    @Override
    public void permDenied(int resultCode) {

    }

    @OnClick(R.id.from_time_ET)
    void fromTimePick() {
        clickFrom = 1;
        createdTimePickerDialog().show();
    }

    @OnClick(R.id.to_time_ET)
    void toTimePick() {
        clickFrom = 2;
        createdTimePickerDialog().show();
    }

    protected Dialog createdTimePickerDialog() {
        return new TimePickerDialog(getActivity(), R.style.DialogTheme, timePickerListener, hr, min, false);
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };

    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();

        if (clickFrom == 1) {
            from_date_ET.setText(aTime);
        } else if (clickFrom == 2) {
            to_date_ET.setText(aTime);
        }
    }

    @Override
    public void locationSuccess(ZipPlaceName zipPlaceName, LatLng userlocation) {
        userLocation = new LatLng(Double.parseDouble(userlocation.latitude + ""), Double.parseDouble(userlocation.longitude + ""));
        model = getCompleteAddressString(getActivity(), userLocation.latitude, userLocation.longitude);

        city_ET.setText(model.getCity());
        location_ET.setText(model.getAdress());


    }


    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        Log.e("----------", object.toString());
        if (call == updateApi) {
            Gson gson = new Gson();
            createAccount userProfile = gson.fromJson(object.toString(), createAccount.class);
            if (userProfile.getStatus() == 200) {
                store.saveString("token", userProfile.getUserInfo().getToken());
                store.setInt(Const.USER_ID, userProfile.getUserInfo().getId());
                store.saveUserPojo(Const.USERDATA, userProfile.getUserInfo());
                Toast.makeText(getActivity(),"Profile updated successfully",Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();

            } else {
                showMessage(getActivity(), "", userProfile.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void locationError() {

    }
}
