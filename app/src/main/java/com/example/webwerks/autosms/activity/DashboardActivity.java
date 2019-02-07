package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.webwerks.autosms.R;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {

    Button btnShareNow;
    ImageView imgBack;

    public static void open(LoginActivity activity) {
        activity.startActivity(new Intent(activity,DashboardActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initViews() {
        btnShareNow = findViewById(R.id.btnShareNow);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnShareNow.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnShareNow:
                ShareScreenActivity.open(this);
                break;


            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }
}
