package com.example.webwerks.autosms.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.service.RestServices;

public class RegisterViewModel extends ViewModel {


    private MutableLiveData<RegisterResponse> registerData;


    public LiveData<RegisterResponse> getRegisterResponse(){

        if (registerData == null){
            registerData = new MutableLiveData<>();
        }

        return registerData;
    }


    public void register(final RegisterRequest request){

        final RegisterResponse response = RestServices.getInstance().registerUser(request);
        registerData.setValue(response);

    }
}
