package com.neon.vyaan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.neon.vyaan.constants.AppConstants;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by Mayank on 27/04/2016.
 */
public class Helper {

    public static long generateRandomPassword() {

        int max = 99999;
        int min = 10000;
        Date date = new Date();


        Random random = new Random();


        Log.e("random no", "" + date.getTime() + random.nextInt((max - min) + 1) + min);

        return date.getTime() + random.nextInt((max - min) + 1) + min;

    }

    public static byte[] getImageBytes(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }

    public static int randomNumberCreation(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}



