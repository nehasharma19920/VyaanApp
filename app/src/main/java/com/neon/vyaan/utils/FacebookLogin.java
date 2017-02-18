package com.neon.vyaan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.activity.LoginActivity;
import com.neon.vyaan.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FacebookLogin {

    static Activity activity;
    static Context context;

    static private LoginManager loginManager;
    static private LoginButton loginButton;
    static public CallbackManager callbackManager;
    static private FacebookCallback facebookCallback;

    static ImageView profilePicture;
    static JSONObject jsonObject;
    static int flag = 0;
    static int permissionFlag = 0;

    static Bitmap bitmap;
    View view;


    static String email;
    static String gender;
    static String name;
    static String id;
    static Bundle bundle = new Bundle();
    static String imageUrl = null;


    public FacebookLogin(Activity activity, final Context context) {
        this.activity = activity;
        this.context = context;


    }

    public void definingCallback() {

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();


        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                //setProfile();

                Log.e("succes", "yes");

                ((BaseActivity) activity).progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);
                fetchUserInfo();


            }

            @Override
            public void onCancel() {
//                Log.e("is cancel", "yes");
                permissionFlag = 1;
//                Toast.makeText(context, "This app needs your " + new String(String.valueOf(AccessToken.getCurrentAccessToken().getDeclinedPermissions()).replaceAll("[\\[\\]]", "")), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("FacebookException is = ", e.toString());
                if (e instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        if (loginManager != null)
                            loginManager.logOut();
                    }
                }
            }
        };

    }


    public void facebookSingnUp() {
        definingCallback();
        if (loginManager != null)
            loginManager.logOut();
        if (flag == 0) {
            loginManager.logInWithReadPermissions(activity, Arrays.asList("email", "user_friends"));
            loginManager.registerCallback(callbackManager, facebookCallback);

            flag = 0;
        } else {
            loginManager.logOut();
            flag = 0;

        }
    }

    public static void logout() {
        LoginManager.getInstance().logOut();
    }

    private static void fetchUserInfo() {
        ((BaseActivity) activity).logTesting("method called", "yes", Log.ERROR);
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject1, GraphResponse response) {
                    jsonObject = jsonObject1;
                    ((BaseActivity) activity).logTesting("completion", "yes", Log.ERROR);
                    try {
                        imageUrl = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                        ((BaseActivity) activity).logTesting("imaeUrl", imageUrl, Log.ERROR);
                        id = jsonObject.getString("id");
                        name = jsonObject.getString("name");


                        if (jsonObject.has("email")) {
                            email = jsonObject.getString("email");

                        } else {

                        }
                        gender = jsonObject.getString("gender");
                        ((BaseActivity) activity).logTesting("name is", name + id + imageUrl + email, Log.ERROR);
                        SharedPreferenceUtils.getInstance(context).putString(AppConstants.keyName, name);
                        bundle.putString(AppConstants.keyEmail, email);
                        bundle.putString(AppConstants.keyName, name);
                        bundle.putString(AppConstants.keyFbId, id);
                        bundle.putString(AppConstants.keyProfilePicUrl, imageUrl);
                        SharedPreferenceUtils.getInstance(context).putString(AppConstants.KEY_PIC_URL, imageUrl);


                        ((LoginActivity) activity).facebookLogin(bundle);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            jsonObject = null;
        }


    }


}
