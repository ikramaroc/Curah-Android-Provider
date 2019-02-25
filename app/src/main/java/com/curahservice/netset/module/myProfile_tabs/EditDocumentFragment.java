package com.curahservice.netset.module.myProfile_tabs;

import android.Manifest;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.EditDocument;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ZipPlaceName;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class EditDocumentFragment extends BaseFragment implements BaseFragment.PermCallback {

    File imgefileDriving, imagefileLicence, imageFile;
    private int clickFrom = 0;
    @BindView(R.id.DL)
    CircularImageView DL;
    @BindView(R.id.IC)
    CircularImageView IC;

    createAccount.UserInfo userInfo;
    Call<JsonObject> editDocument;
    @BindView(R.id.licence_num_edt)
    AppCompatEditText licence_num_edt;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.edit_document, container, false);
            ButterKnife.bind(this, rootView);
            userInfo = store.getUserPojo(Const.USERDATA);
            setToolBar();
            setUserData();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @OnClick(R.id.submit_TV)
    void submitDocument() {


        baseActivity.AlertYesNo("Are you sure you want to Update your documents?\nAs after updating your documents you have to wait till Admin not approved your profile.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editDocument(imgefileDriving, imagefileLicence);
            }
        }, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getFragmentManager().popBackStack("back", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                });


    }


    private void editDocument(File driving, File identity) {

        if (driving != null && identity != null) {
            editDocument = apiInterfaceAuth.editMyDocument(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))),returnRequestBody(licence_num_edt.getText().toString().trim()), returnRequestFile(imgefileDriving, "driving_license"), returnRequestFile(imagefileLicence, "identification_card"));
            apiHitAndHandle.makeApiCall(editDocument, this);
        }
        if (driving == null) {
            editDocument = apiInterfaceAuth.editMyDocument(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(licence_num_edt.getText().toString().trim()),returnRequestFile(imagefileLicence, "identification_card"));
            apiHitAndHandle.makeApiCall(editDocument, this);
        } else if (identity == null) {
            editDocument = apiInterfaceAuth.editMyDocument(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(licence_num_edt.getText().toString().trim()),returnRequestFile(imgefileDriving, "driving_license"));
            apiHitAndHandle.makeApiCall(editDocument, this);
        } else {
            getFragmentManager().popBackStack();
        }

    }


    private void setUserData() {
        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getLicense())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(DL);
        Glide.with(getActivity())
                .load(userInfo.getImgUrl() + userInfo.getIdentification_card())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(IC);
        licence_num_edt.setText(userInfo.getLicense_number());

    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(android.R.color.transparent);
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.editDoc), R.mipmap.ic_back, false);
    }

    @OnClick(R.id.DL)
    void pickImage1() {
        clickFrom = 1;
        checkCameraPermission();
    }

    @OnClick(R.id.IC)
    void pickImage2() {
        clickFrom = 2;
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
                        if (clickFrom == 1) {
                            imgefileDriving = imageFile;
                            Glide.with(getActivity())
                                    .load("file://" + imageFile)
                                    .apply(new RequestOptions()
                                            .dontAnimate().
                                                    error(R.mipmap.user_img))
                                    .into(DL);

                        } else if (clickFrom == 2) {
                            imagefileLicence = imageFile;
                            Glide.with(getActivity())
                                    .load("file://" + imageFile)
                                    .apply(new RequestOptions()
                                            .dontAnimate().
                                                    error(R.mipmap.user_img))
                                    .into(IC);
                        }
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
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == editDocument) {
            Gson gson = new Gson();
            EditDocument cardList = gson.fromJson(object.toString(), EditDocument.class);
            if (cardList.getStatus() == 200) {
                userInfo = store.getUserPojo(Const.USERDATA);
                userInfo.setIdentification_card(cardList.getIdentificationCard());
                userInfo.setLicense(cardList.getDrivingLicense());
                showMessage(getContext(), "", cardList.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        baseActivity.getLogoutApi();
                    }
                });
            }

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
                        if (clickFrom == 1) {
                            imgefileDriving = imageFile;
                            Glide.with(getActivity())
                                    .load("file://" + imageFile)
                                    .apply(new RequestOptions()
                                            .dontAnimate().
                                                    error(R.mipmap.user_img))
                                    .into(DL);
                        }
                    } else if (clickFrom == 2) {
                        imagefileLicence = imageFile;
                        Glide.with(getActivity())
                                .load("file://" + imageFile)
                                .apply(new RequestOptions()
                                        .dontAnimate().
                                                error(R.mipmap.user_img))
                                .into(IC);
                    }
                }

            }, 200).crop().start();
        }
    }

    @Override
    public void permDenied(int resultCode) {

    }
}
