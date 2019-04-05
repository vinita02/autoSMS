package com.example.webwerks.autosms.service;


import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.activity.DashboardActivity;
import com.example.webwerks.autosms.model.Contacts;
import com.example.webwerks.autosms.model.request.SendMessagesIdRequest;
import com.example.webwerks.autosms.model.response.BaseResponse;
import com.example.webwerks.autosms.model.response.SendMessagesIdResponse;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.utils.Prefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static com.example.webwerks.autosms.application.App.CHANNEL_ID;


public class ForgroundService extends Service {
    private int counter = 0;
    ArrayList<Contacts.User> num = new ArrayList<>();
    ArrayList<SendMessagesResponse.Result> results = new ArrayList<>();
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    int ID;
    ArrayList<String> deliverIDs = new ArrayList<>();
    Gson gson = new Gson();
    SendMessagesIdRequest request = new SendMessagesIdRequest();

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        Task(intent);
        return START_NOT_STICKY;
    }

    private void Task(Intent intent) {

        String demoMessage = intent.getStringExtra("json_data");
        String liveMessage = intent.getStringExtra("respo_data");

        //-------------------------   Demo Messages ------------------ //

        /*final Contacts resp = gson.fromJson(demoMessage, Contacts.class);

        if (resp != null) {
            num.addAll(resp);
            for (int i = 0; i < resp.size(); i++) {

                final String message = resp.get(i).getMessages();
                final String phone = resp.get(i).getMobile();
                final int id = resp.get(i).getId();

//                Log.d("MySimpleService", "message " + message);
//                Log.d("MySimpleService", "phone " + phone);
//                Log.d("MySimpleService", "id" + id);

                String check_id = String.valueOf(id);
                String check = Prefs.getDeliverdIds(getApplication());
                if (check.contains(check_id)) {
                  //  Log.d("MySimpleService", "Match");
                } else {
                   // Log.d("MySimpleService", "NotMatch");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                             Log.d("size", String.valueOf(num.size()));
                             senMessage(phone, message, id);
                        }
                    }, 20000);
                }
            }
        }*/

        // ------------------------- Live Messages ------------------ //

        SendMessagesResponse response = gson.fromJson(liveMessage, SendMessagesResponse.class);

        Log.d("SendMessagesResponse ", "Task: " + response.getMessage());
        Log.d("TAG", "Task: " + response.getResponse_code());

        if (response != null) {
            results.addAll(response.result);
            for (int i = 0; i < response.result.size(); i++) {

                final String message = response.result.get(i).message_content;
                final String phone = response.result.get(i).mobile_number;
                final int id = Integer.parseInt(response.result.get(i).message_id);

                Log.d("MySimpleService", "message " + message);
                Log.d("MySimpleService", "phone " + phone);
                Log.d("MySimpleService", "id" + id);

                String check_id = String.valueOf(id);
                String check = Prefs.getDeliverdIds(getApplication());
                if (check.contains(check_id)) {
                    Log.d("MySimpleService", "Match");
                } else {
                    Log.d("MySimpleService", "NotMatch");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("button_cancel", "run: ");
                            senMessage(phone, message, id);
                        }
                    }, 20000);
                }
            }
        }
    }

    private void senMessage(String number, final String message, int id) {

//        Log.d("MySimpleService", number);
//        Log.d("MySimpleService", message);
//        Log.d("MySimpleService", String.valueOf(id));

        ID = id;
        Intent deliveredIntent = new Intent(DELIVERED + ID);
        deliveredIntent.putExtra("messageID", ID);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                deliveredIntent, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, sentPI, deliveredPI);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
                        Log.d("MySimpleService", "SMS SENT");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        //Toast.makeText(getBaseContext(), "Generic failure",
                        //  Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        /*Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();*/
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        /*Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();*/
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                int messageID = arg1.getIntExtra("messageID", -1);
                if (messageID != -1) {
                    // This is the message ID (row ID) of the message that was delivered
                    Log.d("MySimpleService", String.valueOf(messageID));
                    Log.d("MySimpleService", "DELIVERED");
                    deliverIDs.add(String.valueOf(messageID));
                    String json = gson.toJson(deliverIDs);
                    Prefs.setDeliverdIds(getApplicationContext(), json);
                    Log.d("MySimpleService", "PREF" + Prefs.getDeliverdIds(getApplicationContext()));
                    sendApi();
                }
            }
        }, new IntentFilter(DELIVERED + ID));

    }

    private void sendApi() {
        String data = Prefs.getDeliverdIds(this);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<Integer> user = gson.fromJson(data, type);
        String mobile_number = Prefs.getUserMobile(this);
        if (user != null) {
//            Log.d("MySimpleService", "user_size " + user.size());
//            Log.d("MySimpleService", "num_size " + num.size());
//            Log.d("MySimpleService", "mobile_number " + mobile_number);

            // ------------------------- Dummy Messages ------------------ //

            /*if (user.size() == num.size()) {

                Log.d("MySimpleService", "Api_call");
                request.setMessage_id(user);
                request.setMobile_number(mobile_number);
                final SendMessagesIdResponse response = RestServices.getInstance().sendMessagesIdResponse(request);
                 if (response!=null){
                     if (response.getResponse_code().equals("200")) {
                         Log.d("MySimpleService", "response " + response.getMessage());
                         Log.d("MySimpleService", "response " + response.getResponse_code());
                         Log.d("MySimpleService", "response " + response.monthly_rewards);
                         Intent intent = new Intent("monthly_rewards");
                         intent.putExtra("Status", response.monthly_rewards);
                         LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                     }else {
                         Log.d("MySimpleService", "error");
                     }

                 }
            } else {
                Log.d("MySimpleService", "Api_notcall");
            }*/

            // ------------------------- Live Messages ------------------ //

            if (user.size() == results.size()) {

                Log.d("MySimpleService", "Api_call");
                request.setMessage_id(user);
                request.setMobile_number(mobile_number);
                final SendMessagesIdResponse response = RestServices.getInstance().sendMessagesIdResponse(request);
                if (response!=null){
                    if (response.getResponse_code().equals("200")) {
                        Log.d("MySimpleService", "response " + response.getMessage());
                        Log.d("MySimpleService", "response " + response.getResponse_code());
                        Log.d("MySimpleService", "response " + response.monthly_rewards);
                        Prefs.setMonthlyRewards(this,response.monthly_rewards);
                        Intent intent = new Intent("monthly_rewards");
                        intent.putExtra("Status", response.monthly_rewards);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }else {
                        Log.d("MySimpleService", "error");
                    }
            } else {
                Log.d("MySimpleService", "Api_notcall");
            }
         }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MySimpleService", "onDestroy");
    }

}