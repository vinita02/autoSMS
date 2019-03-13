package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NetworkResponse extends BaseResponse{

    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        public ArrayList<Operators> getOperators() {
            return operators;
        }

        public void setOperators(ArrayList<Operators> operators) {
            this.operators = operators;
        }

        @SerializedName("networks")
        public ArrayList<Operators> operators;
    }


    public static class Operators{

        @SerializedName("id")
        public String id;

        @SerializedName("name")
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
