package com.neon.vyaan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.interfaces.CancellingResendOtpThread;
import com.neon.vyaan.receiver.IncomingSms;
import com.neon.vyaan.utils.Helper;
import com.neon.vyaan.utils.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class OtpValidation extends BaseActivity implements View.OnClickListener, CancellingResendOtpThread {

    EditText edtOtp;
    Button btnSubmit;
    Button btnResendOtp;
    Button btnChangeNumber;
    ProgressDialog pdialog;

    public static Runnable r;
    public static Handler handler;
    public String textMobileNo;
    public SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SendOTP sendOTP;

    String msgBody = " ";
    int otp;
    String suppliededOtp;

    BroadcastReceiver smsReciever;
    Boolean recieverRegistered = false;
    Boolean exitStatus = false;
    Long timeafterWhichButtonsAreEnable = 10000l;
    String password = "rexovo";
    int oldOtp;

    ActionBar actionBar;
    Runnable runnable;

    static Handler handlerButtonVisibility;
    public static Runnable rButtonVisibility;
    long longTimeAfterWhichButtonEnable = 35000l;
    String stringMobileNumber;
    TextView textMobileNumber;
    CountDownTimer countDownTimer;

    long longTotalVerificationTime = 35000;
    long longOneSecond = 1000;

    RelativeLayout layoutSendingOtp;
    RelativeLayout layoutContainerForOtpOptions;
    RelativeLayout layoutContainerForMobileNoWhenAuthenticating;
    TextView textViewMobileNumberWhenAuthenticating;

    TextView textViewTimer;
    TextView textViewSendingOtp;
    Animation animationBlinking;
    CountDownTimer timerForUnregisteringBroadcastReciever;
    long timeafterwhichRecieverUnregister = 900000l;
    boolean istimerForUnregisteringBroadcastRecieverrunning = false;
    int intNumberOfTimesResendPress = 0;

    ProgressDialog progressDialog;
    Context context;
    Bundle bundle;

    public static int OtpIs = 0;
    ImageView imgClearOtp;
    TextView textInCorrectOtp;
    Intent loginScreenRecievingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.otp_validate);


    }

    public void onCreateWork() {
        getSupportActionBar().setTitle("Enter OTP");
        handler = new Handler();


        //    settingHomeButton();
        settingListners();
        context = OtpValidation.this;

        if (loginScreenRecievingIntent.hasExtra(AppConstants.keyMobileNo)) {
            textMobileNo = loginScreenRecievingIntent.getStringExtra(AppConstants.keyMobileNo);
        }
        textMobileNumber.setText(getResources().getString(R.string.textOtp) + " " + textMobileNo);
        editor.putString(AppConstants.keyMobileNo, textMobileNo.toString().trim());
        editor.apply();
        OtpIs = 0;

        if (!(OtpIs == 0)) {
            otp = OtpIs;
            editor.putString(AppConstants.keyOtp, String.valueOf(otp));
            editor.apply();
        } else {
            otp = Helper.randomNumberCreation(100000, 999999);
            OtpIs = otp;

            editor.putString(AppConstants.keyOtp, String.valueOf(otp));
            editor.apply();
        }

        smsReciever = new IncomingSms(OtpValidation.this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.eventSmsReceived);
        intentFilter.setPriority(AppConstants.highPriority);
        registerReceiver(smsReciever, intentFilter);
        sendOTP = new SendOTP();

        if (Validator.isNetworkAvailable(context)) {
            btnResendOtp.setClickable(false);
            btnResendOtp.setAlpha(0.5f);
            functionEnablingButtons();
            functionInitializingCountDownTimer();

            sendOTP.execute();
            recieverRegistered = true;
            functionForUnregisteringBroadcastReceiever();
        } else {
            alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);
        }
    }

    @Override
    protected void initViews() {

        loginScreenRecievingIntent = getIntent();
        context = getBaseContext();
        currentActivity = OtpValidation.this;
        bundle = new Bundle();
        imgClearOtp = (ImageView) findViewById(R.id.imgClearOtp);
        imgClearOtp.setOnClickListener(this);
        edtOtp = (EditText) findViewById(R.id.edtOtp);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnResendOtp = (Button) findViewById(R.id.btnResendOtp);
        btnChangeNumber = (Button) findViewById(R.id.btnChangeNumber);
        textMobileNumber = (TextView) findViewById(R.id.textMobileNumber);
        textInCorrectOtp = (TextView) findViewById(R.id.textInCorrectOtp);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);


        preferences = getSharedPreferences(AppConstants.nameSharedPreference, MODE_PRIVATE);
        editor = preferences.edit();

        onCreateWork();
    }

    @Override
    protected void initContext() {
        currentActivity = OtpValidation.this;
        context = OtpValidation.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    public boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                toHideKeyboard();
                ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);
                suppliededOtp = edtOtp.getText().toString();

                if (suppliededOtp.equals("")) {
                    cancelProgressDialog();
                    toast(getResources().getString(R.string.emptyOtp), true);
                } else {
                    textInCorrectOtp.setVisibility(View.INVISIBLE);
                    preferences = getSharedPreferences(AppConstants.nameSharedPreference, MODE_PRIVATE);
                    int otp = Integer.parseInt(preferences.getString(AppConstants.keyOtp, AppConstants.entityNotPresent));
                    if (Integer.parseInt(suppliededOtp) == otp) {
                        if (recieverRegistered == true) {
                            unregisterReceiver(smsReciever);
                            recieverRegistered = false;
                        }

                        if (!sendOTP.isCancelled()) {
                            sendOTP.cancel(true);
                        }

                        if (istimerForUnregisteringBroadcastRecieverrunning) {
                            timerForUnregisteringBroadcastReciever.cancel();
                            istimerForUnregisteringBroadcastRecieverrunning = false;
                        }

                        toast(getResources().getString(R.string.messageNoValidated), true);
                        numberVerified();
                    } else {
                        cancelProgressDialog();
                        textInCorrectOtp.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }
            case R.id.btnChangeNumber: {
                toHideKeyboard();

                if (!sendOTP.isCancelled()) {
                    sendOTP.cancel(true);
                }

                if (recieverRegistered == true) {
                    unregisterReceiver(smsReciever);
                    recieverRegistered = false;
                }

                if (istimerForUnregisteringBroadcastRecieverrunning) {
                    timerForUnregisteringBroadcastReciever.cancel();
                    istimerForUnregisteringBroadcastRecieverrunning = false;
                }

                finish();

                //     startActivity(currentActivity, UserNumber.class, bundle, true, AppConstants.REQUEST_TAG_CHANGE_NUMBER, false);
                break;
            }
            case R.id.btnResendOtp: {
                toHideKeyboard();
                functionInitializingCountDownTimer();
                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;

                if (intNumberOfTimesResendPress <= AppConstants.limitReset) {
                    if (istimerForUnregisteringBroadcastRecieverrunning) {
                        timerForUnregisteringBroadcastReciever.cancel();
                        istimerForUnregisteringBroadcastRecieverrunning = false;
                    }
                    functionForUnregisteringBroadcastReceiever();
                    if (!sendOTP.isCancelled()) {
                        sendOTP.cancel(true);
                    }

                    sendOTP = new SendOTP();
                    if (Validator.isNetworkAvailable(context)) {
                        sendOTP.execute();
                        btnResendOtp.setClickable(false);
                        btnResendOtp.setAlpha(0.5f);
                        functionEnablingButtons();
                        toast(getResources().getString(R.string.messageOtpResent), true);
                    } else {
                        alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);
                    }
                }
                break;
            }
            case R.id.imgClearOtp: {
                edtOtp.setText("");
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.REQUEST_TAG_CHANGE_NUMBER && resultCode == RESULT_OK) {
            loginScreenRecievingIntent = data;
            countDownTimer.cancel();
            onCreateWork();
        }
    }

    public void settingListners() {
        btnSubmit.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);
        btnChangeNumber.setOnClickListener(this);
    }

    @Override
    public void cancelHandler(boolean isCancel) {
        handlerButtonVisibility.removeCallbacks(rButtonVisibility);
        if (!sendOTP.isCancelled()) {
            sendOTP.cancel(true);
        }

        countDownTimer.cancel();
        timerForUnregisteringBroadcastReciever.cancel();
    }

    public void functionEnablingButtons() {
        rButtonVisibility = new Runnable() {
            @Override
            public void run() {
                btnResendOtp.setClickable(true);
                btnResendOtp.setAlpha(1);

            }
        };
        handlerButtonVisibility = new Handler();
        handlerButtonVisibility.postDelayed(rButtonVisibility, longTimeAfterWhichButtonEnable);
    }


    @Override
    public void onAlertClicked(int alertType) {

    }

    public class SendOTP extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stringMobileNumber = preferences.getString(AppConstants.keyMobileNo, textMobileNo.toString().trim());
            SharedPreferences preferences = getSharedPreferences(AppConstants.nameSharedPreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            oldOtp = Integer.parseInt(preferences.getString(AppConstants.keyOtp, AppConstants.entityNotPresent));
            Log.i("Otp is", "" + oldOtp);
        }

        @Override
        protected String doInBackground(String... params) {
            // String message = "Welcome to TruckLoad. Your OTP is " + oldOtp;
            //Helper.SMSSender(textMobileNo, message);

            // String respId = Helper.sendSMS(textMobileNo, message);


            String message = "Dear%20Customer,%20your%20verification%20OTP%20is%20" + oldOtp + "%20,%20Thanks%20for%20subscribing%20with%20VYAAN";

            new RequestTask().execute("http://enterprise.smsgupshup.com/GatewayAPI/rest?method=SendMessage&send_to=91" + textMobileNo + "&msg=" + message + "&msg_type=TEXT&userid=2000161769&auth_scheme=plain&password=WiUCjFMov&v=1.1&format=text");

           /* if (respId == null) {
                return null;
            }*/
            //  new RequestTask().execute("http://enterprise.smsgupshup.com/GatewayAPI/rest?method=SendMessage&send_to=91" + textMobileNo + "&msg=" + oldOtp + "%20This%20is%20your%20OTP%20for%20verifying%20your%20mobile%20number%20for%20QuizProQuo." + "&msg_type=TEXT&userid=2000150385&auth_scheme=plain&password=gfHuYOSxn&v=1.1&format=text");
            // Log.e("hi", "http://enterprise.smsgupshup.com/GatewayAPI/rest?method=SendMessage&send_to=91" + textMobileNo + "&msg=" + oldOtp + "%20This%20is%20your%20OTP%20for%20verifying%20your%20mobile%20number%20for%20QuizProQuo." + "&msg_type=TEXT&userid=2000150385&auth_scheme=plain&password=gfHuYOSxn&v=1.1&format=text");
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onBackPressed() {
        if (exitStatus == true) {
            //pdialog.cancel();
            //finish();
            super.onBackPressed();
        } else {
            toast(getResources().getString(R.string.messageExit), true);
            exitStatus = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitStatus = false;

                }
            }, 2000);
        }
    }

    public void functionInitializingCountDownTimer() {
        textViewTimer.setVisibility(View.VISIBLE);
        stringMobileNumber = preferences.getString(AppConstants.keyMobileNo, textMobileNo.toString().trim());
        textMobileNumber.setText(getResources().getString(R.string.textOtp) + " " + stringMobileNumber);
        //   textViewMobileNumberWhenAuthenticating.setText(stringMobileNumber);

        animationBlinking = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
//        textViewSendingOtp.setAnimation(animationBlinking);

  /*      layoutContainerForMobileNoWhenAuthenticating.setVisibility(View.VISIBLE);
        layoutContainerForOtpOptions.setVisibility(View.GONE);
        layoutSendingOtp.setVisibility(View.VISIBLE);

*/
        countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setVisibility(View.VISIBLE);
                textViewTimer.setText("Wait for " + millisUntilFinished / 1000 + " " + "seconds");
            }

            @Override
            public void onFinish() {
                /*layoutContainerForMobileNoWhenAuthenticating.setVisibility(View.GONE);
                layoutSendingOtp.setVisibility(View.GONE);
                layoutContainerForOtpOptions.setVisibility(View.VISIBLE);*/
                textViewTimer.setVisibility(View.INVISIBLE);
                edtOtp.setFocusable(true);

                edtOtp.requestFocus();
                edtOtp.setFocusableInTouchMode(true);
                edtOtp.setCursorVisible(true);
                edtOtp.invalidate();
            }
        }.start();
    }

    public void functionForUnregisteringBroadcastReceiever() {
        istimerForUnregisteringBroadcastRecieverrunning = true;
        timerForUnregisteringBroadcastReciever = new CountDownTimer(timeafterwhichRecieverUnregister, 120000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (recieverRegistered == true) {
                    unregisterReceiver(smsReciever);
                    recieverRegistered = false;
                }
            }
        };
    }


    /*class to Request the Gupshup service to send otp Start*/
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            URL url = null;
            try {
                url = new URL(uri[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                BufferedReader rd = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                System.out.println(buffer.toString());
                rd.close();
                conn.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }

/*
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }*/
            String responseString = "";
            return responseString;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }

    }


    // to do the operation
    public void numberVerified() {


        setResult(RESULT_OK);
        finish();
        /*preferences = getSharedPreferences(AppConstants.nameSharedPreference, MODE_PRIVATE);
        //  editor.putBoolean(AppConstants.keyUserRegistered,true);
        editor.apply();*/
    }


}