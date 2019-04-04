package com.example.webwerks.autosms.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.adapter.RegisterNetworkAdapter;
import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.utils.Progress;
import com.example.webwerks.autosms.utils.Validation;
import com.example.webwerks.autosms.viewmodel.RegisterViewModel;
import com.rilixtech.CountryCodePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {

    private static String TAG = "RegisterActivity";
    @BindView(R.id.etMobile)
    EditText etmobile;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etValidationCode)
    EditText etValidationCode;
    @BindView(R.id.imgMobilereward)
    ImageView imgMobilereward;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    /* @BindView(R.id.spDate)
     Spinner spDate;*/
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.rgSmsplan)
    RadioGroup rgSmsplan;
    @BindView(R.id.rgMontlyopt)
    RadioGroup rgMontlyopt;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.txtTerms)
    TextView txtTerms;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.spCountrycode)
    CountryCodePicker spCountrycode;
    RadioButton smsUnlimed, payOption;
    RegisterViewModel viewModel;
    RegisterRequest request = new RegisterRequest();
    String smsPlan, paymentOpt, mobile, validationCode, billingDate, operatorId, password, prefix;
    ArrayList<NetworkResponse.Operators> networkList = new ArrayList<>();
    boolean checkReward = true;
    private Progress progress;
    Context context;
   // private ProgressBar pBar;
  //  ProgressDialog dialog;


    public static void open(LoginActivity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        context = this;
       // pBar = (ProgressBar) findViewById(R.id.pBar);
       // pBar.setVisibility(View.VISIBLE);
        //dialog = new ProgressDialog(this);
       // dialog.setTitle("Loading");
        //dialog.show();
        progress = new Progress(this, root);
        Prefs.setLaunchActivity(RegisterActivity.this, "registerActivity");
        progress.showProgresBar();
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        //List of NetworkOperators
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNetworkList();
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    showToast("Enable Network State");
                } else {
                    viewModel.fetchNetworkOperator();
                }
            }
        }, 3000);
        viewModel.getRegisterResponse().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(@Nullable RegisterResponse response) {
                if (response != null) {
                    //progress.hideProgressBar();
                   // dialog.hide();
                    Log.d("PROGRESS", "onChanged");
                   // pBar.setVisibility(View.GONE);
                    if (response.getResponse_code().equals("200")) {
                        Prefs.setToken(context, response.result.token);
                        Prefs.setUserMobile(context, response.result.mobile_number);
                        Log.d(TAG, response.result.activation_code);
                        Prefs.setActivationCode(context, response.result.activation_code);
                        DashboardActivity.open(context);
                        finish();
                        showToast(response.getMessage());
                    } else {
                        Log.d(TAG, "error");
                        showToast(response.getMessage());
                    }
                }
            }
        });
    }


    private void getNetworkList() {
        //pBar.setVisibility(View.VISIBLE);
        viewModel.getNetworkList().observe(this, new Observer<NetworkResponse>() {
            @Override
            public void onChanged(@Nullable final NetworkResponse response) {
                if (response != null) {
                    //dialog.hide();

                    if (response.getResponse_code().equals("200")) {
                        final NetworkResponse.Operators operators = new NetworkResponse.Operators();
                        operators.setId("0");
                        operators.setOperator_name("Select Mobile network");
                        networkList.add(operators);
                        if (response.result.operators.size() != 0) {
                            setOperators(response);

                        } else {
                            Log.d("TAGA", response.getMessage());
                        }
                    } else {
                        showToast(response.getMessage());
                    }
                }
            }
        });

    }

    private void setOperators(NetworkResponse response) {
        networkList.addAll(response.result.getOperators());
        Log.d("TAGA", networkList.toString());
        /*final MobileNetworkAdapter adapter = new MobileNetworkAdapter(getApplicationContext(), R.layout.spinner_network, networkList);
        spinner.setAdapter(adapter);*/
        final RegisterNetworkAdapter adapter = new RegisterNetworkAdapter(context,networkList);
        spinner.setAdapter(adapter);
        //simple_spinner_item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operatorId = adapter.getItem(i).getId();
                Log.d("TAGA", operatorId);
                Log.d("TAGA", adapter.getItem(i).getOperator_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("TAGA", "nothing");
            }
        });
        //pBar.setVisibility(View.GONE);
        progress.hideProgressBar();
    }

    @OnClick({R.id.btnRegister, R.id.imgMobilereward, R.id.imgBack, R.id.txtTerms})
    public void clickListener(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;
            case R.id.imgMobilereward:
                unableReward();
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtTerms:
                TermsAndConditionActivity.open(this);
                break;
        }
    }

    private void register() {
        spCountrycode.registerPhoneNumberTextView(etmobile);
        prefix = spCountrycode.getSelectedCountryCode();
        String mobile = getMobileNumber();
        Log.d("number", mobile);
        validationCode = etValidationCode.getText().toString();
        password = etPassword.getText().toString();

        if (!spCountrycode.isValid()) {
            showToast("Mobile number not valid");
        } else if (Validation.isValidMobilenetwork(operatorId)) {
            showToast("Select Mobile network");
        } else if (!ischeckPaymentopt()) {
            showToast("Select Mobile sim");
        } else if (!ischeckSMSplan()) {
            showToast("Select SMS plan");
        } else if (!Validation.isValidValidationcode(validationCode)) {
            showToast("enter Validation code");
        } else {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
            } else {
                //progress.showProgresBar();
               // dialog.show();
               // pBar.setVisibility(View.VISIBLE);
                Log.d("TAGAGA",mobile);
               // request.setMobile_number(prefix + mobile);
                request.setMobile_number(mobile);
                request.setActivation_code(validationCode);
                request.setOperator(Integer.parseInt(operatorId));
                request.setSim_type(paymentOpt);
                request.setSms_plan(smsPlan);
                //request.setBilling_date("0");
                viewModel.register(request);
            }
        }
    }

    private String getMobileNumber() {
        String mobileNumber;
        mobile = etmobile.getText().toString();
        char first_char = mobile.charAt(0);
        Log.d("TAGA", String.valueOf(first_char));
        if (first_char=='0'){
            mobileNumber = prefix + mobile.substring(1);
        }else {
            mobileNumber = prefix + mobile;
        }
        return mobileNumber;
    }

    //Mobile reward
    private void unableReward() {
        if (checkReward) {
            checkReward = false;
            imgMobilereward.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.reward_unselect));
        } else {
            checkReward = true;
            imgMobilereward.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.reward_select));
        }
    }

    //SIM option
    private boolean ischeckPaymentopt() {
        payOption = rgMontlyopt.findViewById(rgMontlyopt.getCheckedRadioButtonId());
        if (rgMontlyopt.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            String getValue = payOption.getText().toString();
            if (getValue.contains("Monthly")) {
                paymentOpt = "paymonthly";
            } else {
                paymentOpt = "payg";
            }
            return true;
        }
    }

    //SMS plan
    private boolean ischeckSMSplan() {
        smsUnlimed = rgSmsplan.findViewById(rgSmsplan.getCheckedRadioButtonId());
        if (rgSmsplan.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            String getValue = smsUnlimed.getText().toString();

            if (getValue.contains("Yes")) {
                smsPlan = "1";
            } else {
                smsPlan = "0";
            }
            return true;
        }
    }
}
