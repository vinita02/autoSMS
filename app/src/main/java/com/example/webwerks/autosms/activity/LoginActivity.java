package com.example.webwerks.autosms.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.request.LoginRequest;
import com.example.webwerks.autosms.model.response.LoginResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.Validation;
import com.example.webwerks.autosms.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "LoginActivity";
    EditText etEmail, etPassword;
    Button btnLogin,btnRegister,btnViewProfile;
    String email, password;
    LoginViewModel viewModel;
    LoginRequest request = new LoginRequest();


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnViewProfile.setOnClickListener(this);

        viewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse response) {
                if (response != null) {
                    if (response.getResponse_code().equals("200")){
                        Log.d(TAG,response.result.token);
                        Log.d(TAG,response.result.email);
                        Log.d(TAG,response.result.first_name);
                        Log.d(TAG,response.result.user_id);
                        Log.d(TAG,response.result.created_at.date);
                        Log.d(TAG,response.result.updated_at.date);
                        if(response.result.deleted_at !=null){
                            Log.d(TAG,response.result.deleted_at);
                        }
                        showToast(response.getMessage());
                    }else {
                         Log.d(TAG,response.getMessage());
                         Log.d(TAG,response.result.message);
                         showToast(response.result.message);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;

            case R.id.btnRegister:
                 RegisterActivity.open(this);
//                MyProfileActivity.open(this);
                break;

            case R.id.btnViewProfile:
//                MyProfileActivity.open(this);
                DashboardActivity.open(this);
                break;


        }
    }

    private void login() {

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (!Validation.isValidEmail(email)) {
            showToast("Enter valid email");
        } else if (!Validation.isValidPassword(password)) {
            showToast("Enter correct password");
        } else {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
            } else {
                request.setAuthorization(DEVICE_ID);
                request.setEmailId(email);
                request.setPassword(password);
                viewModel.login(request);
            }
        }
    }
}
