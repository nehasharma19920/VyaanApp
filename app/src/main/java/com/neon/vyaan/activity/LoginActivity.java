package com.neon.vyaan.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.LoginModel;
import com.neon.vyaan.utils.FacebookLogin;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    EditText editEmail;
    EditText editPassword;

    Button buttonLogin;

    TextView textSignUp;
    TextView textForgetPassword;

    RelativeLayout containerGoogleLogin;
    RelativeLayout containerFacebookLogin;

    LoginModel loginModel;

    FacebookLogin objectFb;


    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void initViews() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        textSignUp = (TextView) findViewById(R.id.textSignUp);
        textForgetPassword = (TextView) findViewById(R.id.textForgetPassword);

        containerGoogleLogin = (RelativeLayout) findViewById(R.id.containerGoogleLogin);
        containerFacebookLogin = (RelativeLayout) findViewById(R.id.containerFacebookLogin);

        googleSignIn();
    }

    @Override
    protected void initContext() {

        currentActivity = LoginActivity.this;
        context = LoginActivity.this;

    }

    @Override
    protected void initListners() {
        buttonLogin.setOnClickListener(this);
        textSignUp.setOnClickListener(this);
        textForgetPassword.setOnClickListener(this);
        containerGoogleLogin.setOnClickListener(this);
        containerFacebookLogin.setOnClickListener(this);

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
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle(R.string.title_activity_login);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }


    private boolean isMandatoryFields() {

        editEmail.setError(null);
        editPassword.setError(null);

        if (!Validator.isValidEmail(context, editEmail.getText().toString()).equals("")) {

            editEmail.requestFocus();
            editEmail.setError(Validator.isValidEmail(context, editEmail.getText().toString()));
            return false;
        } else if (editPassword.getText().toString().isEmpty()) {
            editPassword.requestFocus();
            editPassword.setError(getResources().getString(R.string.error_password_empty));
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textSignUp: {
                startActivity(currentActivity, SignUpActivity.class, bundle, true, REQUEST_TAG_SIGN_UP_ACTIVITY, true, ANIMATION_SLIDE_UP);
                finish();
                break;
            }

            case R.id.textForgetPassword: {
                startActivity(currentActivity, ForgotPassword.class, bundle, false, REQUEST_TAG_NO_RESULT, false, ANIMATION_SLIDE_UP);


                break;
            }

            case R.id.buttonLogin: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        toHideKeyboard();
                        initLoginModel();
                        toLogin();
                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }

                break;
            }

            case R.id.containerGoogleLogin: {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            }

            case R.id.containerFacebookLogin: {

                objectFb = new FacebookLogin(currentActivity, context);
                objectFb.facebookSingnUp();
                break;
            }
        }


    }

    private void initLoginModel() {
        loginModel = new LoginModel();

        loginModel.setEmail(editEmail.getText().toString());
        loginModel.setPassword(editPassword.getText().toString());
        loginModel.setIsAdmin(0);
    }


    public void toLogin() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        String json = new Gson().toJson(loginModel);
        logTesting("login request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull Login", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(AppConstants.KEY_IS_USER_LOGIN, true);
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_NAME, response.getString(KEY_USER_NAME));
                        SharedPreferenceUtils.getInstance(currentActivity).putInteger(AppConstants.KEY_USER_ID, response.getInt(KEY_USER_ID));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_EMAIL, editEmail.getText().toString());

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        cancelProgressDialog();
                        alert(currentActivity, getString(R.string.error_login), getString(R.string.alert_username_incorrect), getString(R.string.labelOk), getString(R.string.labelCancel), false, true, ALERT_TYPE_NO_NETWORK);

                        logTesting("login error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("login json exeption is", e.toString(), Log.ERROR);
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

                params.put("Content-Type", "application/json");


                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            objectFb.callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }


    public void facebookLogin(final Bundle bundle) {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        try {
            jsons = new JSONObject();

            jsons.put("name", bundle.getString(AppConstants.keyName));
            jsons.put("email", bundle.getString(AppConstants.keyEmail));
            jsons.put("facebook_id", bundle.getString(AppConstants.keyFbId));
            jsons.put("is_app_user", "1");
            jsons.put("is_admin", "0");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        logTesting("facebook login request json", jsons.toString(), Log.ERROR);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_FACEBOOK_LOGIN, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull facebook Login", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(AppConstants.KEY_IS_USER_LOGIN, true);
                        SharedPreferenceUtils.getInstance(currentActivity).putInteger(AppConstants.KEY_USER_ID, response.getInt(KEY_USER_ID));

                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_NAME, bundle.getString(AppConstants.keyName));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_EMAIL, bundle.getString(AppConstants.keyEmail));

                        setResult(RESULT_OK);
                        finish();
                    } else {

                        alert(currentActivity, getString(R.string.alert_user_normal_exist), getString(R.string.alert_user_normal_exist), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);


                        cancelProgressDialog();
                        logTesting("login error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("login json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorFacebookLogin), true);

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


    private void googleSignIn() {

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.

        // [END customize_button]
    }


    @Override
    public void onStart() {
        super.onStart();

 /*       OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    cancelProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Bundle bundle = new Bundle();

            bundle.putString(AppConstants.keyEmail, acct.getEmail());
            bundle.putString(AppConstants.keyName, acct.getDisplayName());
            //     bundle.putString(AppConstants.KEY_PIC_URL, acct.getPhotoUrl().toString());
            bundle.putString(AppConstants.keyGoogleId, acct.getId());


            googleLogin(bundle);

            // Toast.makeText(LoginActivity.this, "" + acct.getEmail() + acct.getPhotoUrl() + acct.getIdToken(), Toast.LENGTH_LONG).show();
      /*      mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }


    public void googleLogin(final Bundle bundle) {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        try {
            jsons = new JSONObject();

            jsons.put("name", bundle.getString(AppConstants.keyName));
            jsons.put("email", bundle.getString(AppConstants.keyEmail));
            jsons.put("google_id", bundle.getString(AppConstants.keyGoogleId));
            jsons.put("is_app_user", "1");
            jsons.put("is_admin", "0");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        logTesting("google login request json", jsons.toString(), Log.ERROR);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_GOOGLE_LOGIN, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull google Login", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {


                        SharedPreferenceUtils.getInstance(currentActivity).putBoolean(AppConstants.KEY_IS_USER_LOGIN, true);
                        SharedPreferenceUtils.getInstance(currentActivity).putInteger(AppConstants.KEY_USER_ID, response.getInt(KEY_USER_ID));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_NAME, bundle.getString(AppConstants.keyName));
                        SharedPreferenceUtils.getInstance(currentActivity).putString(AppConstants.KEY_USER_EMAIL, bundle.getString(AppConstants.keyEmail));


                        setResult(RESULT_OK);
                        finish();
                    } else {

                        alert(currentActivity, getString(R.string.alert_user_normal_exist), getString(R.string.alert_user_normal_exist), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);


                        cancelProgressDialog();
                        logTesting("google login error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("google login json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorGoogleLogin), true);

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
