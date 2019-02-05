package com.example.webwerks.autosms.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.utils.Validation;
import com.example.webwerks.autosms.viewmodel.ViewProfileViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "MyProfileActivity";
    TextView txtMobileNumber,txtValidationCode;
    Spinner spinner;
    RadioGroup rgSmsplan, rgMontlyopt;
    ImageView imgBack, imgDate;
    EditText etDate;
    Button btnUpdate,btnCancel;
    ViewProfileViewModel viewModel;
    String token;
    ViewProfileRequest request = new ViewProfileRequest();

    public static void open(Context context) {
        context.startActivity(new Intent(context,MyProfileActivity.class));
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews() {

        token = Prefs.getToken(getApplicationContext());
        Log.d(TAG,token);

        viewModel = ViewModelProviders.of(this).get(ViewProfileViewModel.class);
        getProfileData();
        request.setToken(token);
        viewModel.viewProfile(request);

        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtValidationCode = findViewById(R.id.txtValidationCode);
        spinner = findViewById(R.id.spinner);
        rgSmsplan = findViewById(R.id.rgSmsplan);
        rgMontlyopt = findViewById(R.id.rgMontlyopt);
        imgBack = findViewById(R.id.imgBack);
        imgDate = findViewById(R.id.imgDate);
        etDate = findViewById(R.id.etDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

    }


    private void getProfileData(){
        viewModel.getProfileData().observe(this, new Observer<ViewProfileResponse>() {
            @Override
            public void onChanged(@Nullable ViewProfileResponse response) {
                if (response !=null){
                    if (response.getResponse_code().equals("200")){
//                        Log.d(TAG,response.result)

                    }else {
                        Log.d(TAG,"error");
                        showToast(response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;

            case R.id.imgDate:
//                billingDate();
                break;

            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.etDate:
                hideKeyboard();
                break;
        }
    }

    private void register() {

    }
}
