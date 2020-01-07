package com.example.webwerks.autosms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.service.RestServices;

public class RegisterViewModel extends ViewModel {


    private MutableLiveData<RegisterResponse> registerData;
    private MutableLiveData<NetworkResponse> fetchNetwokrList;


    public LiveData<RegisterResponse> getRegisterResponse(){

        if (registerData == null){
            registerData = new MutableLiveData<>();
        }

        return registerData;
    }

    public LiveData<NetworkResponse> getNetworkList(){

        if (fetchNetwokrList == null){
            fetchNetwokrList = new MutableLiveData<>();
        }

        return fetchNetwokrList;
    }



    public void register(final RegisterRequest request){

        final RegisterResponse response = RestServices.getInstance().registerUser(request);
        registerData.setValue(response);
    }


    public void fetchNetworkOperator(){

        final NetworkResponse response = RestServices.getInstance().networkOperatorList();
        fetchNetwokrList.setValue(response);
    }


}
