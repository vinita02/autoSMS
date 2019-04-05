package com.example.webwerks.autosms.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.Contacts;
import com.example.webwerks.autosms.model.request.SendMessagesRequest;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.service.ForgroundService;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.viewmodel.MessagesViewModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.btnShareNow)
    Button btnShareNow;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.txtRewardPercent)
    TextView txtRewardPercent;
    int monthly_rewards;
    MessagesViewModel viewModel;
    SendMessagesRequest request = new SendMessagesRequest();
    ArrayList<Contacts.User> num = new ArrayList<>();
    ArrayList<SendMessagesResponse.Result> results = new ArrayList<>();
    SendMessagesResponse response = new SendMessagesResponse();
    private static final int PERMISSION_REQUEST = 100;
    String mobile;
    Context context;

    public static void open(Context context) {
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initViews() {
        context = this;
        //Prefs.setUserMobile(context,"447380447420");
        //checkSMSPermission(response);
        // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        Prefs.setLaunchActivity(DashboardActivity.this, "dashboardActivity");
        mobile = Prefs.getUserMobile(this);
        Log.d("TAGA",mobile);

        viewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //get user Messages
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMessages();
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    showToast("Enable Network State");
                } else {
                    request.setMobile_number(mobile);
                    viewModel.getMessages(request);
                }
            }
        }, 3000);

        Log.d("TAGA","onStart");
        String message = Prefs.getMonthlyRewards(context);
        setPercnatage(message);
        LocalBroadcastManager.getInstance(context).registerReceiver(
                mMessageReceiver, new IntentFilter("monthly_rewards"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    private void getMessages() {
        viewModel.getuserMessages().observe(this, new Observer<SendMessagesResponse>() {
            @Override
            public void onChanged(@Nullable SendMessagesResponse data) {

                if (data != null) {
                    if (data.getResponse_code().equals("200")) {

                        if (data.result.size() != 0) {
                            response = data;
                            checkSMSPermission(data);
                            Log.d("TAGA", "service call");
                        } else {
                            Log.d("TAGA", "service not call");
                        }
                    }
                }
            }
        });
    }

    private void sendMessages(SendMessagesResponse response) {

        Contacts.User user1 = new Contacts.User();
        user1.setId(340);
        user1.setMobile("8805429015");
        user1.setMessages("hiii abhijeet whatsup...");
        num.add(user1);

        /*SendMessagesResponse.Result result = new SendMessagesResponse.Result();
        result.setMessage_id(String.valueOf(1));
        result.setMobile_number("8805429015");
        result.setMessage_content("hiii abhijeet whatsup...");
        results.add(result);*/

        Gson gson = new Gson();
        String json = gson.toJson(num);
        String respo = gson.toJson(response);
        Intent serviceIntent = new Intent(this, ForgroundService.class);
        serviceIntent.putExtra("json_data", json);
        serviceIntent.putExtra("respo_data", respo);
        serviceIntent.putExtra("inputExtra", "Background task perform...");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @OnClick({R.id.btnShareNow, R.id.imgBack, R.id.imgUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareNow:
                ShareScreenActivity.open(context);
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgUser:
                MyProfileActivity.open(context);
                break;
        }
    }

    private void checkSMSPermission(SendMessagesResponse response) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Snackbar.make(findViewById(R.id.rl), "You need to grant SEND SMS permission to send sms",
                            Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST);
                        }
                    }).show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST);
                }
            } else {
                sendMessages(response);
            }
        } else {
            sendMessages(response);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            sendMessages(response);
        } else {

            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Status");
            Log.d("TAGA", String.valueOf(message));
            setPercnatage(message);
        }
    };

    private void setPercnatage(String message) {
        if (message.equals("")){
            message = "0";
        }

        txtRewardPercent.setText(message + "%");
    }
}