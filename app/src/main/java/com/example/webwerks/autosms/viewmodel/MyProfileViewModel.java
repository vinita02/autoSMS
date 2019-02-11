package com.example.webwerks.autosms.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.webwerks.autosms.model.request.UpdateProfileRequest;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.service.RestServices;

public class MyProfileViewModel extends ViewModel {


    private MutableLiveData<UpdateProfileResponse> updateProfile;
    private MutableLiveData<ViewProfileResponse> viewProfile;


    public LiveData<UpdateProfileResponse> getUpdateProfileData(){

        if (updateProfile == null){
            updateProfile = new MutableLiveData<>();
        }

        return updateProfile;
    }

    public LiveData<ViewProfileResponse> getViewProfileData(){

        if (viewProfile == null){
            viewProfile = new MutableLiveData<>();
        }

        return viewProfile;
    }



    public void updateProfile(final UpdateProfileRequest request){

        final UpdateProfileResponse response = RestServices.getInstance().updateProfile(request);
        updateProfile.setValue(response);
    }


    public void viewProfile(final ViewProfileRequest request){

        final ViewProfileResponse response = RestServices.getInstance().viewProfile(request);
        viewProfile.setValue(response);
    }


}
