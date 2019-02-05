package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;


public class LoginResponse extends BaseResponse {


    @SerializedName("result")
    public Result result;

//    @SerializedName("result")
//    public ArrayList<Result> result;


    public class Result {

        @SerializedName("token")
        public String token;

//        @SerializedName("user_details")
//        public UserDetails userDetails;

        @SerializedName("user_id")
        public String user_id;

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

        @SerializedName("operator_id")
        public String operator_id;

        @SerializedName("remember_token")
        public String remember_token;

        @SerializedName("pay_monthly")
        private String pay_monthly;

        @SerializedName("is_verified")
        public String is_verified;

        @SerializedName("is_blocked")
        public String is_blocked;

        @SerializedName("active_status")
        public String active_status;

        @SerializedName("created_at")
        public CreateDate created_at;

        @SerializedName("updated_at")
        public UpdateDate updated_at;

        @SerializedName("deleted_at")
        public String deleted_at;

        @SerializedName("message")
        public String message;

    }

    public class CreateDate{

        @SerializedName("date")
        public String date;

        @SerializedName("timezone_type")
        public String timezone_type;

        @SerializedName("timezone")
        public String timezone;
    }

    public class UpdateDate{

        @SerializedName("date")
        public String date;

        @SerializedName("timezone_type")
        public String timezone_type;

        @SerializedName("timezone")
        public String timezone;
    }

}
