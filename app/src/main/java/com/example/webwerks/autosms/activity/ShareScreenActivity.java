package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.webwerks.autosms.R;

public class ShareScreenActivity extends BaseActivity {

    public static void open(DashboardActivity activity) {
        activity.startActivity(new Intent(activity,ShareScreenActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_share_screen;
    }

    @Override
    protected void initViews() {

    }
}
