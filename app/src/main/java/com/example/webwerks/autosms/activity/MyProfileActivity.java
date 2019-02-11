package com.example.webwerks.autosms.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.adapter.OperatorsAdapter;
import com.example.webwerks.autosms.model.request.UpdateProfileRequest;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.DateFormat;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.viewmodel.MyProfileViewModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyProfileActivity extends BaseActivity {

    private static String TAG = "MyProfileActivity";
    @BindView(R.id.txtMobileNumber) TextView txtMobileNumber;
    @BindView(R.id.txtValidationCode) TextView txtValidationCode;
    @BindView(R.id.spinner) Spinner spinner;
    @BindView(R.id.imgBack) ImageView imgBack;
    @BindView(R.id.imgDate) ImageView imgDate;
    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.btnCancel) Button btnCancel;
    @BindView(R.id.radioMonthly) RadioButton radioMonthly;
    @BindView(R.id.radioPayg) RadioButton radioPayg;
    @BindView(R.id.radioYes) RadioButton radioYes;
    @BindView(R.id.radioNo) RadioButton radioNo;
    MyProfileViewModel viewModel;
    String token, mobile;
    UpdateProfileRequest updateRequest = new UpdateProfileRequest();
    ViewProfileRequest viewRequest = new ViewProfileRequest();
    String smsPlan, paymentOpt, validationCode, billingDate, operatorId;
    ArrayList<ViewProfileResponse.Operators> networkList = new ArrayList<>();

    public static void open(Context context) {
        context.startActivity(new Intent(context, MyProfileActivity.class));}
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }
    @Override
    protected void initViews() {
        //token = Prefs.getToken(getApplicationContext());
        //token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hdXRvc21zLnBocC1kZXYuaW5cL2F1dG8tc21zLWFwcFwvcHVibGljXC9hcGlcL3YxXC91c2VyXC9yZWdpc3RlciIsImlhdCI6MTU0OTYwNDM5OSwiZXhwIjoxNTUwODEzOTk5LCJuYmYiOjE1NDk2MDQzOTksImp0aSI6Ijd1VlRyYVhqTFRrdlZ1cjEiLCJzdWIiOjE1LCJwcnYiOiIzMjk2M2E2MDZjMmYxNzFmMWMxNDMzMWU3Njk3NjZjZDU5MTJlZDE1In0.JhcQAYBgIAyuHeCAUOeF_ahfXVbO7RzCPwp00f-d8Zk";
        token =  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hdXRvc21zLnBocC1kZXYuaW5cL2F1dG8tc21zLWFwcFwvcHVibGljXC9hcGlcL3YxXC91c2VyXC9yZWdpc3RlciIsImlhdCI6MTU0OTg3NDM1NywiZXhwIjoxNTUxMDgzOTU3LCJuYmYiOjE1NDk4NzQzNTcsImp0aSI6IjgzNFpvS0RGV1N2MGxkQk0iLCJzdWIiOjE3LCJwcnYiOiIzMjk2M2E2MDZjMmYxNzFmMWMxNDMzMWU3Njk3NjZjZDU5MTJlZDE1In0.59Ly77AiBylezt1OzH7cZpH8ydSL6aGCyKlOzdnPjE8";
        Log.d(TAG, token);
        viewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        //set View Profile
        showProgress();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                getProfileData();
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    showToast("Enable Network State");
                } else {
                    viewRequest.setToken(token);
                    viewModel.viewProfile(viewRequest);
                }
            }
        },3000);

        viewModel.getUpdateProfileData().observe(this, new Observer<UpdateProfileResponse>() {
            @Override
            public void onChanged(@Nullable UpdateProfileResponse response) {
                if (response != null) {
                    if (response.getResponse_code().equals("200")) {
                        Log.d(TAG, response.result.message);
                        showToast(response.result.message);
                        finish();
                    } else {
                        showToast(response.result.message);
                        Log.d(TAG, response.result.message);
                    }
                }
            }
        });
    }
    private void getProfileData() {
        viewModel.getViewProfileData().observe(this, new Observer<ViewProfileResponse>() {
            @Override
            public void onChanged(@Nullable ViewProfileResponse response) {
                if (response != null) {
                    if (response.getResponse_code().equals("200")) {

                        if (response.error != null) {
                            Log.d(TAG, response.error);
                        } else {
                            Log.d(TAG, response.result.getOperators().get(0).operator_name);
                            Log.d(TAG, response.getMessage());
                            setValues(response);
                            hideProgress();
                        }

                    } else {
                        Log.d(TAG, "error");
                        showToast(response.getMessage());
                    }
                }
            }
        });
    }

    public void setValues(ViewProfileResponse response){

        //set mobile number
        txtMobileNumber.setText(response.result.profile.mobile_number);
        mobile = txtMobileNumber.getText().toString();

        // set spinner
        if (response.result.operators.size() != 0) {
            networkList.addAll(response.result.getOperators());
            Log.d("TAGA", networkList.toString());
            final OperatorsAdapter adapter = new OperatorsAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, networkList);
            spinner.setAdapter(adapter);
            int id = Integer.parseInt(response.result.profile.operator_id);
            spinner.setSelection(id - 1);
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

            //set sms plan
            if (response.result.profile.sms_plan.contains("1")) {
                radioYes.setChecked(true);
                smsPlan = "1";
            } else {
                radioNo.setChecked(true);
                smsPlan = "0";
            }

            //set sim type

            if (response.result.profile.sim_type.contains("paymonthly")) {
                radioMonthly.setChecked(true);
                paymentOpt = "paymonthly";
            } else {
                radioPayg.setChecked(true);
                paymentOpt = "payg";
            }
            //set billing date
            etDate.setText(response.result.profile.billing_date);
            billingDate = etDate.getText().toString();

            //set activation code
            txtValidationCode.setText(response.result.activation_codes.code);
            validationCode = txtValidationCode.getText().toString();
        }
    }


    @OnClick({R.id.btnUpdate,R.id.radioYes,R.id.radioNo,R.id.radioMonthly,R.id.radioPayg,R.id.btnCancel,R.id.imgDate,R.id.imgBack,R.id.etDate})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnUpdate:
                update();
                break;
            case R.id.radioYes:
                smsPlan = "1";
                break;
            case R.id.radioNo:
                smsPlan = "0";
                break;
            case R.id.radioMonthly:
                paymentOpt = "paymonthly";
                break;
            case R.id.radioPayg:
                paymentOpt = "payg";
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.imgDate:
                billingDate();
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.etDate:
                hideKeyboard();
                break;
        }
    }

    private void update() {
        Log.d("TAGA", mobile);
        Log.d("TAGA", operatorId);
        Log.d("TAGA", paymentOpt);
        Log.d("TAGA", billingDate);
        Log.d("TAGA", smsPlan);
        Log.d("TAGA", validationCode);
        if (!CheckNetwork.isConnected(this)) {
            showToast("Enable Network State");
        } else {
            updateRequest.setToken(token);
            updateRequest.setMobile_number(mobile);
            updateRequest.setOperator(Integer.parseInt(operatorId));
            updateRequest.setSim_type(paymentOpt);
            updateRequest.setBilling_date(billingDate);
            updateRequest.setSms_plan(smsPlan);
            updateRequest.setActivation_code(validationCode);
            viewModel.updateProfile(updateRequest);
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
                            String date = startMonth + "-" + startDay + "-" + startYear;
                            billingDate = DateFormat.Date(date);
                            etDate.setText(billingDate);
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
}
