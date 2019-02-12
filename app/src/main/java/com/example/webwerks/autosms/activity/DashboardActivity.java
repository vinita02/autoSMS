package com.example.webwerks.autosms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DashboardActivity extends BaseActivity {

    @BindView(R.id.btnShareNow)
    Button btnShareNow;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.imgUser)
    ImageView imgUser;


    public static void open(Context context) {
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void initViews() {
        Prefs.setLaunchActivity(DashboardActivity.this, "dashboardActivity");
    }


    @OnClick({R.id.btnShareNow, R.id.imgBack, R.id.imgUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareNow:
                ShareScreenActivity.open(this);
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgUser:
                MyProfileActivity.open(this);
                break;
        }
    }
}
