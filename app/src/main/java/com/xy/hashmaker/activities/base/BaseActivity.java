package com.xy.hashmaker.activities.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xy.hashmaker.utils.Logger;
import com.xy.hashmaker.utils.PrefsUtil;

/**
 * Created by Xavier Yin on 10/21/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Logger logger = Logger.getInstance(this.getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preProcess();
        this.setContentView(this.getLayoutId());
        this.receiveIntent();
        this.arrangeFragment(savedInstanceState);
        this.findUI();
        this.initUI();
        this.registerEvent();
        this.postProcess();
    }

    protected void preProcess() {
    }

    protected void receiveIntent() {
    }

    protected void arrangeFragment(Bundle savedInstanceState) {}

    protected void initUI() {
    }

    protected void postProcess() {
    }

    protected boolean isLogin() {
        return PrefsUtil.getLogin(this);
    }

    protected void setLogin(boolean isLogin) {
        PrefsUtil.setLogin(this, isLogin);
    }

    abstract protected int getLayoutId();

    abstract protected void findUI();

    abstract protected void registerEvent();
}
