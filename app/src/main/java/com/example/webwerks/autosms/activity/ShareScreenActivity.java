package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.webwerks.autosms.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareScreenActivity extends BaseActivity {

    @BindView(R.id.imgClose)
    ImageView imgClose;

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

    @OnClick({R.id.imgClose})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imgClose:
                onBackPressed();
                break;
        }
    }
}
