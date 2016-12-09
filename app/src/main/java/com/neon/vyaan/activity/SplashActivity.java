package com.neon.vyaan.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.service.RegistrationIntentService;
import com.neon.vyaan.utils.Helper;
import com.neon.vyaan.utils.LoginUtils;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    @Override
    protected void initViews() {

    }

    @Override
    protected void initContext() {
        currentActivity = SplashActivity.this;
        context = SplashActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return false;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (LoginUtils.isLogin(context)) {

                        updateGcmToken();

                        startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        finish();
                    } else {
                        startActivity(currentActivity, AppAccessActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);
                        finish();
                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }
            }
        }, SPLASH_TIME);


    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

    }


    public void updateGcmToken() {


        JSONObject jsons = null;
        try {
            jsons = new JSONObject();
            jsons.put("gcm_token", SharedPreferenceUtils.getInstance(currentActivity).getString(GCM_TOKEN));
            jsons.put("user_id", SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));

            logTesting("gcm token request json", jsons.toString(), Log.ERROR);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UPDATE_GCM_TOKEN, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull update gcm token", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                    } else {

                        logTesting("gcm token update error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("gcm token json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorUpdateGcm), true);

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


}
