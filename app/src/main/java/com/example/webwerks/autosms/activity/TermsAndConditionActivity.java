package com.example.webwerks.autosms.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.response.TermsResponse;
import com.example.webwerks.autosms.utils.CheckNetwork;
import com.example.webwerks.autosms.viewmodel.TermsandConditionViewModel;
import butterknife.BindView;
import butterknife.OnClick;

public class TermsAndConditionActivity extends BaseActivity {

    private static String TAG = "TermsAndConditionActivity";
    @BindView(R.id.imgBack)ImageView imgBack;
    @BindView(R.id.txtContent)TextView txtContent;
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
        viewModel = ViewModelProviders.of(this).get(TermsandConditionViewModel.class);
        getContent();
        if (!CheckNetwork.isConnected(this)){
            showToast("Enable Network State");
        }else {
            viewModel.getTermsandCondtion();
        }
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

    @OnClick({R.id.imgBack})
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }
}
