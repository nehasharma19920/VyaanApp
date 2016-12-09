package com.neon.vyaan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.ProfileModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {

    EditText editOldPassword;
    EditText editNewPassword;
    EditText editConfirmPassword;
    Button buttonChangePassword;


    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_change_password));
        editOldPassword = (EditText) findViewById(R.id.editOldPassword);
        editNewPassword = (EditText) findViewById(R.id.editNewPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        buttonChangePassword = (Button) findViewById(R.id.buttonChangePassword);
    }

    @Override
    protected void initContext() {
        currentActivity = ChangePasswordActivity.this;
        context = ChangePasswordActivity.this;
    }

    @Override
    protected void initListners() {
        buttonChangePassword.setOnClickListener(this);
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
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonChangePassword: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        updatePassword();
                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }

                break;
            }
        }

    }

    private boolean isMandatoryFields() {
        editOldPassword.setError(null);
        editNewPassword.setError(null);
        editConfirmPassword.setError(null);

        if (editOldPassword.getText().toString().isEmpty()) {
            editOldPassword.setError(getResources().getString(R.string.error_empty_old_password));
            editOldPassword.requestFocus();
            return false;
        } else if (editNewPassword.getText().toString().isEmpty()) {
            editNewPassword.setError(getResources().getString(R.string.error_empty_new_password));
            editNewPassword.requestFocus();
            return false;
        } else if (editConfirmPassword.getText().toString().isEmpty()) {
            editConfirmPassword.setError(getResources().getString(R.string.error_empty_confirm_password));
            editConfirmPassword.requestFocus();
            return false;
        } else if (!editNewPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
            editConfirmPassword.setError(getResources().getString(R.string.error_confirm_password_not_matches));
            editConfirmPassword.requestFocus();
            return false;
        }


        return true;
    }

    private void updatePassword() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonUpdateUser = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonUpdateUser = new JSONObject();
            jsonUpdateUser.put("user_id", SharedPreferenceUtils.getInstance(currentActivity).getInteger(KEY_USER_ID));
            jsonUpdateUser.put("old_pwd", editOldPassword.getText().toString());
            jsonUpdateUser.put("new_pwd", editNewPassword.getText().toString());


            Log.e("json passUpdate request", jsonUpdateUser.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest profileUpdateRequest = new JsonObjectRequest(Request.Method.POST, URL_CHANGE_PASSWORD, jsonUpdateUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    logTesting(getResources().getString(R.string.ntwk_response_profile_updation), response.toString(), Log.ERROR);
                    String message = response.getString(MESSAGE);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                        toast(getResources().getString(R.string.message_password_updated), true);
                    } else {
                        cancelProgressDialog();
                        logTesting("passwd update update error", "true", Log.ERROR);
                        if (response.get(MESSAGE).equals(INVALID_OLD_PWD)) {
                            editOldPassword.setError(getResources().getString(R.string.error_incorrect_old_password));
                            editOldPassword.requestFocus();

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                //   toast(getResources().getString(R.string.error_updating_password), true);
                logTesting(getResources().getString(R.string.error_updating_password), error.toString(), Log.ERROR);

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
