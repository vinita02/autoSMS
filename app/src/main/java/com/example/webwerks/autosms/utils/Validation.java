package com.example.webwerks.autosms.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {


    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password){
        return !TextUtils.isEmpty(password);
    }

    public static boolean isValidName(String name) {

        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(name);
        boolean bs = ms.matches();

        return bs;
    }

//    public static boolean isValidMobile(String phone) {
//        boolean check = false;
//
//        if (phone.length() != 10) {
//            check = false;
//        } else {
//            check = true;
//        }
//        return check;
//    }

    public static boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }

    public static boolean isValidValidationpassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    public static boolean isValidValidationcode(String code) {
        return !TextUtils.isEmpty(code);
    }

    public static boolean isValidOperator(String operator) {
        return !TextUtils.isEmpty(operator);
    }


    public static boolean isValidMobilenetwork(String network){
        return network.equalsIgnoreCase("0");
    }





}


