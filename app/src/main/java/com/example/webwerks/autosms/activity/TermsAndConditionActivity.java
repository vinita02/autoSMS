package com.example.webwerks.autosms.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.response.TermsResponse;
import com.example.webwerks.autosms.viewmodel.TermsandConditionViewModel;

public class TermsAndConditionActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "TermsAndConditionActivity";
    ImageView imgBack;
    TextView txtContent;
    TermsandConditionViewModel viewModel;


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
        txtContent = findViewById(R.id.txtContent);


        viewModel = ViewModelProviders.of(this).get(TermsandConditionViewModel.class);
        getContent();
        viewModel.getTermsandCondtion();
    }

    private void getContent() {
        viewModel.getContent().observe(this, new Observer<TermsResponse>() {
            @Override
            public void onChanged(@Nullable TermsResponse response) {
                if (response != null){
                    if (response.getResponse_code().equals("200")){
                        Log.d(TAG,response.result.content);
                        txtContent.setText(response.result.content);
                    }
                }
            }
        });
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
