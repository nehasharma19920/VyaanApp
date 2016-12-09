package com.neon.vyaan.utils;

import android.content.Context;

import com.neon.vyaan.constants.AppConstants;


/**
 * Created by Mayank on 27/04/2016.
 */
public class LoginUtils {


    public static boolean isLogin(Context context) {

        return SharedPreferenceUtils.getInstance(context).getSharedPreferences().getBoolean(AppConstants.KEY_IS_USER_LOGIN, false);
    }


}
