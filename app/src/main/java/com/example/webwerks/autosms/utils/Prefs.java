package com.example.webwerks.autosms.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    private static final String PREF_LAUNCH_ACTIVITY = "launch_activity";
    private static final String USER_DATA = "user_data";
    private static final String TOKEN = "token";
    private static final String USER_MOBILE = "mobile";
    private static final String DELIVERD_IDS = "deliverIDs";
    private static final String ACTIVATION_CODE = "activation_code";
    private static final String MONTHLY_REWARDS = "monthly_rewards";
    private static final String RESPONSE_IDS = "responseIDs";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLaunchActivity(Context ctx, String launch_activity) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LAUNCH_ACTIVITY, launch_activity);
        editor.apply();
    }

    public static String getLaunchActivity(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LAUNCH_ACTIVITY, "");
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

    public static void setUserMobile(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_MOBILE, str);
        editor.apply();
    }

    public static String getUserMobile(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_MOBILE, "");
    }



    public static void setDeliverdIds(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(DELIVERD_IDS, str);
        editor.apply();
    }

    public static String getDeliverdIds(Context ctx) {
        return getSharedPreferences(ctx).getString(DELIVERD_IDS, "");
    }

    public static void setActivationCode(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ACTIVATION_CODE, str);
        editor.apply();
    }

    public static String getActivationCode(Context ctx) {
        return getSharedPreferences(ctx).getString(ACTIVATION_CODE, "");
    }

    public static void setMonthlyRewards(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(MONTHLY_REWARDS, str);
        editor.apply();
    }

    public static String getMonthlyRewards(Context ctx) {
        return getSharedPreferences(ctx).getString(MONTHLY_REWARDS, "");
    }

    public static void setResponseIds(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RESPONSE_IDS, str);
        editor.apply();
    }

    public static String getResponseIds(Context ctx) {
        return getSharedPreferences(ctx).getString(RESPONSE_IDS, "");
    }
}