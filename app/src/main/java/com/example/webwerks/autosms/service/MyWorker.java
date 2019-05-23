package com.example.webwerks.autosms.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.request.SendMessagesRequest;
import com.example.webwerks.autosms.model.response.SendMessagesResponse;
import com.example.webwerks.autosms.utils.Prefs;
import com.google.gson.Gson;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    SendMessagesRequest request = new SendMessagesRequest();

    public static final String TASK_DESC = "task_desc";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
//        Intent serviceIntent = new Intent(getApplicationContext(), ForgroundService.class);
//        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
        getUserMessages();
        Log.d("TAGA", "doWork");
        return Result.success();
    }

    private void getUserMessages() {

        String mobile = Prefs.getUserMobile(getApplicationContext());

        // Log.d("TAGA", mobile);
        request.setMobile_number(mobile);
        SendMessagesResponse response = RestServices.getInstance().sendMessagesResponse(request);
        try {
            if (response != null) {
                if (response.getResponse_code().equals("200")) {

                    if (response.result.size() != 0) {

                        //Log.d("TAGA", "response");
                        startService(response);
                        //Log.d("TAGA", "service call");
                    } else {
                        //Log.d("TAGA", "not respnse");
                        //Log.d("TAGA", "service not call");
                    }
                }
            } else {
                //Log.d("TAGA","null data");
            }
        } catch (Exception e) {
            Log.d("TAGA", e.getMessage());
        }
    }

    private void startService(SendMessagesResponse response) {
        try {
            Gson gson = new Gson();
//          String json = gson.toJson(num);
            String respo = gson.toJson(response);
            Intent serviceIntent = new Intent(getApplicationContext(), ForgroundService.class);
            serviceIntent.putExtra("respo_data", respo);
            serviceIntent.putExtra("inputExtra", "Background Task...");
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
        } catch (Exception e) {
            Log.d("TAGA", e.getMessage());
        }
    }
}
