package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewProfileResponse extends BaseResponse{


    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("profile")
        public Profile profile;


        @SerializedName("activation_codes")
        public ActivationCode activation_codes;


        @SerializedName("operators")
        public ArrayList<Operators> operators;

        public ArrayList<Operators> getOperators() {
            return operators;
        }

        public void setOperators(ArrayList<Operators> operators) {
            this.operators = operators;
        }

        @SerializedName("token")
        public String token;


        @SerializedName("message")
        @Expose
        public String message;
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

        @SerializedName("operator_id")
        public String operator_id;

        @SerializedName("remember_token")
        public String remember_token;

        @SerializedName("sim_type")
        public String sim_type;

        @SerializedName("sms_plan")
        public String sms_plan;

        @SerializedName("billing_date")
        public String billing_date;

        @SerializedName("is_verified")
        public String is_verified;

        @SerializedName("is_blocked")
        public String is_blocked;

        @SerializedName("active_status")
        public String active_status;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("updated_at")
        public String updated_at;

        @SerializedName("deleted_at")
        public String deleted_at;

    }


    public class ActivationCode{

        @SerializedName("code")
        public String code;
    }

    public static class Operators{

        @SerializedName("id")
        public String id;

        @SerializedName("operator_name")
        public String operator_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOperator_name() {
            return operator_name;
        }

        public void setOperator_name(String operator_name) {
            this.operator_name = operator_name;
        }
    }

}
