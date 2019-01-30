package com.example.webwerks.autosms.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
    EditText etEmail,etPassword;
    Button btnLogin;
    String email,password;
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
        btnLogin.setOnClickListener(this);

        viewModel.getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(@Nullable LoginResponse loginResponse) {
                Log.d(TAG, "onChanged");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                    login();
                break;
        }
    }

    private void login() {

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (!Validation.isValidEmail(email)){
            showToast("Enter valid email");
        }else if (!Validation.isValidPassword(password)){
            showToast("Enter correct password");
        }else {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
            } else {
                showToast("success");
                request.setAuthorization("");
                request.setEmailId(email);
                request.setPassword(Integer.parseInt(password));
                viewModel.login(request);
            }
        }
    }
}
