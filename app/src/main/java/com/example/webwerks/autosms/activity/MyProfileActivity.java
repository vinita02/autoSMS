package com.example.webwerks.autosms.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.utils.DateFormat;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.viewmodel.ViewProfileViewModel;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "MyProfileActivity";
    TextView txtMobileNumber, txtValidationCode;
    Spinner spinner;
    ImageView imgBack, imgDate;
    EditText etDate;
    Button btnUpdate, btnCancel;
    ViewProfileViewModel viewModel;
    String token;
    ViewProfileRequest request = new ViewProfileRequest();
    RadioButton radioMonthly, radioPayg, radioYes, radioNo;
    String smsPlan, paymentOpt, mobile, validationCode, billingDate, operatorId;


    public static void open(Context context) {
        context.startActivity(new Intent(context, MyProfileActivity.class));
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews() {

        token = Prefs.getToken(getApplicationContext());
        Log.d(TAG, token);

//        viewModel = ViewModelProviders.of(this).get(ViewProfileViewModel.class);
//        getProfileData();
//        request.setToken(token);
//        viewModel.viewProfile(request);

        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtValidationCode = findViewById(R.id.txtValidationCode);
        spinner = findViewById(R.id.spinner);
        etDate = findViewById(R.id.etDate);
        radioYes = findViewById(R.id.radioYes);
        radioNo = findViewById(R.id.radioNo);
        imgBack = findViewById(R.id.imgBack);
        imgDate = findViewById(R.id.imgDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        radioMonthly = findViewById(R.id.radioMonthly);
        radioPayg = findViewById(R.id.radioPayg);
        imgBack.setOnClickListener(this);
        imgDate.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        radioYes.setOnClickListener(this);
        radioNo.setOnClickListener(this);
        radioMonthly.setOnClickListener(this);
        radioPayg.setOnClickListener(this);

        String value = "Yes";
        if (value.contains("Yes")) {
            radioYes.setChecked(true);
            smsPlan = "1";
        } else {
            radioNo.setChecked(true);
            smsPlan = "0";
        }

        String option = "payg";
        if (option.contains("payg")) {
            radioPayg.setChecked(true);
            paymentOpt = "payg";
        } else {
            radioMonthly.setChecked(true);
            paymentOpt = "paymonthly";
        }

    }


    private void getProfileData() {
        viewModel.getProfileData().observe(this, new Observer<ViewProfileResponse>() {
            @Override
            public void onChanged(@Nullable ViewProfileResponse response) {
                if (response != null) {
                    if (response.getResponse_code().equals("200")) {
//                        Log.d(TAG,response.result)

                    } else {
                        Log.d(TAG, "error");
                        showToast(response.getMessage());
                    }
                }
            }
        });
    }

    @Override
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

        Log.d("TAGA", smsPlan);
        Log.d("TAGA", paymentOpt);
        Log.d("TAGA", billingDate);
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
