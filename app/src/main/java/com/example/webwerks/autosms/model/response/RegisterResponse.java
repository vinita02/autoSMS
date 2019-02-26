package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse extends BaseResponse{



    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("mobile_number")
        public String mobile_number;


        @SerializedName("activation_code_id")
        public String activation_code_id;



        @SerializedName("activation_code")
        public String activation_code;


        @SerializedName("operator")
        public Operator operator;

        @SerializedName("sms_plan")
        public String sms_plan;


        @SerializedName("sim_type")
        public String sim_type;


        @SerializedName("token")
        public String token;


        @SerializedName("billing_date")
        public String billing_date;

        @SerializedName("message")
        public String message;

//        @SerializedName("created_at")
//        public Created_at created_at;

    }

//    public class Created_at{
//
//
//        @SerializedName("date")
//        public String date;
//
//
//        @SerializedName("timezone_type")
//        public String timezone_type;
//
//
//        @SerializedName("timezone")
//        public String timezone;
//    }

    public class Operator{

        @SerializedName("id")
        public String id;


        @SerializedName("operator_name")
        public String operator_name;


        @SerializedName("min_percent_usage")
        public String min_percent_usage;


        @SerializedName("max_percent_usage")
        public String max_percent_usage;


        @SerializedName("per_day_min_sms")
        public String per_day_min_sms;

        @SerializedName("per_day_max_sms")
        public String per_day_max_sms;


        @SerializedName("per_month_min_sms")
        public String per_month_min_sms;


        @SerializedName("per_month_max_sms")
        public String per_month_max_sms;

        @SerializedName("active_status")
        public String active_status;


        @SerializedName("created_at")
        public String created_at;

        @SerializedName("updated_at")
        public String updated_at;


        @SerializedName("deleted_at")
        public String deleted_at;

    }


}
