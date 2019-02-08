package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TermsResponse extends BaseResponse{

    @SerializedName("result")
    public Result result;


    //object
    public class Result {

        @SerializedName("content")
        public String content;
    }
}
