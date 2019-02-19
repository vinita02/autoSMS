package com.example.webwerks.autosms.model;

import java.util.ArrayList;

public class Contacts extends ArrayList<Contacts.User> {


    public ArrayList<User> users;

    public static class User{

        String mobile;
        int id;
        String messages;

        public String getMessages() {
            return messages;
        }

        public void setMessages(String messages) {
            this.messages = messages;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
