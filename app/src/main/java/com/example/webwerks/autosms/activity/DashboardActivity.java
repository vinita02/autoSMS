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
import com.example.webwerks.autosms.service.MyWorker;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.viewmodel.MessagesViewModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
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
    Observer SendMessagesResponse;
    Boolean checkResponse = false;

    public static void open(Context context) {
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initViews() {
        Log.d("TAGA", "initViews");
        context = this;
       // Prefs.setUserMobile(context,"447719014485");
        checkSMSPermission(response,"initViews");
        Prefs.setLaunchActivity(DashboardActivity.this, "dashboardActivity");
       // mobile = Prefs.getUserMobile(this);
        //Log.d("TAGA",mobile);

        viewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);

//        SendMessagesResponse =  new Observer<SendMessagesResponse>() {
//            @Override
//            public void onChanged(@Nullable SendMessagesResponse data) {
//                try {
//                    if (data != null) {
//                        if (data.getResponse_code().equals("200")) {
//
//                            if (data.result.size() != 0) {
//                                response = data;
//                                Log.d("TAGA", "onChanged");
//                                checkResponse = true;
//                                checkSMSPermission(data,"onChanged");
//                                //Log.d("TAGA", "service call");
//                            } else {
//                                //Log.d("TAGA", "service not call");
//                            }
//                        }
//                    }
//                }catch (Exception e){
//                    Log.d("TAGA",e.getMessage());
//                }
//            }
//        };
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //get user Messages
//        try {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getMessages();
//                    if (!CheckNetwork.isConnected(getApplicationContext())) {
//                        showToast("Enable Network State");
//                    } else {
//                        request.setMobile_number(mobile);
//                        viewModel.getMessages(request);
//                    }
//                }
//            }, 3000);
//
//            String message = Prefs.getMonthlyRewards(context);
//            setRewardPercentage(message);
//            LocalBroadcastManager.getInstance(context).registerReceiver(
//                    mMessageReceiver, new IntentFilter("monthly_rewards"));
//        }catch (Exception e){
//            Log.d("TAGA",e.getMessage());
//        }
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        String message = Prefs.getMonthlyRewards(context);
        setRewardPercentage(message);
        LocalBroadcastManager.getInstance(context).registerReceiver(
                mMessageReceiver, new IntentFilter("monthly_rewards"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    private void getMessages() {
        Log.d("TAGA","getMessages");
        viewModel.getuserMessages().observe(this,SendMessagesResponse);
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

    private void checkSMSPermission(SendMessagesResponse response,String from) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Snackbar.make(findViewById(R.id.rl), "You need to grant SEND SMS permission to send sms",
                            Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                        }
                    }).show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                }
            } else {
                  startWorkMangr();
//                if (from.contains("onChanged")){
//                    Log.d("TAGA","onChanged 1");
//                    startService(response);
//                }
            }
        } else {
            startWorkMangr();
//            if (from.contains("onChanged")){
//                Log.d("TAGA","onChanged 2");
//                startService(response);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try{
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAGA","Permission Granted");
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
//                if (checkResponse){
//                    Log.d("TAGA","onRequestPermissionsResult");
//                    startService(response);
//                }
                startWorkMangr();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.d("TAGA",e.getMessage());
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Status");
            // Log.d("TAGA", String.valueOf(message));
            setRewardPercentage(message);
        }
    };

    private void setRewardPercentage(String message) {
        if (message.equals("")){
            message = "0";
        }
        txtRewardPercent.setText(message + "%");
    }

//    private void startService(SendMessagesResponse response) {
//       try {
//
//           Gson gson = new Gson();
//           String json = gson.toJson(num);
//           String respo = gson.toJson(response);
//           Intent serviceIntent = new Intent(this, ForgroundService.class);
//           serviceIntent.putExtra("json_data", json);
//           serviceIntent.putExtra("respo_data", respo);
//           serviceIntent.putExtra("inputExtra", "Background task Perform...!!!");
//           ContextCompat.startForegroundService(this, serviceIntent);
//       }catch (Exception e){
//           Log.d("TAGA",e.getMessage());
//       }
//    }


    private void startWorkMangr(){

        Log.d("TAGA","startWorkMangr");
        PeriodicWorkRequest myWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 20, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("12312ds", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest);
    }
}