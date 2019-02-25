package com.curahservice.netset.module;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.userInformation;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.util.ImageUtils;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

import retrofit2.Call;

public class CreateProfileFragment extends BaseFragment implements View.OnClickListener, BaseFragment.PermCallback {
    private Button createProfile_BTN;
    CircularImageView userDp;
    ImageView bg;
    File imageFile;
    AppCompatEditText fname_EDT, lname_edt, mobile_EDT, yelp_link, insta_link, fb_link;
    String from;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


       if (view==null){
           view=inflater.inflate(R.layout.create_profile, container, false);
           createProfile_BTN = view.findViewById(R.id.createProfile_BTN);
           fname_EDT = view.findViewById(R.id.fname_EDT);
           lname_edt = view.findViewById(R.id.lname_edt);
           mobile_EDT = view.findViewById(R.id.mobile_EDT);
           yelp_link = view.findViewById(R.id.yelp_link);
           insta_link = view.findViewById(R.id.insta_link);
           fb_link = view.findViewById(R.id.fb_link);
           bg = view.findViewById(R.id.bg);
           userDp = view.findViewById(R.id.userDp);
           userDp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   checkCameraPermission();
               }
           });
           createProfile_BTN.setOnClickListener(this);
           from = getArguments().getString("from");
           if (from.equals("Create")) {
               userDp.setImageResource(R.mipmap.ic_camera);
               createProfile_BTN.setText("Create Profile");
           } else if (from.equals("CreateSocial")) {
               createProfile_BTN.setText("Create Profile");
               fname_EDT.setText(getArguments().getString("fname"));
               lname_edt.setText(getArguments().getString("lname"));
               if (getArguments().getString("image") != null && getArguments().getString("image").length() > 0) {
                   imageFile = new File(getArguments().getString("image"));
                   if (isAdded()) {
                       setUserImage(imageFile.getAbsolutePath());
                   }
               } else {
                   userDp.setImageResource(R.mipmap.ic_camera);
               }
           } else {
               userDp.setImageResource(R.mipmap.ic_profile_pic_img);

           }

       }
        return view;
    }


    private void setUserImage(String imageFile) {
        Glide.with(getActivity())
                .load("file://" + imageFile)
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.ic_camera))
                .into(userDp);
        Glide.with(getActivity())
                .load("file://" + imageFile)
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.ic_camera))
                .into(bg);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbarTitle();
    }

    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundColor(Color.TRANSPARENT);
            myactivity.setTitle("Create Profile", false, null, 0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createProfile_BTN:
                checkValidation();
                break;
        }
    }


    private void checkValidation() {
        if (fname_EDT.getText().toString().trim().length() == 0) {
            fname_EDT.setError("Please enter first name");
            fname_EDT.requestFocus();
        } else if (lname_edt.getText().toString().trim().length() == 0) {
            lname_edt.setError("Please enter last name");
            lname_edt.requestFocus();
        } else if (mobile_EDT.getText().toString().trim().length() == 0) {
            mobile_EDT.setError("Please enter mobile number");
            mobile_EDT.requestFocus();
        }
        else if (fb_link.getText().toString().length()>0 && !URLUtil.isValidUrl(fb_link.getText().toString())){
            fb_link.setError("Please enter valid link");
            fb_link.requestFocus();
        }
        else if (insta_link.getText().toString().length()>0 && !URLUtil.isValidUrl(insta_link.getText().toString())){
            insta_link.setError("Please enter valid link");
            insta_link.requestFocus();
        }
        else if (yelp_link.getText().toString().length()>0 && !URLUtil.isValidUrl(yelp_link.getText().toString())){
            yelp_link.setError("Please enter valid link");
            yelp_link.requestFocus();
        }

        else {
            userInformation inform = new userInformation(mobile_EDT.getText().toString().trim(), fname_EDT.getText().toString().trim(), lname_edt.getText().toString().trim(), fb_link.getText().toString().trim(), insta_link.getText().toString().trim(), yelp_link.getText().toString().trim(), imageFile);
            CompleteProfileFragment com = new CompleteProfileFragment();
            Bundle b = new Bundle();
            b.putSerializable("userInform", inform);
            com.setArguments(b);
            replaceFragment(R.id.container, com, "CompleteProfileFragment", b);

        }
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
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        userDp.setImageBitmap(myBitmap);
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
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        userDp.setImageBitmap(myBitmap);
                        // backCover.setImageBitmap(myBitmap);
                    }
                }
            }, 200).crop().start();
        }
    }

    @Override
    public void permDenied(int resultCode) {

    }
}
