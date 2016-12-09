package com.neon.vyaan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neon.vyaan.R;


public class AppAccessActivity extends BaseActivity {

    Button btnSignUp;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_access);


    }


    @Override
    protected void initContext() {

        currentActivity = AppAccessActivity.this;
        context = AppAccessActivity.this;

    }

    @Override
    protected void initViews() {

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }


    @Override
    protected void initListners() {
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

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
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSignUp: {
                startActivity(currentActivity, SignUpActivity.class, bundle, true, REQUEST_TAG_SIGN_UP_ACTIVITY, true, ANIMATION_SLIDE_UP);

                break;
            }
            case R.id.btnLogin: {
                startActivity(currentActivity, LoginActivity.class, bundle, true, REQUEST_TAG_SIGN_IN_ACTIVITY, true, ANIMATION_SLIDE_UP);

                break;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_TAG_SIGN_IN_ACTIVITY: {
                    progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


                    startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
                    finish();

                    break;
                }

                case REQUEST_TAG_SIGN_UP_ACTIVITY: {
                    progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

                    startActivity(currentActivity, Dashboard.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
                    finish();

                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
