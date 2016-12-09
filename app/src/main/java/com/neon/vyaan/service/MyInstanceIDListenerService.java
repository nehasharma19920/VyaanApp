package com.neon.vyaan.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.iid.InstanceIDListenerService;
import com.neon.vyaan.constants.AppConstants;

/**
 * Created by Mayank on 19/02/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService implements AppConstants {

    private static final String TAG = "MyInstanceIDLS";


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        unsetTokenSended();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        Toast.makeText(getApplicationContext(), "token refers", Toast.LENGTH_LONG).show();
        Log.e("token refersh", "yes");
    }
    // [END refresh_token]

    private void unsetTokenSended() {
        preferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean(SENT_TOKEN_TO_SERVER, false);


    }
}
