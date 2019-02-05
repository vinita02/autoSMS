package com.example.webwerks.autosms.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.service.RestServices;

public class ViewProfileViewModel extends ViewModel {


   // private MutableLiveData<ViewProfileResponse> registerData;
    private MutableLiveData<ViewProfileResponse> viewProfile;


//    public LiveData<ViewProfileResponse> getRegisterResponse(){
//
//        if (registerData == null){
//            registerData = new MutableLiveData<>();
//        }
//
//        return registerData;
//    }

    public LiveData<ViewProfileResponse> getProfileData(){

        if (viewProfile == null){
            viewProfile = new MutableLiveData<>();
        }

        return viewProfile;
    }



//    public void register(){
//
//        final ViewProfileResponse response = RestServices.getInstance().registerUser(request);
//        Log.d("TAGA",response.toString());
//        registerData.setValue(response);
//    }


    public void viewProfile(final ViewProfileRequest request){

        final ViewProfileResponse response = RestServices.getInstance().viewProfile(request);
        Log.d("TAGA",response.toString());
        viewProfile.setValue(response);
    }


}
