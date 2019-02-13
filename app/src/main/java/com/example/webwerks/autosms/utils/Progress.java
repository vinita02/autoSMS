package com.example.webwerks.autosms.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.webwerks.autosms.R;


public class Progress {

    private ProgressBar progressBar;
    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;


    public Progress(Context context, ViewGroup viewGroup) {
        this.context = context;
        this.viewGroup = viewGroup;
        initProgressBar();
    }

    private void initProgressBar() {
        try {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            viewGroup.addView(progressBar, params);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.material_light_blue_500), android.graphics.PorterDuff.Mode.MULTIPLY);
            progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void showProgresBar() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception ignored) {
        }
    }

    public void hideProgressBar() {
        try {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception ignored) {
        }
    }
}
