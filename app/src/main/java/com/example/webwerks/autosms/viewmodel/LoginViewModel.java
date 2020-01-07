package com.example.webwerks.autosms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.webwerks.autosms.model.request.LoginRequest;
import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.service.RestServices;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginResponse> loginData;

    public LiveData<LoginResponse> getLoginResponse(){

        if (loginData ==null){
            loginData = new MutableLiveData<>();
        }
        return loginData;
    }


    public void login(final LoginRequest request){

        final LoginResponse response = RestServices.getInstance().loginUser(request);
        loginData.setValue(response);
    }
}
