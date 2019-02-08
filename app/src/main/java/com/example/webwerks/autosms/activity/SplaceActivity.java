package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.webwerks.autosms.R;

public class SplaceActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splace;
    }

    @Override
    protected void initViews() {

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplaceActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
