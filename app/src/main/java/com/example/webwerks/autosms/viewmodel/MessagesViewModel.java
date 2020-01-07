package com.example.webwerks.autosms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.webwerks.autosms.model.request.SendMessagesRequest;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.service.RestServices;

public class MessagesViewModel extends ViewModel {


    private MutableLiveData<SendMessagesResponse> getData;


    public LiveData<SendMessagesResponse> getuserMessages(){

        if (getData == null){
            getData = new MutableLiveData<>();
        }

        return getData;
    }


    public void getMessages(final SendMessagesRequest request){

        final SendMessagesResponse response = RestServices.getInstance().sendMessagesResponse(request);
        getData.setValue(response);
    }
}
