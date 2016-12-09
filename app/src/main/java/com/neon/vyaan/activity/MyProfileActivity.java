package com.neon.vyaan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.ProductsModel;
import com.neon.vyaan.model.ProfileModel;
import com.neon.vyaan.network.BaseVolleyRequest;
import com.neon.vyaan.utils.Helper;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;
import com.neon.vyaan.widgets.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProfileActivity extends BaseActivity {

    LinearLayout containerImage;
    CircularImageView circularProfileImage;
    EditText editName;
    EditText editEmail;
    EditText editPhoneNo;
    Button buttonSave;
    TextView textChangePassword;

    ProfileModel profileModel;

    String userProfileUrl;


    Bitmap bitmapProfileImage;

    int bytesAvailable;
    int bytesRead;
    String mimeType;

    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_my_profile));

        containerImage = (LinearLayout) findViewById(R.id.containerImage);
        circularProfileImage = (CircularImageView) findViewById(R.id.circularProfileImage);
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        textChangePassword = (TextView) findViewById(R.id.textChangePassword);

        getMyProfile();


        userProfileUrl = SharedPreferenceUtils.getInstance(currentActivity).getString(USER_IMAGE);

        if (userProfileUrl != null && !userProfileUrl.equals("")) {
            Picasso.with(currentActivity).load(BASE_URL_IMAGES + "/" + userProfileUrl).into(circularProfileImage);


            Picasso.with(currentActivity).load(BASE_URL_IMAGES + "/" + userProfileUrl).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    containerImage.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }


                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });

        }
    }

    @Override
    protected void initContext() {
        currentActivity = MyProfileActivity.this;
        context = MyProfileActivity.this;
    }

    @Override
    protected void initListners() {
        buttonSave.setOnClickListener(this);
        textChangePassword.setOnClickListener(this);
        circularProfileImage.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    public void onAlertClicked(int alertType) {
        switch (alertType) {
            case ALERT_TYPE_IMAGE_UPLOAD: {
                uploadImageByVolley();
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSave: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        updateUserProfile();
                    }
                } else {

                }

                break;
            }

            case R.id.textChangePassword: {

                startActivity(currentActivity, ChangePasswordActivity.class, bundle, true, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);

                break;
            }

            case R.id.circularProfileImage: {
                showFileChooser();
            }
        }

    }


    private boolean isMandatoryFields() {

        editName.setError(null);
        editPhoneNo.setError(null);
        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.error_enter_name));
            return false;
        } else if (!Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()).equals("")) {
            editPhoneNo.setError(Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()));
            return false;
        }

        return true;
    }


    public void getMyProfile() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;
        String urlGetProfile = URL_GET_PROFILE + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGetProfile, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("user profile response", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        // productsModelList.clear();
                        Gson gson = new Gson();
                        profileModel = gson.fromJson(response.getJSONObject(MESSAGE).toString(), ProfileModel.class);
                        initProfileViews();

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("user profile error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("user profile json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                //  ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorLogin), true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");


                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }

    private void initProfileViews() {
        editEmail.setText(profileModel.getEmail());
        editName.setText(profileModel.getName());
        editPhoneNo.setText("" + profileModel.getPhoneNumber());
    }


    public void updateUserProfile() {


        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);
        profileModel.setUserId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));

        String json = new Gson().toJson(profileModel);
        logTesting("profile update request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UPDATE_USER_PROFILE, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("user profile update response", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        // productsModelList.clear();
                        toast(getResources().getString(R.string.label_profile_updated), true);
                        Gson gson = new Gson();
                        profileModel = gson.fromJson(response.getJSONObject(MESSAGE).toString(), ProfileModel.class);
                        initProfileViews();

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("user profile update error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("user profile update json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                //  ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorLogin), true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");


                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstants.REQUEST_TAG_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAG_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmapProfileImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                circularProfileImage.setImageBitmap(bitmapProfileImage);
                containerImage.setBackground(new BitmapDrawable(context.getResources(), bitmapProfileImage));
                alert(currentActivity, getResources().getString(R.string.alert_message_upload_image), getResources().getString(R.string.alert_message_upload_image), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_IMAGE_UPLOAD);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAG_Image_Capture && resultCode == RESULT_OK) {
            bitmapProfileImage = (Bitmap) data.getExtras().get("data");
            circularProfileImage.setImageBitmap(bitmapProfileImage);
            containerImage.setBackground(new BitmapDrawable(context.getResources(), bitmapProfileImage));


        }
    }

    public void uploadImageByVolley() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        final String lineEnd = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";


        final int maxBufferSize = 1 * 1024 * 1024;
        final byte[] meetupImageBytesArray = Helper.getImageBytes(((BitmapDrawable) circularProfileImage.getDrawable()).getBitmap());
        Long timeMillis = System.currentTimeMillis();
        final String fileName = "profileImage_" + timeMillis.toString() + ".jpg";


        String imageUploadUrl = UPLOAD_PROFILE_IMAGE;
        BaseVolleyRequest uploadProfileImageRequest = new BaseVolleyRequest(1, imageUploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    Log.e("image response  is", response.toString() + jsonString);
                    if (bundle == null) {
                        bundle = new Bundle();

                    }
                    updateImageUrl(fileName);
                    //addNews();

                } catch (UnsupportedEncodingException e) {
                    cancelProgressDialog();
                    e.printStackTrace();
                }
                Log.e("image response  is", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("error uploading image", true);
                Log.e("image upload error is", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                mimeType = "multipart/form-data;boundary=" + boundary;
                return mimeType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);


                try {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    ByteArrayInputStream fileInputStream = new ByteArrayInputStream(meetupImageBytesArray);
                    bytesAvailable = fileInputStream.available();

                    int bufferSize = Math.min(meetupImageBytesArray.length, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }


                    //    dos.write(meetupImageBytesArray, 0, bufferSize);
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return meetupImageBytesArray;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Long timeMillis = System.currentTimeMillis();
                Map<String, String> params = new HashMap<String, String>();
                params.put("Connection", "Keep-Alive");
                params.put("ENCTYPE", "multipart/form-data");
                params.put("accept", "application/json");
                params.put("uploaded_file", fileName);
                params.put("Content-Type", "multipart/form-data;boundary=" + boundary);
                return params;

            }

        };

        VyaanApplication.getInstance().addToRequestQueue(uploadProfileImageRequest);


    }


    private void updateImageUrl(final String imageName) {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonUpdateUser = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonUpdateUser = new JSONObject();
            jsonUpdateUser.put("user_id", SharedPreferenceUtils.getInstance(currentActivity).getInteger(KEY_USER_ID));
            jsonUpdateUser.put("image_url", imageName);


            Log.e("json profUpdate request", jsonUpdateUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_PROFILE_PIC = UPLOAD_PROFILE_PIC_NAME;
        JsonObjectRequest profileUpdateRequest = new JsonObjectRequest(Request.Method.POST, URL_PROFILE_PIC, jsonUpdateUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.ntwk_response_profile_updation), response.toString(), Log.ERROR);
                    String message = response.getString(MESSAGE);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        SharedPreferenceUtils.getInstance(currentActivity).putString(USER_IMAGE, imageName);
                        toast(getResources().getString(R.string.message_image_updated), true);
                    } else {
                        cancelProgressDialog();
                        logTesting("image url update error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                toast(getResources().getString(R.string.error_updating_profile_url), true);
                logTesting(getResources().getString(R.string.error_updating_profile_url), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(profileUpdateRequest);
    }


}
