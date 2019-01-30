package com.example.webwerks.autosms.service;


import com.example.webwerks.autosms.model.request.LoginRequest;
import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.BaseResponse;
import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RestServices {

    ApiServices apiServices;
    private static RestServices instance;

    private RestServices(){
        apiServices = ApiClient.getInstance().getApiServices();
    }

    public static RestServices getInstance(){

        if (instance == null){
            instance = new RestServices();
        }

        return instance;
    }


    public LoginResponse loginUser(LoginRequest request){
        Single<LoginResponse> loginObservable = apiServices.userLogin(request.getAuthorization(),
                request.getEmailId(),request.getPassword());

        LoginResponse response = attachCommonRxProperiesAndExecute(loginObservable,LoginResponse.class);

        return response;
    }

    public RegisterResponse registerUser(RegisterRequest request){

        Single <RegisterResponse> registerObservable = apiServices.userRegister(request.getAuthorization(),
                request.getEmailId(),request.getFirstName(),request.getMobileNumber()
                ,request.getActivecode(),request.getOperator(),request.getPaymentOption());

        RegisterResponse response = attachCommonRxProperiesAndExecute(registerObservable,RegisterResponse.class);

        return response;
    }


    private <E> E attachCommonRxProperiesAndExecute(Single<E> observable,final Class errorClass){
        return observable.subscribeOn(Schedulers.io())
                .onErrorReturn(new Function<Throwable, E>() {
                    @Override
                    public E apply(Throwable throwable) throws Exception {
                        BaseResponse response = (BaseResponse) errorClass.newInstance();
                        response.setResponse_code(response.getResponse_code());
                        response.setMessage(response.getMessage());
                        return (E) response;
                    }
                })
                .blockingGet();
    }
}
