package com.example.webwerks.autosms.model.request;

public class UpdateProfileRequest {


    private String mobile_number;
    private String  activation_code;
    private int operator;
    private String sim_type;
    private String sms_plan;
    private String billing_date;

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public String getSim_type() {
        return sim_type;
    }

    public void setSim_type(String sim_type) {
        this.sim_type = sim_type;
    }

    public String getSms_plan() {
        return sms_plan;
    }

    public void setSms_plan(String sms_plan) {
        this.sms_plan = sms_plan;
    }

    public String getBilling_date() {
        return billing_date;
    }

    public void setBilling_date(String billing_date) {
        this.billing_date = billing_date;
    }
}
