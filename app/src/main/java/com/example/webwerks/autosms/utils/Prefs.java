package com.example.webwerks.autosms.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    private static final String USER_DATA = "user_data";
    private static final String TOKEN = "token";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setUserData(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_DATA, str);
        editor.apply();
    }

    public static String getUserData(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_DATA, "");
    }

    public static void setToken(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TOKEN, str);
        editor.apply();
    }

    public static String getToken(Context ctx) {
        return getSharedPreferences(ctx).getString(TOKEN, "");
    }
}