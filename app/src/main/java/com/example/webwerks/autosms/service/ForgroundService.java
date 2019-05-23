package com.example.webwerks.autosms.service;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.webwerks.autosms.model.response.SendMessagesIdResponse;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.utils.Prefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import static com.example.webwerks.autosms.application.App.CHANNEL_ID;


public class ForgroundService extends Service {

    private int counter = 0;
    ArrayList<Contacts.User> num = new ArrayList<>();
    ArrayList<SendMessagesResponse.Result> results = new ArrayList<>();
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    int ID;
    ArrayList<String> sentID = new ArrayList<>();
    Gson gson = new Gson();
    SendMessagesIdRequest request = new SendMessagesIdRequest();
    int count;

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
                .setContentTitle("Mobile Rewards")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_app_icon_round)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        peformTask(intent);
        return START_NOT_STICKY;
    }

    private void peformTask(Intent intent) {

        String demoMessage = intent.getStringExtra("json_data");
        String liveMessage = intent.getStringExtra("respo_data");

        try {

            SendMessagesResponse response = gson.fromJson(liveMessage, SendMessagesResponse.class);
            if (response != null) {
                //results.addAll(response.result);

                Observable.fromIterable(response.result)
                        .subscribeOn(Schedulers.io())
                        .concatMapCompletable(new Function<SendMessagesResponse.Result, CompletableSource>() {
                            @Override
                            public CompletableSource apply(final SendMessagesResponse.Result data) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<SendMessagesResponse.Result>() {
                                    @Override
                                    public void subscribe(final ObservableEmitter<SendMessagesResponse.Result> emitter) throws Exception {
                                        try {
                                            String check_id = String.valueOf(data.message_id);
                                            String check = Prefs.getSentIds(getApplication());
                                            if (check.contains(check_id)) {
                                                Log.d("MySimpleService", "Match");
                                            } else {
                                                Log.d("MySimpleService", "NotMatch");
                                                sendMessages(data.mobile_number, data.message_content, Integer.parseInt(data.message_id));
                                            }
                                            Observable.timer(2, TimeUnit.MINUTES).doAfterNext(new Consumer<Long>() {
                                                @Override
                                                public void accept(Long aLong) throws Exception {
                                                    emitter.onComplete();
                                                }
                                            }).subscribe();
                                        }catch (Exception e){
//                                            Log.d("TAGA", e.getMessage());
                                        }
                                    }
                                }).ignoreElements();
                            }
                        }).subscribe();
            }
        } catch (Exception e) {
            //Log.d("TAGA", e.getMessage());
        }
    }

    @SuppressLint("CheckResult")
    private void sendMessages(final String num, String message, int id){

        ID = id;
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts =sms.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

        final int numParts = parts.size();
        count = numParts;
        Log.d("TAGA", String.valueOf(count));

        for (int i = 0; i < numParts; i++) {
            sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
            deliveryIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0));
        }

        sms.sendMultipartTextMessage(num,null, parts, sentIntents, deliveryIntents);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        count = count-1;
                        Log.d("TAGA","count"+count);
                        Log.d("TAGA", "SENT");
                        Log.d("TAGA", String.valueOf(ID));
                        if (count==0){
                        Log.d("TAGA", "CallApi");
                        sentID.add(String.valueOf(ID));
                        String json = gson.toJson(sentID);
                        Prefs.setSentIds(getApplicationContext(), json);
                        updateDeliverIds();
                        }else {
                            Log.d("TAGA", "ApiNotCall");
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---//
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                Log.d("TAGA", "DELIVERED");
            }
        }, new IntentFilter(DELIVERED + ID));
    }


//    private void sendMessages(String number, final String message, int id) {
//
//        Log.d("MySimpleService", number);
//        Log.d("MySimpleService", String.valueOf(id));
//
//        ID = id;
//        Intent deliveredIntent = new Intent(DELIVERED + ID);
//        deliveredIntent.putExtra("messageID", ID);
//
//        Intent sentIntent = new Intent(SENT + ID);
//        sentIntent.putExtra("ID", ID);
//
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//                deliveredIntent, PendingIntent.FLAG_ONE_SHOT);
//
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
//                new Intent(SENT), 0);
//
////        SmsManager sms = SmsManager.getDefault();
////        sms.sendTextMessage(number, null, message, sentPI, deliveredPI);
//
//
//        //---when the SMS has been sent---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Log.d("TAGA", "SENT");
//                        Log.d("TAGA", String.valueOf(ID));
//                        sentID.add(String.valueOf(ID));
//                        String json = gson.toJson(sentID);
//                        Prefs.setDeliverdIds(getApplicationContext(), json);
//                        updateDeliverIds();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));
//
//        //---when the SMS has been delivered---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//
//            }
//        }, new IntentFilter(DELIVERED + ID));
//
//    }

    private void updateDeliverIds() {
        try {
            String data = Prefs.getSentIds(this);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<Integer> user = gson.fromJson(data, type);
            String mobile_number = Prefs.getUserMobile(this);
            if (user != null) {
                try {
                    request.setMessage_id(user);
                    request.setMobile_number(mobile_number);
                    final SendMessagesIdResponse response = RestServices.getInstance().sendMessagesIdResponse(request);
                    if (response != null) {
                        if (response.getResponse_code().equals("200")) {
                            Log.d("MySimpleService", "Response");
                            Prefs.setMonthlyRewards(this, response.monthly_rewards);
                            Intent intent = new Intent("monthly_rewards");
                            intent.putExtra("Status", response.monthly_rewards);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                        } else {
                            Log.d("MySimpleService", "error");
                        }
                    }
                } catch (Exception e) {
                    Log.d("TAGA", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.d("TAGA", e.getMessage());
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
        // Log.d("MySimpleService", "onDestroy");
    }

}