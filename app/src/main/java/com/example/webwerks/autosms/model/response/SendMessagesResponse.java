package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendMessagesResponse extends BaseResponse{


    @SerializedName("result")
    public ArrayList<Result> result;


    //object
    public static class Result {

        @SerializedName("message_id")
        @Expose
        public String message_id;

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getMessage_content() {
            return message_content;
        }

        public void setMessage_content(String message_content) {
            this.message_content = message_content;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        @SerializedName("message_content")
        @Expose
        public String message_content;


        @SerializedName("mobile_number")
        @Expose
        public String mobile_number;
    }

}
