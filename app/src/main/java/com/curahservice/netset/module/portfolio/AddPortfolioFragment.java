package com.curahservice.netset.module.portfolio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.PortFolioGson;
import com.curahservice.netset.gsonModel.ReviewModel;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddPortfolioFragment extends BaseFragment implements BaseFragment.PermCallback {

    File imageFile,thumbFile;
    @BindView(R.id.pick_CIV)
    ImageView pick_CIV;
    retrofit2.Call<JsonObject> addPortfolio;

    @BindView(R.id.ic_play)
    ImageView ic_play;

    @BindView(R.id.title_ET)
    AppCompatEditText title_ET;

    @BindView(R.id.enter_description_ET)
    AppCompatEditText enter_description_ET;

    @BindView(R.id.videoView)
    VideoView videoView;

    private String type;
    PortFolioGson.Porfolio portfolio;
    String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getTag().equals("editPortfolio")){

            setToolBar(getString(R.string.editPortFolio));
            portfolio = (PortFolioGson.Porfolio) getArguments().getSerializable("portFolio");
            url = getArguments().getString("url");
            title_ET.setText(portfolio.getTitle());
            enter_description_ET.setText(portfolio.getDescription());

            try {
                type=portfolio.getType();
                if (portfolio.getType().equals("I")) {
                    Glide.with(getActivity())
                            .load(url + portfolio.getFile())
                            .apply(new RequestOptions()
                                    .dontAnimate().
                                            error(R.mipmap.user_img))
                            .into(pick_CIV);
                }else {
                    ic_play.setVisibility(View.VISIBLE);
                    long interval = 6 * 1000;
                    RequestOptions options = new RequestOptions().frame(interval);
                    Glide.with(getActivity()).asBitmap()
                            .load(url+portfolio.getFile())
                            .apply(options)
                            .into(pick_CIV);

                }


            }catch (Exception e){

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }else {
            setToolBar(getString(R.string.addPortfolio));
        }
    }

    private void setToolBar(String title) {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(android.R.color.transparent);
        ((DrawerActivity) getActivity()).setToolbarAttributes(title, 0, false);
        ((DrawerActivity) getActivity()).skipText(View.VISIBLE, "Close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }


    public void addPortFolio(String title, String des,String type) {

        if (type.equals("I")){
            addPortfolio = apiInterfaceAuth.addPortfolio(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type), returnRequestFile(imageFile, "file"));

        }else {
            addPortfolio = apiInterfaceAuth.addPortfolio(returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type), returnRequestFile(imageFile, "file"),returnRequestFile(thumbFile, "video_thumb"));

        }
        apiHitAndHandle.makeApiCall(addPortfolio, this);
    }

    public void editPortFolio(String portfolioId,String title, String des,String type) {

        if (type.equals("I")){
            if (imageFile!=null){
                addPortfolio = apiInterfaceAuth.editPortfolio(returnRequestBody(portfolioId),returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type), returnRequestFile(imageFile, "file"));
            }else {
                addPortfolio = apiInterfaceAuth.editPortfolio(returnRequestBody(portfolioId),returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type));
            }
        }else {
            if (imageFile!=null){
                addPortfolio = apiInterfaceAuth.editPortfolio(returnRequestBody(portfolioId),returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type), returnRequestFile(imageFile, "file"),returnRequestFile(thumbFile, "video_thumb"));
            }else {
                addPortfolio = apiInterfaceAuth.editPortfolio(returnRequestBody(portfolioId),returnRequestBody(String.valueOf(store.getInt(Const.USER_ID))), returnRequestBody(title), returnRequestBody(des),returnRequestBody(type));
            }
        }

        apiHitAndHandle.makeApiCall(addPortfolio, this);
    }
    @OnClick(R.id.submit_TV)
    void submitClick() {

        if (title_ET.getText().toString().trim().length() == 0) {
            title_ET.setError("Please add title");
            title_ET.requestFocus();
        } else if (enter_description_ET.getText().toString().trim().length() == 0) {
            enter_description_ET.setError("Please add description");
            enter_description_ET.requestFocus();
        } else {

            if (getTag().equals("editPortfolio")){
                editPortFolio(portfolio.getId()+"",title_ET.getText().toString().trim(), enter_description_ET.getText().toString().trim(),type);

            }else {
                 if (imageFile == null) {
                    Toast.makeText(getActivity(), "Add Video/Image to post on your portfolio", Toast.LENGTH_LONG).show();
                } else {
                     addPortFolio(title_ET.getText().toString().trim(), enter_description_ET.getText().toString().trim(),type);

                 }
            }
        }
    }


    @Override
    public void onSuccess(retrofit2.Call call, Object object) {
        super.onSuccess(call, object);
        try {
            JSONObject result = new JSONObject(object.toString());
            if (result.getInt(Const.STATUS) == 200) {
                title_ET.setText("");
                enter_description_ET.setText("");
                ic_play.setVisibility(View.INVISIBLE);
                imageFile=null;
                pick_CIV.setImageResource(R.mipmap.ic_camera);
                Toast.makeText(getActivity(), result.getString(Const.MESSAGE), Toast.LENGTH_LONG).show();
            } else {
                showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.pick_CIV)
    void pickImage() {
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
                        videoView.setVisibility(View.INVISIBLE);
                        pick_CIV.setVisibility(View.VISIBLE);
                        if (isImageFile(imageFile.getAbsolutePath())) {
                            type="I";
                            ic_play.setVisibility(View.INVISIBLE);
                            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            pick_CIV.setImageBitmap(myBitmap);
                        } else {
                            type="V";
                            ic_play.setVisibility(View.VISIBLE);
                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            pick_CIV.setImageBitmap(bMap);
                            thumbFile = new File(getActivity().getCacheDir(), "thumb.png");
                            try {

                                thumbFile.createNewFile();
                                OutputStream os = new BufferedOutputStream(new FileOutputStream(thumbFile));
                                bMap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                os.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }, 200, true).crop().start();
        }
    }


    private void playVideo(Uri uri) {
        videoView.setVideoURI(uri);
        videoView
                .start();
    }

    public boolean isImageFile(String path) {

        String mimeType = URLConnection.guessContentTypeFromName(path);

        if (path.contains("Picture")) {
            return true;
        } else {
            return mimeType != null && mimeType.startsWith("image");
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



    @Override
    public void permGranted(int resultCode) {
        Log.e("code", "camera----" + resultCode);
        if (resultCode == 99) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {
                    imageFile = new File(imagePath);
                    if (imageFile.exists()) {

                        videoView.setVisibility(View.INVISIBLE);
                        pick_CIV.setVisibility(View.VISIBLE);

                        if (isImageFile(imageFile.getAbsolutePath())) {
                            type="I";
                            ic_play.setVisibility(View.INVISIBLE);
                            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            pick_CIV.setImageBitmap(myBitmap);
                        } else {
                            type="V";
                            ic_play.setVisibility(View.VISIBLE);
                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            pick_CIV.setImageBitmap(bMap);
                            thumbFile = new File(getActivity().getCacheDir(), "thumb.png");
                            try {

                                thumbFile.createNewFile();
                                OutputStream os = new BufferedOutputStream(new FileOutputStream(thumbFile));
                                bMap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                os.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //playVideo(Uri.fromFile(imageFile));
                        }

                    }
                }
            }, 200, true).crop().start();
        }
    }

    @Override
    public void permDenied(int resultCode) {

    }
}
