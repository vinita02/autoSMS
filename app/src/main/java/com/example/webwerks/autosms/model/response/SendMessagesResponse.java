package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendMessagesResponse extends BaseResponse{


    @SerializedName("result")
    public ArrayList<Result> result;


    //object
    public class Result {

        @SerializedName("message_id")
        @Expose
        public String message_id;


        @SerializedName("message_content")
        @Expose
        public String message_content;


        @SerializedName("mobile_number")
        @Expose
        public String mobile_number;
    }

}
