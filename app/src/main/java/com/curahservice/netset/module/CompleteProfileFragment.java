package com.curahservice.netset.module;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ZipPlaceName;
import com.curahservice.netset.model.userInformation;
import com.curahservice.netset.module.addServices.AddServicesFragment;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class CompleteProfileFragment extends BaseFragment implements BaseFragment.PermCallback, BaseFragment.PlacePicker {


    File driving, identity;
    @BindView(R.id.userDl)
    CircularImageView userDl;
    @BindView(R.id.userIc)
    CircularImageView userIc;
    String clickFrom = "0";
    @BindView(R.id.from_time_ET)
    TextView from_time_ET;
    @BindView(R.id.to_time_ET)
    TextView to_time_ET;
    @BindView(R.id.licence_num_edt)
    AppCompatEditText licence_num;
    @BindView(R.id.location_ET)
    AppCompatEditText location_ET;
    String experienceIs = "";
    @BindView(R.id.city_ET)
    AppCompatEditText city_ET;
    @BindView(R.id.experince)
    Spinner experience;

    @BindView(R.id.from_breakTime)
            TextView from_breakTime;
    @BindView(R.id.to_time_breakTime)
            TextView to_time_breakTime;



    Call<JsonObject> createProfileApi;
    private int hr;
    private int min;
    LatLng userLocation;
    ZipPlaceName model;
    View root;
    userInformation information;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.complete_profile, container, false);
            ButterKnife.bind(this, root);
            checkLoactionPermission();
            Bundle bundle = getArguments();
            information = (userInformation) bundle.getSerializable("userInform");
            openPlacePicker(location_ET);
            openPlacePicker(city_ET);
            setSpinnerExperience();
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle();
    }

    private void setSpinnerExperience() {

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.experience, R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        experience.setAdapter(adapter);
        experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void checkLoactionPermission() {
        String[] arrayPermission = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE
        };
        if (checkPermissions(arrayPermission, 120, this)) {
            userLocation = new LatLng(Double.parseDouble(store.getString(getString(R.string.latitude), "0.0")), Double.parseDouble(store.getString(getString(R.string.longitude), "0.0")));
            model = getCompleteAddressString(getActivity(), userLocation.latitude, userLocation.longitude);
            city_ET.setText(model.getCity());
            location_ET.setText(model.getAdress());
        }
    }


    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundColor(Color.TRANSPARENT);
            myactivity.setTitle(getString(R.string.complete_profile), true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            }, 0);
        }
    }

    @OnClick(R.id.create_profile_TV)
    void createProfile() {
        checkValidation();

    }


    private void checkValidation() {
        if (driving == null) {
            Toast.makeText(getActivity(), "Please add your Cosmetology License", Toast.LENGTH_LONG).show();
        } else if (identity == null) {
            Toast.makeText(getActivity(), "Please add your Identity card", Toast.LENGTH_LONG).show();
        } else if (experienceIs.length() == 0) {
            Toast.makeText(getContext(), "Please select experience", Toast.LENGTH_LONG).show();
        } else if (location_ET.getText().toString().trim().length() == 0) {
            location_ET.setError("Please add location");
            location_ET.requestFocus();
        } else if (city_ET.getText().toString().trim().length() == 0) {
            city_ET.setError("Please add city");
            city_ET.requestFocus();
        } else if (from_time_ET.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please select working hours", Toast.LENGTH_LONG).show();
        } else if (to_time_ET.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please select working hours", Toast.LENGTH_LONG).show();
        } else if (experienceIs.equals("Enter Your Experience")) {
            Toast.makeText(getContext(), "Please select experience", Toast.LENGTH_LONG).show();
        } else {
            createProfileCustomer(information.getFname(), information.getLname()
                    , model.getAdress(), information.getPhone(), model.getCity(),model.getState(),
                    String.valueOf(userLocation.latitude),
                    String.valueOf(userLocation.longitude), information.getFblink(), information.getInstalink(), information.getYelplink(), from_time_ET.getText().toString(), to_time_ET.getText().toString().trim(),from_breakTime.getText().toString().trim(),to_time_breakTime.getText().toString().trim(), experienceIs, licence_num.getText().toString().trim(), information.getUserProfile(), driving, identity);
        }
    }

    private void createProfileCustomer(String fname, String lname, String address, String phone, String city,String state, String lattitude, String longitude, String fblink, String instalink, String yelplink, String appointfrom, String appointto,String brkTimeFrom,String brkTimeTo, String experinec, String licence_num, File imageFile, File driving, File indentity) {
        createProfileApi = apiInterfaceAuth.createProfile(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(fname), returnRequestBody(lname), returnRequestBody(address), returnRequestBody(phone), returnRequestBody(city),returnRequestBody(state), returnRequestBody(lattitude), returnRequestBody(longitude), returnRequestBody(fblink), returnRequestBody(instalink), returnRequestBody(yelplink), returnRequestBody(convertTo24Hour(appointfrom.toLowerCase())), returnRequestBody(convertTo24Hour(appointto.toLowerCase())),returnRequestBody(brkTimeFrom),returnRequestBody(brkTimeTo), returnRequestBody(experinec), returnRequestBody(licence_num), returnRequestFile(imageFile, "profile_image"), returnRequestFile(driving, "driving_license"), returnRequestFile(indentity, "identification_card"));
        apiHitAndHandle.makeApiCall(createProfileApi, this);

    }


    private void openPlacePicker(final AppCompatEditText addressEdt) {
        addressEdt.setClickable(true);
        addressEdt.setFocusable(false);
        addressEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress(addressEdt, CompleteProfileFragment.this);

            }
        });

    }


    @OnClick(R.id.userDl)
    void imagePicker1() {
        clickFrom = "1";
        checkCameraPermission();
    }


    @OnClick(R.id.to_time_ET)
    void todateClick() {
        createdTimePickerDialog(timePickerListenerTo).show();
    }

    @OnClick(R.id.from_time_ET)
    void fromdateClick() {
        createdTimePickerDialog(timePickerListenerFrom).show();
    }

    @OnClick(R.id.to_time_breakTime)
    void toBreakTimeClick() {
        createdTimePickerDialog(breakToListner).show();
    }

    @OnClick(R.id.from_breakTime)
    void fromBreakTimeClick() {
        createdTimePickerDialog(breakFromListner).show();
    }


    @OnClick(R.id.userIc)
    void imagePicker2() {
        clickFrom = "2";
        checkCameraPermission();
    }


    protected Dialog createdTimePickerDialog(TimePickerDialog.OnTimeSetListener timePickerListener) {
        return new TimePickerDialog(getActivity(), R.style.DialogTheme, timePickerListener, hr, min, false);
    }

    private TimePickerDialog.OnTimeSetListener breakFromListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min, from_breakTime);
        }
    };


    private TimePickerDialog.OnTimeSetListener breakToListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min, to_time_breakTime);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListenerFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min, from_time_ET);
        }
    };


    private TimePickerDialog.OnTimeSetListener timePickerListenerTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
// TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min, to_time_ET);
        }
    };

    private void updateTime(int hours, int mins, TextView editText) {
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
        editText.setText(aTime);
    }


    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
    }

    private void checkCameraPermission() {
        String[] arrayPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (checkPermissions(arrayPermission, 99, this)) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {

                    if (new File(imagePath).exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());
                        if (clickFrom.contains("1")) {
                            driving = new File(imagePath);
                            userDl.setImageBitmap(myBitmap);

                        } else if (clickFrom.contains("2")) {
                            identity = new File(imagePath);
                            userIc.setImageBitmap(myBitmap);

                        }
                    }
                }
            }, 200).crop().start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("onActivityResult", "---------CReate");

        if (requestCode == 4) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            ImageUtils.activityResult(requestCode, resultCode, data);
        }
    }


    public static boolean compareDates(String psDate1, String psDate2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date1 = dateFormat.parse(psDate1);
        Date date2 = dateFormat.parse(psDate2);
        if (date2.after(date1)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == createProfileApi) {
            Gson gson = new Gson();
            createAccount userProfile = gson.fromJson(object.toString(), createAccount.class);
            if (userProfile.getStatus() == 200) {
               /* store.saveString("token", userProfile.getUserInfo().getToken());
                store.setInt(Const.USER_ID, userProfile.getUserInfo().getId());
                store.saveUserPojo(Const.USERDATA, userProfile.getUserInfo());*/

                replaceFragment(R.id.container, new AddServicesFragment(), "AddServicesFragment", null);

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
    public void permGranted(int resultCode) {
        Log.e("code", "camera----" + resultCode);
        if (resultCode == 99) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {
                    if (new File(imagePath).exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());
                        if (clickFrom.contains("1")) {
                            driving = new File(imagePath);
                            userDl.setImageBitmap(myBitmap);

                        } else if (clickFrom.contains("2")) {
                            identity = new File(imagePath);
                            userIc.setImageBitmap(myBitmap);

                        }
                    }
                }
            }, 200).crop().start();
        }
        if (resultCode == 120) {
            userLocation = new LatLng(Double.parseDouble(store.getString(getString(R.string.latitude), "0.0")), Double.parseDouble(store.getString(getString(R.string.longitude), "0.0")));
            model = getCompleteAddressString(getActivity(), userLocation.latitude, userLocation.longitude);

            city_ET.setText(model.getCity());
            location_ET.setText(model.getAdress());
        }
    }

    @Override
    public void permDenied(int resultCode) {

    }

    @Override
    public void locationSuccess(ZipPlaceName zipPlaceName, LatLng userlocation) {
        userLocation = new LatLng(Double.parseDouble(userlocation.latitude + ""), Double.parseDouble(userlocation.longitude + ""));
        model = getCompleteAddressString(getActivity(), userLocation.latitude, userLocation.longitude);

        city_ET.setText(model.getCity());
        location_ET.setText(model.getAdress());

    }

    @Override
    public void locationError() {

    }
}
