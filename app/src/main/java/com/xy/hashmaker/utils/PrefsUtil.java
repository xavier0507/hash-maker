package com.xy.hashmaker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Xavier Yin on 10/27/16.
 */

public class PrefsUtil {
    private static final String PREFS_TOKEN = "token";
    private static final String PREFS_TOKEN_FB_KEY_AUTH = "fb_auth";

    public static void setLogin(Context context, boolean isLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putBoolean(PREFS_TOKEN_FB_KEY_AUTH, isLogin);
            editor.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TOKEN, Context.MODE_PRIVATE);
        boolean isLogin;

        try {
            if (sharedPreferences == null) {
                isLogin = false;
            } else {
                isLogin = sharedPreferences.getBoolean(PREFS_TOKEN_FB_KEY_AUTH, false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isLogin;
    }
}
