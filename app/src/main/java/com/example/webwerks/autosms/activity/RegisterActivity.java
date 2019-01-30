package com.example.webwerks.autosms.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.Validation;
import com.example.webwerks.autosms.viewmodel.RegisterViewModel;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "RegisterActivity";
    EditText etEmail, etFirstname, etMobile, etActicode, etOperator;
    RadioGroup radioGroup;
    RadioButton paymentOption;
    Button btnRegister;
    RegisterViewModel viewModel;
    RegisterRequest request = new RegisterRequest();
    String email, firstName, mobile, actionCode, operator, paymentMode;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initViews() {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        etEmail = findViewById(R.id.etEmail);
        etFirstname = findViewById(R.id.etFirstname);
        etMobile = findViewById(R.id.etMobile);
        etActicode = findViewById(R.id.etActicode);
        etOperator = findViewById(R.id.etOperator);
        radioGroup = findViewById(R.id.radioGroup);
        //radioGroup.clearCheck();
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        viewModel.getRegisterResponse().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(@Nullable RegisterResponse registerResponse) {
                Log.d(TAG, "onChanged");
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {

        email = etEmail.getText().toString();
        firstName = etFirstname.getText().toString();
        mobile = etMobile.getText().toString();
        actionCode = etActicode.getText().toString();
        operator = etOperator.getText().toString();

        if (!Validation.isValidEmail(email)) {
            showToast("Enter valid email");
        } else if (!Validation.isValidName(firstName)) {
            showToast("Enter your firstName");
        } else if (!Validation.isValidMobile(mobile)) {
            showToast("Enter valid Mobile number");
        } else if (!Validation.isValidActivecode(actionCode)) {
            showToast("Enter Activation Code");
        } else if (!Validation.isValidOperator(operator)) {
            showToast("Enter Operator");
        } else if (!ischeckPaymentMode()) {
            showToast("Select payment method");
        } else {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
            } else {
                showToast("success");
                Log.d(TAG, paymentMode);
                request.setAuthorization("");
                request.setEmailId(email);
                request.setFirstName(firstName);
                request.setMobileNumber(mobile);
                request.setActivecode(Integer.parseInt(actionCode));
                request.setOperator(Integer.parseInt(operator));
                request.setPaymentOption(paymentMode);
                viewModel.register(request);
            }
        }
    }


    private boolean ischeckPaymentMode() {

        paymentOption = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Log.d(TAG, "not check");
            return false;
        } else {
            Log.d(TAG, "checked");
            paymentMode = paymentOption.getText().toString();
            return true;
        }
    }

}
