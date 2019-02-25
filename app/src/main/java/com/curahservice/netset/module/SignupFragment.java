package com.curahservice.netset.module;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curahservice.netset.MyFirebaseInstanceIDService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.module.addServices.AddServicesFragment;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.socialIntegration.FacebookLogin;
import com.curahservice.netset.socialIntegration.GPlusLoginActivity;
import com.curahservice.netset.socialIntegration.SocialLogin;
import com.curahservice.netset.util.Const;
import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;

public class SignupFragment extends BaseFragment implements View.OnClickListener, SocialLogin.SocialLoginListener {
    TextView terms_service;
    TextView login_text;
    Button signup_button;
    TextInputLayout confirmPassIL, pass_IL, email_IL;
    CheckBox termServices;
    ImageView facebookLogin, gmailLogin;
    AppCompatEditText email_signup, pass_signup, confirm_pass;
    String userFname = "", userLname, userImage, userEmail, userFBId = "", socialId = "";
    private Call<JsonObject> createAccountapi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup, container, false);
    }

    private class URLSpanline_none extends URLSpan {
        public URLSpanline_none(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup_button = view.findViewById(R.id.signup_button);
        terms_service = view.findViewById(R.id.terms_service);
        //  terms_service.setText(Html.fromHtml("<medium> I agree to the Curah </medium> <b> Terms of Service  </b> <br><medium> and </medium><b>Privacy Policy</b>"));

        terms_service = view.findViewById(R.id.terms_service);
        terms_service.setClickable(true);
        terms_service.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable s = (Spannable) terms_service.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanline_none(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        terms_service.setText(s);

        email_signup = view.findViewById(R.id.email_signup);
        pass_signup = view.findViewById(R.id.pass_signup);
        confirm_pass = view.findViewById(R.id.confirm_pass);
        termServices = view.findViewById(R.id.termServices);
        gmailLogin = view.findViewById(R.id.gmailLogin);
        facebookLogin = view.findViewById(R.id.facebookLogin);
        gmailLogin.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        login_text = view.findViewById(R.id.login_text);
        login_text.setText(Html.fromHtml("<u>LOGIN NOW</u>"));
        signup_button.setOnClickListener(this);
        login_text.setOnClickListener(this);
        setToolbarTitle();
        confirmPassIL = view.findViewById(R.id.confirmPassIL);
        pass_IL = view.findViewById(R.id.pass_IL);
        email_IL = view.findViewById(R.id.email_IL);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.aeh_regular);
        email_IL.setTypeface(typeface);
        pass_IL.setTypeface(typeface);
        confirmPassIL.setTypeface(typeface);
        pass_signup.setFilters(new InputFilter[]{filter});
        confirm_pass.setFilters(new InputFilter[]{filter});

    }


    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundColor(Color.TRANSPARENT);
            myactivity.setTitle("Sign Up", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            }, 0);
        }
    }


    private void checkCameraPermissionFacebook(final boolean facebook) {
        String[] arrayPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE

        };
        if (checkPermissions(arrayPermission, 120, new PermCallback() {
            @Override
            public void permGranted(int resultCode) {
                if (facebook) {
                    SocialLogin.getInstance().setListener(SignupFragment.this);
                    startActivity(new Intent(getContext(), FacebookLogin.class));
                } else {
                    SocialLogin.getInstance().setListener(SignupFragment.this);
                    startActivity(new Intent(getContext(), GPlusLoginActivity.class));
                }
            }

            @Override
            public void permDenied(int resultCode) {

            }
        })) {
            if (facebook) {
                SocialLogin.getInstance().setListener(SignupFragment.this);
                startActivity(new Intent(getContext(), FacebookLogin.class));
            } else {
                SocialLogin.getInstance().setListener(SignupFragment.this);
                startActivity(new Intent(getContext(), GPlusLoginActivity.class));
            }
        }
    }


    private void checkLocation() {
        String[] arrayPermission = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE

        };
        if (checkPermissions(arrayPermission, 99, new PermCallback() {
            @Override
            public void permGranted(int resultCode) {
                checkValidation();
            }

            @Override
            public void permDenied(int resultCode) {

            }
        })) {
            checkValidation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:

                checkLocation();
                break;
            case R.id.login_text:
                replaceFragmentNull(R.id.container, new SigninFragment(), "");
                break;
            case R.id.facebookLogin:
                checkCameraPermissionFacebook(true);
                break;
            case R.id.gmailLogin:
                checkCameraPermissionFacebook(false);
                break;
        }
    }

    private void checkValidation() {
        if (email_signup.getText().toString().trim().length() == 0) {
            email_signup.setError("Please enter email address.");
            email_signup.requestFocus();
        } else if (!isValidMail(email_signup.getText().toString().trim())) {
            email_signup.setError("Please enter valid email address.");
            email_signup.requestFocus();
        } else if (pass_signup.getText().toString().trim().length() == 0) {
            pass_signup.setError("Please enter password.");
            pass_signup.requestFocus();
        } else if (pass_signup.getText().toString().length() < 8) {
            pass_signup.setError("Password should be at-least 8 characters long.");
            pass_signup.requestFocus();
        } else if (!confirm_pass.getText().toString().trim().equals(pass_signup.getText().toString().trim())) {
            confirm_pass.setError("Confirm Password does not match.");
            confirm_pass.setText("");
            confirm_pass.requestFocus();
        } else if (!termServices.isChecked()) {
            Toast.makeText(getActivity(), "Please agree Terms of service and Privacy Policy.", Toast.LENGTH_LONG).show();
        } else {

            createAccountSocial(email_signup.getText().toString().trim(), pass_signup.getText().toString().trim(), "O", "");

        }
    }


    public static Bitmap getImageUrl(String userImage) throws IOException {
        URL imageURL = new URL(userImage);
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        return bitmap;
    }


    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        return bitmap;
    }

    private File getImageFile(Bitmap thumbnail) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(getActivity().getCacheDir(), "UserFb.jpg");
        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destination;
    }

    @Override
    public void onFacebookLoginDone(AccessToken accessToken, String token, JSONObject object, GraphResponse response) {


        //{Response:  responseCode: 200, graphObject: {"id":"958864900983482","first_name":"Netset","last_name":"Devops","email":"vp757327@gmail.com","picture":{"data":{"height":50,"is_silhouette":false,"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=958864900983482&height=50&width=50&ext=1538109177&hash=AeSLF9wEte7JxgtJ","width":50}}}, error: null}

        try {
            String image_url = object.optJSONObject("picture").getJSONObject("data").optString("url");
            userFname = object.getString("first_name");
            userLname = object.getString("last_name");
            socialId = object.getString("id");
            userEmail = object.getString("email");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Do network action in this function
                    try {
                        Bitmap bitmap = getFacebookProfilePicture(socialId);
                        File file = getImageFile(bitmap);
                        userImage = file.getAbsolutePath();
                        /*userEmail="ikramaroc@gmail.com";
                        socialId="116487695334267015580";*/
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createAccountSocial(userEmail, "", "F", socialId);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //new imageDownload(getContext(), "F").execute(bitmap);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGPlusLoginDone(GoogleSignInAccount acct, Task<GoogleSignInAccount> currentPerson, String idToken) {
        Uri personPhoto = acct.getPhotoUrl();
        userImage = null;
        userFname = "";
        userLname = "";
        try {
            Log.e("---------------", acct.getPhotoUrl() + "");
            if (acct.getGivenName() != null && !acct.getGivenName().equals("null")) {
                userFname = acct.getGivenName();
            }
            if (acct.getFamilyName() != null && !acct.getFamilyName().equals("null")) {
                userLname = acct.getFamilyName();
            }
            if (acct.getEmail() != null && !acct.getEmail().equals("null")) {
                userEmail = acct.getEmail();
            }
            socialId = acct.getId();
            if (personPhoto != null) {
                new imageDownload(getContext(), "G").execute(acct.getPhotoUrl().toString());
            } else {
                createAccountSocial(acct.getEmail(), "", "G", socialId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAccountSocial(String email, String password, String socialRegisterType, String socialId) {
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            createAccountapi = apiInterface.createAccount(email.toLowerCase(), password, password, socialRegisterType, FirebaseInstanceId.getInstance().getToken(), Const.DEVICE_TYPE, Const.USER_TYPE, socialId, store.getString(getString(R.string.latitude)), store.getString(getString(R.string.longitude)));
            apiHitAndHandle.makeApiCall(createAccountapi, this);

        } else {
            MyFirebaseInstanceIDService myFireBaseInstanceId = new MyFirebaseInstanceIDService();
            myFireBaseInstanceId.onTokenRefresh();
        }

    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        Gson gson = new Gson();
        createAccount userProfile = gson.fromJson(object.toString(), createAccount.class);
        if (userProfile.getStatus() == 200) {
            store.saveString("token", userProfile.getUserInfo().getToken());
            store.setInt(Const.USER_ID, userProfile.getUserInfo().getId());
            if (userProfile.getUserInfo().getFirstname().length() > 0) {
                store.saveUserPojo(Const.USERDATA, userProfile.getUserInfo());
                if (userProfile.getServices() == null) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    replaceFragment(R.id.container, new AddServicesFragment(), "AddServicesFragment", null);
                } else if (userProfile.getServices().size() == 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    replaceFragment(R.id.container, new AddServicesFragment(), "AddServicesFragment", null);
                } else {
                    store.setBoolean(Const.LOGIN, true);
                    store.setBoolean(Const.SOCIAL_LOGIN, true);
                    store.setSerVices(Const.SERVICES, userProfile.getServices());
                    startActivity(new Intent(getActivity(), DrawerActivity.class));
                    getActivity().finish();
                }
            } else {
                getFragmentManager().popBackStack();
                Fragment create = new CreateProfileFragment();
                Bundle b = new Bundle();
                if (userFname.length() > 0) {
                    b.putString("from", "CreateSocial");
                    b.putString("image", userImage);
                    b.putString("fname", userFname);
                    b.putString("lname", userLname);
                } else {
                    b.putString("from", "Create");
                }
                replaceFragment(R.id.container, create, "", b);
            }
        } else if (userProfile.getStatus() == 406) {
            pendingAdminApproval();
        } else {
            userImage = null;
            userFname = "";
            userLname = "";
            showMessage(getActivity(), "", userProfile.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {
        super.onError(call, errorMessage, apiResponse);
    }


    class imageDownload extends AsyncTask<String, Integer, Bitmap> {
        Context context;

        Bitmap bitmap;
        InputStream in = null;
        int responseCode = -1;

        String SocialAccountIs;

        //constructor.
        public imageDownload(Context context, String SocialAccount) {
            this.context = context;
            this.SocialAccountIs = SocialAccount;

        }


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Bitmap doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL(params[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap data) {
            try {

                File file = getImageFile(bitmap);
                userImage = file.getAbsolutePath();
                createAccountSocial(userEmail, "", SocialAccountIs, socialId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

