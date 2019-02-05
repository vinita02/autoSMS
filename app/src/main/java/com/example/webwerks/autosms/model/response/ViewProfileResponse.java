package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewProfileResponse extends BaseResponse{

    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("profile")
        public Profile profile;

    }


    public class Profile{

        @SerializedName("id")
        public String id;

        @SerializedName("first_name")
        public String first_name;


        @SerializedName("last_name")
        public String last_name;


        @SerializedName("email")
        public String email;

        @SerializedName("mobile_number")
        public String mobile_number;

        @SerializedName("password")
        public String password;


        @SerializedName("activation_code_id")
        public String activation_code_id;

//        @SerializedName("id")
//        public String id;
//
//        @SerializedName("id")
//        public String id;
//
//        @SerializedName("id")
//        public String id;






    }

}
