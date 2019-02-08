package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateProfileResponse extends BaseResponse{

    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("message")
        public String message;
    }

}
