package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse extends BaseResponse{



    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("email")
        public String email;

        @SerializedName("name")
        public String name;

        @SerializedName("token")
        public String token;

    }
}
