package com.example.webwerks.autosms.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendMessagesIdResponse extends BaseResponse{


    @SerializedName("monthly_rewards")
    @Expose
    public String monthly_rewards;

}
