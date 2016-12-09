package com.neon.vyaan.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayank on 19/02/2016.
 */
public class RegistrationIntentService extends IntentService implements AppConstants {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static String url;

    int userId;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        Log.e("gcm reg", "service");
        userId = gettingUserId();

        unsetTokenSended();


        url = AppConstants.BASE_GCM_URL + "/register.php";
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]


            Log.e(TAG, "GCM Registration Token: " + token);

            sharedPreferences.edit().putString(AppConstants.GCM_TOKEN, token).apply();
            sharedPreferences.edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, true).apply();


            if (!isGCmTokenReg()) {
                // TODO: Implement this method to send any registration to your app's servers.


                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.

                // [END register_for_gcm]

                //  sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);
            }


        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("name", "xyz@gmail.com");
        postParam.put("email", "somepasswordhere");
        postParam.put("userId", String.valueOf(userId));
        postParam.put("regId", token);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                setTokenSended();
                Log.e("gcm reg responce is", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("gcm responce error is", error.toString());
            }
        });

        VyaanApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]


    private int gettingUserId() {
        preferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);

        return preferences.getInt(AppConstants.KEY_USER_ID, 0);

    }

    private boolean isGCmTokenReg() {
        preferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);


        return preferences.getBoolean(AppConstants.SENT_TOKEN_TO_SERVER, false);

    }

    private void setTokenSended() {
        preferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean(SENT_TOKEN_TO_SERVER, true);


    }


    private void unsetTokenSended() {
        preferences = getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        editor.putBoolean(SENT_TOKEN_TO_SERVER, false);


    }
}