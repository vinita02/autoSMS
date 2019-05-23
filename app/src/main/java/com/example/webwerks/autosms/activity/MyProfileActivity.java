package com.example.webwerks.autosms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.adapter.OperatorsAdapter;
import com.example.webwerks.autosms.adapter.ProfileNetworkAdapter;
import com.example.webwerks.autosms.adapter.RegisterNetworkAdapter;
import com.example.webwerks.autosms.model.request.UpdateProfileRequest;
import com.example.webwerks.autosms.model.request.ViewProfileRequest;
import com.example.webwerks.autosms.model.response.UpdateProfileResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.DateFormat;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.utils.Progress;
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
    Context context;
    @BindView(R.id.txtMobileNumber)
    TextView txtMobileNumber;
    @BindView(R.id.txtValidationCode)
    TextView txtValidationCode;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.radioMonthly)
    RadioButton radioMonthly;
    @BindView(R.id.radioPayg)
    RadioButton radioPayg;
    @BindView(R.id.radioYes)
    RadioButton radioYes;
    @BindView(R.id.radioNo)
    RadioButton radioNo;
    @BindView(R.id.root)
    RelativeLayout root;
    Progress progress;
    ProgressDialog pDialog;
    AlertDialog.Builder builder;

    MyProfileViewModel viewModel;
    String token, mobile;
    UpdateProfileRequest updateRequest = new UpdateProfileRequest();
    ViewProfileRequest viewRequest = new ViewProfileRequest();
    String smsPlan, paymentOpt, validationCode, billingDate;
    int operatorId;
    ArrayList<ViewProfileResponse.Operators> networkList = new ArrayList<>();

    public static void open(Context context) {
        context.startActivity(new Intent(context, MyProfileActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews() {
        context = this;
        showProgress();
        progress = new Progress(this, root);
        progress.showProgresBar();
        token = Prefs.getToken(getApplicationContext());
        //token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC92MVwvdXNlclwvcmVnaXN0ZXIiLCJpYXQiOjE1NTgwNzY5NDQsIm5iZiI6MTU1ODA3Njk0NCwianRpIjoiSHA5WmgxOGpJMm1tdFJHbCIsInN1YiI6MjY2LCJwcnYiOiIzMjk2M2E2MDZjMmYxNzFmMWMxNDMzMWU3Njk3NjZjZDU5MTJlZDE1In0.mUlhxvBKgELu3qZehB0AdGlixDornHYPvT2LjA1tpU4";
        viewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        //set View Profile
        try{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
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
            }, 3000);

            //getUpdateResponse
            getUpdateProfileResponse();

        }catch (Exception e){
            Log.d("TAGA",e.getMessage());
        }
    }

    private void showProgress() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Uploading...");
    }


    private void getUpdateProfileResponse() {

        viewModel.getUpdateProfileData().observe(this, new Observer<UpdateProfileResponse>() {
            @Override
            public void onChanged(@Nullable final UpdateProfileResponse response) {
                if (response != null) {
                            if (response.getResponse_code().equals("200")) {
                                pDialog.dismiss();
                                showToast(response.getMessage());
                                finish();
                            } else {
                                Log.d("TAGA","error");
                                showToast(response.getMessage());
                                pDialog.dismiss();
                            }
                        }
            }
        });
    }

    private void getProfileData() {
        viewModel.getViewProfileData().observe(this, new Observer<ViewProfileResponse>() {
            @Override
            public void onChanged(@Nullable ViewProfileResponse response) {
                try {
                    if (response != null) {
                        if (response.getResponse_code().equals("200")) {
                                Prefs.setToken(context,response.result.token);
                                //Log.d("TAGA",Prefs.getToken(context));
                                setValues(response);

                        } else {
//                            showToast(response.getMessage());
                            progress.hideProgressBar();
                            userNotFoundPopup(response.getMessage());
                        }
                    }
                }catch (Exception e){
                    Log.d("TAGA",e.getMessage());
                }
            }
        });
    }

    private void userNotFoundPopup(String message) {

        builder = new AlertDialog.Builder(this);
        builder.setTitle(message + ".");
        builder.setMessage("Please Register again.");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                  finish();
//                  moveTaskToBack(true);
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setValues(ViewProfileResponse response) {
        //set mobile number
        txtMobileNumber.setText(response.result.profile.mobile_number);
        mobile = txtMobileNumber.getText().toString();

        // set spinner
        if (response.result.operators.size() != 0) {
            networkList.addAll(response.result.getOperators());
            final ProfileNetworkAdapter adapter = new ProfileNetworkAdapter(context,networkList);
            spinner.setAdapter(adapter);
            int id = response.result.profile.operator_id;
            int pos = getSelectedtOperatorPosition(id,networkList);
            spinner.setSelection(pos);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    operatorId = adapter.getItem(i).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
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
            //set activation code
            txtValidationCode.setText(response.result.activation_codes.code);
            validationCode = txtValidationCode.getText().toString();
        }
        progress.hideProgressBar();
    }

    private int getSelectedtOperatorPosition(int id, ArrayList<ViewProfileResponse.Operators> networkList) {
          int pos = -1;
          for (int i=0;i<networkList.size();i++){
              ViewProfileResponse.Operators operators = networkList.get(i);
              if (operators.id==id){
                  pos = i;
                  break;
              }
          }
          return pos;
    }


    @OnClick({R.id.btnUpdate, R.id.radioYes, R.id.radioNo, R.id.radioMonthly, R.id.radioPayg, R.id.btnCancel, R.id.imgBack})
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
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void update() {
        try {
            if (!CheckNetwork.isConnected(this)) {
                showToast("Enable Network State");
                return;
            }
            pDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateRequest.setToken(token);
                    updateRequest.setMobile_number(mobile);
                    updateRequest.setOperator(operatorId);
                    updateRequest.setSim_type(paymentOpt);
                    updateRequest.setSms_plan(smsPlan);
                    updateRequest.setActivation_code(validationCode);
                    viewModel.updateProfile(updateRequest);
                }
            },2000);

        }catch (Exception e){
           //Log.d("TAGA",e.getMessage());
        }
    }
}
