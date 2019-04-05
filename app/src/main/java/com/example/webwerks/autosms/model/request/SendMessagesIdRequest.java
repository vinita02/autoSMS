package com.example.webwerks.autosms.model.request;

import java.util.ArrayList;

public class SendMessagesIdRequest {

   ArrayList<Integer> message_id ;
   String mobile_number;

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public ArrayList<Integer> getMessage_id() {
        return message_id;
    }

    public void setMessage_id(ArrayList<Integer> message_id) {
        this.message_id = message_id;
    }
}
