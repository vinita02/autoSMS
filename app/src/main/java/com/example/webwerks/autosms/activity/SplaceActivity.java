package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.os.Handler;
import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.utils.Prefs;

public class SplaceActivity extends BaseActivity {
    private String launch_activity;
    private Intent mainIntent;

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
                launch_activity = Prefs.getLaunchActivity(SplaceActivity.this);

                if (launch_activity.equalsIgnoreCase("dashboardActivity")) {
                    mainIntent = new Intent(SplaceActivity.this, DashboardActivity.class);
                } else {
                    mainIntent = new Intent(SplaceActivity.this, LoginActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
