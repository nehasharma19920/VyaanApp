package com.neon.vyaan.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.OtpValidation;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.interfaces.CancellingResendOtpThread;


public class IncomingSms extends BroadcastReceiver implements CancellingResendOtpThread {


    final SmsManager sms = SmsManager.getDefault();
    private static String message;
    Context myContext;
    String phone;
    Activity callingActivityObj;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public IncomingSms() {

    }

    public IncomingSms(Activity callingActivityObj) {

        this.callingActivityObj = callingActivityObj;
        Log.e("reciever registered", "by me");
    }

    public void onReceive(Context context, Intent intent) {
        Log.e("message recieved", " sms recieved");
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        myContext = context;


        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();

                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    preferences = context.getSharedPreferences(AppConstants.nameSharedPreference, context.MODE_PRIVATE);
                    editor = preferences.edit();
                    int otp = Integer.parseInt(preferences.getString(AppConstants.keyOtp, AppConstants.entityNotPresentInt));
                    phone = preferences.getString(AppConstants.keyMobileNo, AppConstants.entityNotPresent);
                    Log.e("Recived otp", "" + Integer.parseInt(message.substring(40, 46).trim()));
                    Log.e("my otp", "" + "" + otp);
                    if ((otp == Integer.parseInt(message.substring(40, 46).trim()))) {
                        Log.e("Recived otp", "" + Integer.parseInt(message.substring(40, 46).trim()));

                        Toast.makeText(context, context.getResources().getString(R.string.messageMobileVerified), Toast.LENGTH_LONG).show();
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);

                        cancelHandler(true);
                        //Class startActivity = Class.forName("Dashboard");
                        // ((BaseActivity) myContext).startActivity((Activity) myContext, startActivity, bundle, false, AppConstants.REQUEST_TAG_NO_RESULT, true,AppConstants.animation[0]);
                        ((OtpValidation) myContext).numberVerified();

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.messageMobileNotVerified), Toast.LENGTH_LONG).show();
                    }


                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }


    }


    @Override
    public void cancelHandler(boolean iscancel) {

    }
}



