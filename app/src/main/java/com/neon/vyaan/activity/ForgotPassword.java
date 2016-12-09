package com.neon.vyaan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.model.ForgotPasswordModel;
import com.neon.vyaan.utils.Helper;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ForgotPassword extends BaseActivity {

    LinearLayout containerPasswordSended;

    EditText editEmail;
    Button buttonSendNewPassword;

    @Override
    protected void initViews() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        buttonSendNewPassword = (Button) findViewById(R.id.buttonSendNewPassword);
        containerPasswordSended = (LinearLayout) findViewById(R.id.containerPasswordSended);
    }

    @Override
    protected void initContext() {
        currentActivity = ForgotPassword.this;
        context = ForgotPassword.this;
    }

    @Override
    protected void initListners() {
        buttonSendNewPassword.setOnClickListener(this);
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
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.buttonSendNewPassword: {
                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        resetPasswordAndSendMail();
                    }
                } else {
                    alert(ForgotPassword.this, "", getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.cancel), false, false, ALERT_TYPE_NO_NETWORK);
                }
                break;
            }
        }
    }


    private boolean isMandatoryFields() {
        editEmail.setError(null);

        if (editEmail.getText().toString().isEmpty()) {
            editEmail.requestFocus();
            editEmail.setError(getResources().getString(R.string.errorEmail));
            return false;
        } else if (!Validator.isValidEmail(context, editEmail.getText().toString()).equals("")) {
            editEmail.requestFocus();
            editEmail.setError(Validator.isValidEmail(context, editEmail.getText().toString()));
            return false;
        }

        initPassWordModel();

        return true;
    }


    private void initPassWordModel() {
        ForgotPasswordModel.getInstance().setPassword("" + Helper.generateRandomPassword());
        ForgotPasswordModel.getInstance().setEmail(editEmail.getText().toString());
    }


    private void resetPasswordAndSendMail() {

        progressDialog(currentActivity, "Loading...", "Please Wait..", false, false);

        JSONObject jsonForgotPaswordRequest = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            jsonForgotPaswordRequest = new JSONObject(gson.toJson(ForgotPasswordModel.getInstance()));
            Log.e("json forgo pswd request", jsonForgotPaswordRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String URL_FORGOT_PASSWORD = FORGOT_PASSWORD_URL;
        JsonObjectRequest forgotPasswordRequest = new JsonObjectRequest(Request.Method.POST, URL_FORGOT_PASSWORD, jsonForgotPaswordRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    Log.e(getResources().getString(R.string.nwk_response_forgot_password), response.toString());
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        //  alert(currentActivity, message, message, getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, false, ALERT_TYPE_NO_NETWORK);
                        toast(getResources().getString(R.string.emailNotExist), true);
                    } else {

                        containerPasswordSended.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                containerPasswordSended.setVisibility(View.VISIBLE);
                // toast( getResources().getString(R.string.nwk_error_forgot_password),true);
                Log.e(getResources().getString(R.string.nwk_error_forgot_password), error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(forgotPasswordRequest);
    }


}
