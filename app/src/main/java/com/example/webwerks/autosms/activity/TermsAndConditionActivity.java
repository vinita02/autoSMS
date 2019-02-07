package com.example.webwerks.autosms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.webwerks.autosms.R;

public class TermsAndConditionActivity extends BaseActivity implements View.OnClickListener {

    ImageView imgBack;

    public static void open(RegisterActivity activity) {
        activity.startActivity(new Intent(activity,TermsAndConditionActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_terms_and_condition;
    }

    @Override
    protected void initViews() {
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }
}
