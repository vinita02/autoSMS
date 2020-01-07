package com.example.webwerks.autosms.activity;

import android.Manifest;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.android.material.snackbar.Snackbar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.service.MyWorker;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.viewmodel.MessagesViewModel;

import java.security.Permission;
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
    MessagesViewModel viewModel;
    ArrayList<SendMessagesResponse.Result> results = new ArrayList<>();
    SendMessagesResponse response = new SendMessagesResponse();
    private static final int PERMISSION_REQUEST = 100;
    private static final int DEF_SMS_REQ = 102;
    Context context;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    int ID, count;

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
//        checkSMSPermission(response, "initViews");
        Prefs.setLaunchActivity(DashboardActivity.this, "dashboardActivity");
        viewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("This app is not set as your default messaging app So, you want to set it as default Sms App!!")
                        .setCancelable(false)
                        .setTitle("Alert!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                                startActivityForResult(intent, DEF_SMS_REQ);
                            }
                        });
                builder.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DEF_SMS_REQ) {
            if (resultCode == RESULT_OK) {
                startWorkMangr();
            }
        }
    }

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

    private void checkSMSPermission(SendMessagesResponse response, String from) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) + checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) || shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    Snackbar.make(findViewById(R.id.rl), "You need to grant SEND SMS & PHONE STATE permission to send sms",
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
                startWorkMangr();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
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
            }
        } else {
            startWorkMangr();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if ((grantResults.length > 0) &&
                        (grantResults[0] + grantResults[1]
                                == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("TAGA", "Permission Granted");
                    startWorkMangr();
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
//                sendMessages("8805429015", "A second common modern English style is to use no indenting, but add vertical white space to create \"block paragraphs.\" On a typewriter, a double carriage return produces a blank line for this purpose; professional typesetters (or word processing software) may put in an arbitrary vertical space by adjusting leading. This style is very common in electronic formats, such as on the World Wide Web and email.", 10);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            } else {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAGA", "Permission Granted");
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    startWorkMangr();
//                sendMessages("8805429015","A second common modern English style is to use no indenting, but add vertical white space to create \"block paragraphs.\" On a typewriter, a double carriage return produces a blank line for this purpose; professional typesetters (or word processing software) may put in an arbitrary vertical space by adjusting leading. This style is very common in electronic formats, such as on the World Wide Web and email.",10);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.d("TAGA", e.getMessage());
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Status");
            setRewardPercentage(message);
        }
    };

    private void setRewardPercentage(String message) {
        if (message.equals("")) {
            message = "0";
        }
        txtRewardPercent.setText(message + "%");
    }

    private void startWorkMangr() {

        Log.d("TAGA", "startWorkMangr");
        PeriodicWorkRequest myWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 16, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("12312ds", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest);
    }

}