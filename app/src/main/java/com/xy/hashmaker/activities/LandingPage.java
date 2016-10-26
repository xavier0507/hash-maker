package com.xy.hashmaker.activities;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseActivity;

/**
 * Created by Xavier Yin on 10/20/16.
 */

public class LandingPage extends BaseActivity {

    @Override
    protected void preProcess() {
        super.preProcess();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_landing_page;
    }

    @Override
    protected void findUI() {
        //Do nothing
    }

    @Override
    protected void initUI() {
        super.initUI();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void registerEvent() {
        // Do nothing
    }

    @Override
    protected void postProcess() {
        super.postProcess();
        this.executePostDelayed();
    }

    private void executePostDelayed() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LandingPage.this, MainDrawerStyleActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
