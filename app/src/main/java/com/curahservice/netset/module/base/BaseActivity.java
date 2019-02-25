package com.curahservice.netset.module.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.bumptech.glide.Glide;
import com.curahservice.netset.BuildConfig;
import com.curahservice.netset.R;
import com.curahservice.netset.retrofitManager.ApiClient;
import com.curahservice.netset.retrofitManager.ApiHitAndHandle;
import com.curahservice.netset.retrofitManager.ApiInterface;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.retrofitManager.NetworkUtil;
import com.curahservice.netset.retrofitManager.networkCheck;
import com.curahservice.netset.util.AppConstants;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.InternetCheckReceiver;
import com.curahservice.netset.util.PrefStore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.splunk.mint.Mint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;

public class BaseActivity extends AppCompatActivity implements AppConstants,ApiResponse {

    private static BaseActivity baseActivity;
    private ProgressDialog progressBA;
    private InternetCheckReceiver internetCheckReceiver = null;
    public String videoName = null;
    public String secretKey = null;
    private Uri uriCameraFile;
    public File imageFile;
    PrefStore store;
    ApiInterface apiInterfaceAuth;
    Call<JsonObject> logoutApi;
    public ApiHitAndHandle apiHitAndHandle;

    public BaseActivity() {
    }

    public static BaseActivity getInstance() {
        return baseActivity;
    }
    public AlertDialog networkAlertDialog;
    public NetworksBroadcast networksBroadcast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        store = new PrefStore(BaseActivity.this);
        apiInterfaceAuth = new ApiClient(store.getString("token"),"1.1").getClient().create(ApiInterface.class);
        apiHitAndHandle = ApiHitAndHandle.getInstance(BaseActivity.this);
        store = new PrefStore(BaseActivity.this);
        keyHash();
        networkDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
        initializeNetworkBroadcast();


    }

    //for intent
    public void moveToNextActivity(final Class<?> destination, Bundle extras) {
        final Intent intent = new Intent(getApplicationContext(), destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
        finish();

    }


    public void keyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }
    public void replaceFragment(@IdRes int containerViewId,
                            @NonNull Fragment fragment,
                            @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss();
    }

    public void showSnackbarCustom(View view, String message) {
        try {
            TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.RED);
            TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    //com.waysways.adapter.fcm.MyFirebaseMessagingService.java

    public void showAlertDialog(Context context, String msg, final String jump2Settings) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setCancelable(true);
            //alertDialog.setTitle("");
            //alertDialog.setIcon(android.R.drawable.ic_delete);
            //R.drawable.tick
            alertDialog.setMessage(msg);
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //dialog.cancel();
                            if (jump2Settings.equals("11")) {
                                dialog.dismiss();
                                //openSetting(ctx);
                            } else {
                                dialog.dismiss();
                                //openLocationSetting(ctx);
                            }
                        }
                    });
            alertDialog.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImageDialog(String resumeURL) {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = ImagePreviewDialogFragment.newInstance(resumeURL);
        //newFragment.setTargetFragment(HomeRecyclerAdapter.this, 300);
        newFragment.show(getSupportFragmentManager(), "");
    }


    void addTransparency(Dialog mydialog) {
        mydialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    //Button btnSubmit = dialog.findViewById(btnId);
    //dialog.dismiss();
    public Dialog showCustomDialog(Context context, @LayoutRes int layoutResID, String cancel) {
        Dialog dialog = new Dialog(context,R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResID);

        //dialog.getWindow().setLayout(275, 350);
        if (cancel.equalsIgnoreCase("no")) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
        }

        dialog.show();
        return dialog;
    }


    public Dialog showTransparentDialog(Context context, @LayoutRes int layoutResID, String cancel) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResID);
        addTransparency(dialog);
        //dialog.getWindow().setLayout(275, 350);
        if (cancel.equalsIgnoreCase("no")) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
        }
        //dialog.setTitle(title);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        //wlp.windowAnimations=R.style.PauseDialog;
        //wlp.dimAmount=0.0f;
        //wlp.setColorMode(Color.TRANSPARENT);
        //window.setGravity(Gravity.CENTER);
        wlp.copyFrom(window.getAttributes());
        //lp.gravity = Gravity.CENTER;
        //This makes the dialog take up the full width
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        dialog.show();
        return dialog;
    }

    //new ProgressDialog(ctx);
    public void progressDialogShow(ProgressDialog progress) {
        try {
            this.progressBA = progress;
            progress.setMessage("Please wait...");
            //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void progressDialogDismiss() {
        progressBA.dismiss();
    }

    //to check the network Availability
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null)
            if (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                log("NETWORKNAME: " + activeNetworkInfo.getTypeName());
                return true;
            }
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //response.errorBody()
    /*public JSONObject apiErrorBodyMsg(View view, ResponseBody json) {
        JSONObject jObjError = null;
        try {
            jObjError = new JSONObject(json.string());
            log("jObjError "+jObjError.get("message"));
            showSnackbarCustom(view, ""+jObjError.get("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jObjError;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        /*if (internetCheckReceiver==null) {
            internetCheckReceiver = new InternetCheckReceiver();
        }
        registerReceiver(internetCheckReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (internetCheckReceiver!=null) {
            unregisterReceiver(internetCheckReceiver);
        }*/
    }

    public void clearSharedPreferences() {
        try {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(SHARED_PREF,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayAdapter setSpinnerAdapter(List<String> modelList) {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_background, modelList);
        adapter.setDropDownViewResource(R.layout.spinner_item_background);        //here apply your custom layout

        return adapter;
    }
    public AlertDialog.Builder networkDialog;
    private String networkStatus;
    networkCheck check;
    String internetStatus;
    public boolean internetConnection=true;

    public class NetworksBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            internetStatus = NetworkUtil.getConnectivityStatusString(context);

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();


            if (check != null) {
                if (isConnected){
                    internetConnection=true;
                }else {
                    internetConnection=false;
                }
                check.internetStatus(isConnected);
            }
            if (!isConnected) {
                showNoNetworkDialog(internetStatus);
            }


        }
    }



    private void initializeNetworkBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        networksBroadcast = new NetworksBroadcast();
        registerReceiver(networksBroadcast, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (networksBroadcast != null) {
                unregisterReceiver(networksBroadcast);
            }
        } catch (IllegalArgumentException e) {
            networksBroadcast = null;
        }
    }
    public void showNoNetworkDialog(String status) {
        networkStatus = status;
        networkDialog.setTitle("");
        networkDialog.setMessage(status);
        networkDialog.setCancelable(false);
        networkDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isNetworkAvailable()) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }
        });
        if (networkAlertDialog != null && !networkAlertDialog.isShowing()) {
            networkAlertDialog.show();

        }else {
            if (networkAlertDialog == null) {
                networkAlertDialog = networkDialog.create();
                networkAlertDialog.setCancelable(false);
                networkAlertDialog.show();
            }
        }
    }

    public void regiterNetwork(networkCheck networkCheckis) {
        check = networkCheckis;
    }
    public String SaveImageInStorage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("FileOutputStream124 " + file.getPath());

        return file.getPath().toString();
    }

    //1 for image and 2 for video
    public void pickImageOrVideo(final Context context, final int count) {
        final Dialog dialog = showCustomDialog(context, R.layout.image_picker_dialog, "");
        TextView dialog_title_TV = dialog.findViewById(R.id.dialog_title_TV);
        if (count == 1) {
            dialog_title_TV.setText("Pick Image From");
        } else {
            dialog_title_TV.setText("Pick Video From");
        }
        TextView gallery = dialog.findViewById(R.id.open_gallery_TV);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1) {
                    Intent takePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(takePicture, RESULT_LOAD_IMAGE);
                } else {
                    Intent takeVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(takeVideo, RESULT_LOAD_VIDEO);
                }
                dialog.dismiss();
            }
        });
        TextView camera = dialog.findViewById(R.id.open_camera_TV);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    uriCameraFile = getOutputMediaFileUri(MEDIA_TYPE_CAMERA);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uriCameraFile);
                    startActivityForResult(takePicture, RESULT_LOAD_IMAGE_CAMERA);
                } else {
                    Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    uriCameraFile = getOutputMediaFileUri(MEDIA_TYPE_CAMERA);
                    takeVideo.putExtra(MediaStore.EXTRA_OUTPUT, uriCameraFile);
                    startActivityForResult(takeVideo, RESULT_LOAD_VIDEO_CAMERA);
                }
                dialog.dismiss();
            }
        });
    }

    public String onActivityResult_image(Intent data, int requestCode) {
        String picturePath = null;
        if (requestCode == RESULT_LOAD_IMAGE || requestCode == RESULT_LOAD_BOTH_IMAGE_VIDEO) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        } else if (requestCode == RESULT_LOAD_IMAGE_CAMERA) {
            //imageview.setImageURI(selectedImage);
            picturePath = uriCameraFile.getPath();
            //picturePath = compressImage(this, picturePath);
        }
        return picturePath;
    }

    //Creating file uri to store image/video
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // returning image / video
    public File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                log("Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_CAMERA) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_GALLERY) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    // File finalFile = new File(getRealPathFromURI(uri));
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    //call within try, catch()
    public String onActivityResult_video(Intent data, int requestCode) {
        String filePath = null;
        if (requestCode == RESULT_LOAD_VIDEO || requestCode == RESULT_LOAD_BOTH_IMAGE_VIDEO) {
            Uri selectedVieo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVieo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

        } else if (requestCode == RESULT_LOAD_VIDEO_CAMERA) {
            filePath = uriCameraFile.getPath();
        }

        if (filePath != null) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.parse(filePath));
            int duration = mp.getDuration();
            mp.release();

            //60 seconds video length
            if ((duration / 1000) >= 60) {
                // Show Your MessagesFragment
                showSnackbarCustom(findViewById(android.R.id.content), "Invalid Video");
                log("Invalid video Size ");
                filePath = null;
                return filePath;
            } else {
                //runVideo(filePath);
                log("filePath234 " + filePath);
                videoName = getFileNameFromUrl(filePath);
                return filePath;
            }
        }
        return null;
    }

    public String getFileNameFromUrl(String path) {
        String[] pathArray = path.split("/");
        return pathArray[pathArray.length - 1];
    }

    public void pickBoth_ImageVideoFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, RESULT_LOAD_BOTH_IMAGE_VIDEO);
    }

    public StringBuilder getData(int[] abc) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < abc.length; i++) {
            if (i == abc.length - 1) {
                builder.append(abc[i]);
            } else {
                builder.append(abc[i] + ",");
            }
        }
        return builder;
    }

    public boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Enter your password";
        } else {
            if (password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*") && password.matches("[A-Za-z0-9]*")) {
                return "valid";
            } else if (password.matches(".*[@#$%^&+=].*")) {
                return "valid";
            } else if (password.matches(".*(?=\\\\S+$).*")) {
                return "Invalid Password";
            } else {
                return "Choose alpha-numeric password";
            }
        }
    }

    //[{"id":"4"}]
    //getJsonFromString("2,3,7"));
    //id        image
    public JSONArray getJsonFromString(String id, String val) {
        //JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String[] a = val.split(",");
        for (int i = 0; i < a.length; i++) {
            try {
                //jsonObject.put("id", a[i]);
                jsonArray.put(new JSONObject().put(id, a[i]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public String convertPojo2Json(Object src) {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        String data = gson.toJson(src);
        try {
            jsonArray.put(new JSONObject(data));
            log("json_body35 " + jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public Bitmap getVideoThumbNail(String videoFile) {
        /*ContentResolver crThumb = getContentResolver();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videoId, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        //iv.setImageBitmap(curThumb);*/
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videoFile, MediaStore.Video.Thumbnails.MINI_KIND);
        return bMap;
    }

    /*public RequestBody getRequestBody(String content) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE_PARSE), content);
    }

    public RequestBody getFileRequestBody(File filePath) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), filePath);
    }*/

    public String getTimeZone() {
        return TimeZone.getDefault().getID();
    }

    // false means ""
   /* public void previewImage(String loadImage, ImageView view, String crop_path) {
        if (crop_path.contains("crop")) {
            Glide.with(getApplicationContext()).load(baseURL + loadImage).into(view);
        } else if (crop_path.contains("path")) {
            Glide.with(getApplicationContext()).load(new File(loadImage)).into(view);
        } else {
            Glide.with(getApplicationContext()).load(baseURL + loadImage).into(view);
        }
    }*/

    public void log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("curahLog", msg);
        }
    }

    public String convertImageToBase64String(String pathName) {
        log("pathname34 " + pathName);
        Bitmap bm = BitmapFactory.decodeFile(pathName);
        //Bitmap bm = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }



    AlertDialog alertDialog;

    public void AlertYesNo(String message, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener noOnClick) {
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog = null;
        }
        if (alertDialog == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("yes",
                    onClickListener);

            alertDialogBuilder.setNegativeButton("No", noOnClick);
            alertDialog = alertDialogBuilder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }



    public void getLogoutApi() {
        logoutApi = apiInterfaceAuth.logoutApi(String.valueOf(store.getInt("userId")));
        apiHitAndHandle.makeApiCall(logoutApi, this);
    }



    @Override
    public void onSuccess(Call call, Object object) {
        Log.e("------Logout----",object.toString());
        if (call==logoutApi){
            store.setBoolean(Const.LOGIN, false);
            store.setBoolean(Const.SOCIAL_LOGIN, false);
            finish();
            startActivity(new Intent(getApplicationContext(), IndexActivity.class)
                    .putExtra("from", "logout")
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {

    }
}