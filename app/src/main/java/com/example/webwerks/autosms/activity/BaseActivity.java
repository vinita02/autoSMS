package com.example.webwerks.autosms.activity;


import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public String DEVICE_ID;
    public int startDay, startMonth, startYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        setFullscreen();
        getDeviceId();
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }


    protected abstract int getLayoutResourceId();

    public String getDeviceId() {

        DEVICE_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return DEVICE_ID;
    }

    public void setFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected abstract void initViews();

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
