package com.neon.vyaan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.SignUpModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity {

    EditText editName;
    EditText editEmail;
    EditText editPassword;
    EditText editPhoneNo;

    Button buttonSignUp;
    TextView textLogin;

    SignUpModel signUpModel;


    @Override
    protected void initViews() {

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textLogin = (TextView) findViewById(R.id.textLogin);

    }

    @Override
    protected void initContext() {
        currentActivity = SignUpActivity.this;
        context = SignUpActivity.this;
    }

    @Override
    protected void initListners() {
        buttonSignUp.setOnClickListener(this);
        textLogin.setOnClickListener(this);
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
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle(R.string.title_activity_signup);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSignUp: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        toHideKeyboard();
                        initSignupModel();
                        toSignup();
                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }
                break;
            }

            case R.id.textLogin: {
                startActivity(currentActivity, LoginActivity.class, bundle, true, REQUEST_TAG_SIGN_IN_ACTIVITY, true, ANIMATION_SLIDE_UP);
                finish();
                break;
            }
        }
    }


    private void initSignupModel() {
        signUpModel = new SignUpModel();

        signUpModel.setEmailId(editEmail.getText().toString());
        signUpModel.setName(editName.getText().toString());
        signUpModel.setPassword(editPassword.getText().toString());
        //  signUpModel.setPhoneNumber(editPhoneNo.getText().toString());

    }


    private boolean isMandatoryFields() {
        editName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        editPhoneNo.setError(null);

        if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.emptyName));
            editName.requestFocus();
            return false;
        } else if (!Validator.isValidEmail(context, editEmail.getText().toString()).equals("")) {

            editEmail.requestFocus();
            editEmail.setError(Validator.isValidEmail(context, editEmail.getText().toString()));
            return false;
        } else if (editPassword.getText().toString().isEmpty()) {
            editPassword.setError(getResources().getString(R.string.emptyPassword));
            editPassword.requestFocus();
            return false;
        } /*else if (!Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()).equals("")) {
            editPhoneNo.setError(Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()));
            editPhoneNo.requestFocus();
            return false;
        }*/
        return true;

    }

    public void toSignup() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        String json = new Gson().toJson(signUpModel);
        logTesting("signup request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull signup", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(AppConstants.KEY_IS_USER_LOGIN, true);
                        SharedPreferenceUtils.getInstance(currentActivity).putInteger(AppConstants.KEY_USER_ID, response.getInt(KEY_USER_ID));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_NAME, signUpModel.getName());
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_EMAIL, signUpModel.getEmailId());


                        setResult(RESULT_OK);
                        finish();
                    } else {
                        cancelProgressDialog();
                        alert(currentActivity, getString(R.string.error_signup), getString(R.string.alert_email_registered), getString(R.string.labelOk), getString(R.string.labelCancel), false, true, ALERT_TYPE_NO_NETWORK);


                        logTesting("signup error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("signup json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorLogin), true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("type", "M");

                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }


}
