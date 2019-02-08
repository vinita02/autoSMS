package com.example.webwerks.autosms.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.adapter.MobileNetworkAdapter;
import com.example.webwerks.autosms.model.request.RegisterRequest;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.RegisterResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.DateFormat;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.utils.Validation;
import com.example.webwerks.autosms.viewmodel.RegisterViewModel;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "RegisterActivity";
    EditText etMobile, etDate, etValidationCode;
    ImageView imgMobilereward, imgBack, imgDate;
    Spinner spinner;
    RadioGroup rgSmsplan, rgMontlyopt;
    RadioButton smsUnlimed, payOption;
    Button btnRegister;
    RegisterViewModel viewModel;
    RegisterRequest request = new RegisterRequest();
    String smsPlan, paymentOpt, mobile, validationCode, billingDate, operatorId;
    ArrayList<NetworkResponse.Operators> networkList = new ArrayList<>();
    boolean checkReward = true;
    public int startDay, startMonth, startYear;
    TextView txtTerms;


    public static void open(LoginActivity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initViews() {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        //List of NetworkOperators
        getNetworkList();
        if (!CheckNetwork.isConnected(this)) {
            showToast("Enable Network State");
        }else {
            viewModel.fetchNetworkOperator();
        }
        etMobile = findViewById(R.id.etMobile);
        etDate = findViewById(R.id.etDate);
        rgSmsplan = findViewById(R.id.rgSmsplan);
        etValidationCode = findViewById(R.id.etValidationCode);
        imgMobilereward = findViewById(R.id.imgMobilereward);
        //network spinner
        spinner = findViewById(R.id.spinner);
        btnRegister = findViewById(R.id.btnRegister);
        imgDate = findViewById(R.id.imgDate);
        imgBack = findViewById(R.id.imgBack);
        rgMontlyopt = findViewById(R.id.rgMontlyopt);
        txtTerms = findViewById(R.id.txtTerms);
        //onclick
        imgMobilereward.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        imgDate.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtTerms.setOnClickListener(this);


        viewModel.getRegisterResponse().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(@Nullable RegisterResponse response) {
                // Log.d(TAG, "onChanged"+registerResponse.result.token);

                if (response != null) {
                    if (response.getResponse_code().equals("200")) {
                        Prefs.setToken(getApplicationContext(), response.result.token);
                        Prefs.setUserMobile(getApplicationContext(), response.result.mobile_number);
                        MyProfileActivity.open(getApplicationContext());
                        finish();
                        showToast(response.getMessage());
                    } else {
                        Log.d(TAG, "error");
                        showToast(response.result.message);
                    }
                }
            }
        });
    }

    private void getNetworkList() {

        viewModel.getNetworkList().observe(this, new Observer<NetworkResponse>() {
            @Override
            public void onChanged(@Nullable final NetworkResponse response) {
                if (response != null) {
                    if (response.getResponse_code().equals("200")) {

                        final NetworkResponse.Operators operators = new NetworkResponse.Operators();
                        operators.setId("0");
                        operators.setOperator_name("Select Mobile network");
                        networkList.add(operators);

                        if (response.result.operators.size() != 0) {

                            networkList.addAll(response.result.getOperators());
                            Log.d("TAGA",networkList.toString());

                            final MobileNetworkAdapter adapter = new MobileNetworkAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, networkList);
                            spinner.setAdapter(adapter);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    operatorId = adapter.getItem(i).id;
                                    Log.d("TAGA", operatorId);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    Log.d("TAGA", "nothing");
                                }
                            });

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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRegister:
                register();
                break;

            case R.id.imgDate:
                billingDate();
                break;

            case R.id.imgMobilereward:
                unableReward();
                break;

            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.etDate:
                hideKeyboard();
                break;

            case R.id.txtTerms:
                TermsAndConditionActivity.open(this);
                break;
        }
    }

    private void register() {

        mobile = etMobile.getText().toString();
        validationCode = etValidationCode.getText().toString();


        if (!Validation.isValidMobile(mobile)) {
            showToast("Enter valid Mobile number");
        } else if (Validation.isValidMobilenetwork(operatorId)) {
            showToast("Select Mobile network");
        } else if (!ischeckPaymentopt()) {
            showToast("Select Mobile sim");
        } else if (!ischeckSMSplan()) {
            showToast("Select SMS plan");
        } else if (!Validation.isValidValidationcode(validationCode)) {
            showToast("enter Validation code");
        } /*else if (!checkReward) {
            showToast("reward service disable");
        }*/ else {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
            } else {
                request.setMobile_number(mobile);
                request.setActivation_code(validationCode);
                request.setOperator(Integer.parseInt(operatorId));
                request.setSim_type(paymentOpt);
                request.setSms_plan(smsPlan);
                request.setBilling_date(billingDate);
                viewModel.register(request);
            }
        }
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

    //Billing Date
    public void billingDate() {

        try {

            int mYear, mMonth, mDay = 0;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            long minDate = c.getTime().getTime();
            final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            DateFormatSymbols symbols = new DateFormatSymbols(Locale.CANADA);
                            String[] monthNames = symbols.getMonths();
                            String month = monthNames[monthOfYear];
                            startDay = dayOfMonth;
                            startMonth = monthOfYear;
                            startYear = year;
                            billingDate = startMonth + "-" + startDay + "-" + startYear;
                            String date = DateFormat.Date(billingDate);
                            etDate.setText(date);
                            Log.d(TAG, date);
                        }
                    }, mYear, mMonth, mDay);

            Calendar calendar = Calendar.getInstance();
            calendar.set(2017, 0, 1);
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.show();


        } catch (Exception e) {
            //e.printStackTrace();
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
