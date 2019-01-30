package com.example.webwerks.autosms.service;


import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServices {


   @POST("user/login")
   @FormUrlEncoded
   Single<LoginResponse> userLogin(
           @Header("Authorization") String Authorization,
           @Field("email") String email,
           @Field("password") int password);

   @POST("user/register")
   @FormUrlEncoded
   Single<RegisterResponse> userRegister(
           @Header("Authorization") String Authorization,
           @Field("email") String email,
           @Field("first_name") String first_name,
           @Field("mobile_number") String mobile_number,
           @Field("activation_code") int activation_code,
           @Field("operator") int operator,
           @Field("pay_monthly") String pay_monthly);

}
