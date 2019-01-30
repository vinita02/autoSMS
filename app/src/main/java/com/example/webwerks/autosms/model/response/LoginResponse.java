package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {


    @SerializedName("result")
    private Result result;


    //object
    public class Result {

        @SerializedName("token")
        private String token;

        @SerializedName("user_details")
        private UserDetails userDetails;

    }

    // object
    public class UserDetails{

        @SerializedName("id")
        private String id;

        @SerializedName("first_name")
        private String first_name;

        @SerializedName("last_name")
        private String last_name;

        @SerializedName("email")
        private String email;

        @SerializedName("mobile_number")
        private String mobile_number;

        @SerializedName("password")
        private String password;

        @SerializedName("activation_code_id")
        private String activation_code_id;

        @SerializedName("operator_id")
        private String operator_id;

        @SerializedName("remember_token")
        private String remember_token;

        @SerializedName("pay_monthly")
        private String pay_monthly;

        @SerializedName("is_verified")
        private String is_verified;

        @SerializedName("is_blocked")
        private String is_blocked;

        @SerializedName("active_status")
        private String active_status;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("updated_at")
        private String updated_at;

        @SerializedName("deleted_at")
        private String deleted_at;

    }
}
