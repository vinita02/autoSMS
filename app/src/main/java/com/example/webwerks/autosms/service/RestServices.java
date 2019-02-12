package com.example.webwerks.autosms.service;


import com.example.webwerks.autosms.model.request.LoginRequest;
import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.request.UpdateProfileRequest;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.BaseResponse;
import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.model.response.TermsResponse;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
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
        Single <RegisterResponse> registerObservable = apiServices.userRegister(request.getMobile_number(),request.getPassword(),request.getActivation_code(),
                request.getOperator(),request.getSim_type(),request.getSms_plan(),request.getBilling_date());

        RegisterResponse response = attachCommonRxProperiesAndExecute(registerObservable,RegisterResponse.class);
        return response;
    }

    public NetworkResponse networkOperatorList(){

        Single <NetworkResponse> networkResponse = apiServices.networkOperator();
        NetworkResponse response = attachCommonRxProperiesAndExecute(networkResponse,NetworkResponse.class);
        return response;
    }

    public ViewProfileResponse viewProfile(ViewProfileRequest request){

        Single <ViewProfileResponse> viewProfileResponse = apiServices.viewProfile(request.getToken());
        ViewProfileResponse response = attachCommonRxProperiesAndExecute(viewProfileResponse,ViewProfileResponse.class);
        return response;
    }

    public UpdateProfileResponse updateProfile(UpdateProfileRequest request){

        Single <UpdateProfileResponse> updateProfileResponse = apiServices.updateProfile(request.getToken(),request.getMobile_number(),request.getActivation_code(),
                request.getOperator(),request.getSim_type(),request.getSms_plan(),request.getBilling_date());
        UpdateProfileResponse response = attachCommonRxProperiesAndExecute(updateProfileResponse,UpdateProfileResponse.class);
        return response;
    }


    public TermsResponse getTermsandCondition(){

        Single <TermsResponse> termsResponse = apiServices.getTermsandCondition();
        TermsResponse response = attachCommonRxProperiesAndExecute(termsResponse,TermsResponse.class);
        return response;
    }


    private <E> E attachCommonRxProperiesAndExecute(Single<E> observable,final Class errorClass){
        return observable.subscribeOn(Schedulers.io())
                .onErrorReturn(new Function<Throwable, E>() {
                    @Override
                    public E apply(Throwable throwable) throws Exception {
                        BaseResponse response = (BaseResponse) errorClass.newInstance();
                        response.setResponse_code("-1");
                        response.setMessage(throwable.getMessage());
                        return (E) response;
                    }
                })
                .blockingGet();
    }
}
