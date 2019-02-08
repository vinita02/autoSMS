package com.example.webwerks.autosms.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.webwerks.autosms.model.request.UpdateProfileRequest;
import com.example.webwerks.autosms.model.response.TermsResponse;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.service.RestServices;

public class TermsandConditionViewModel extends ViewModel {


    private MutableLiveData<TermsResponse> getContent;


    public LiveData<TermsResponse> getContent(){

        if (getContent == null){
            getContent = new MutableLiveData<>();
        }

        return getContent;
    }




    public void getTermsandCondtion(){

        final TermsResponse response = RestServices.getInstance().getContent();
        Log.d("TAGA",response.toString());
        getContent.setValue(response);
    }
}
