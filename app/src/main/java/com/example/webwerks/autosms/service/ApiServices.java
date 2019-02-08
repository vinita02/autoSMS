package com.example.webwerks.autosms.service;


import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.model.response.TermsResponse;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServices {


   @POST("user/login")
   @FormUrlEncoded
   Single<LoginResponse> userLogin(
           @Header("Authorization") String Authorization,
           @Field("email") String email,
           @Field("password") String password);


   @GET("user/netwok_operator")
   Single<NetworkResponse> networkOperator();


   @POST("user/register")
   @FormUrlEncoded
   Single<RegisterResponse> userRegister(
           @Field("mobile_number") String mobile_number,
           @Field("activation_code") String activation_code,
           @Field("operator_id") int operator,
           @Field("sim_type") String sim_type,
           @Field("sms_plan") String sms_plan,
           @Field("billing_date") String billing_date);

   @POST("user/viewProfile")
   @FormUrlEncoded
   Single<ViewProfileResponse> viewProfile(
           @Field("token") String token);

   @POST("user/updateProfile")
   @FormUrlEncoded
   Single<UpdateProfileResponse> updateProfile(
           @Field("mobile_number") String mobile_number,
           @Field("activation_code") String activation_code,
           @Field("operator_id") int operator,
           @Field("sim_type") String sim_type,
           @Field("sms_plan") String sms_plan,
           @Field("billing_date") String billing_date);


   @GET("user/termscontent")
   Single<TermsResponse> getContent();

}
