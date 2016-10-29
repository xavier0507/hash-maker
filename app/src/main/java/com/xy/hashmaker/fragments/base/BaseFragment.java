package com.xy.hashmaker.fragments.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xy.hashmaker.utils.Logger;

/**
 * Created by Xavier Yin on 10/27/16.
 */

public abstract class BaseFragment extends Fragment {
    protected Logger logger = Logger.getInstance(this.getClass());

    protected View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.receiveIntent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(this.getLayoutId(), container, false);
        this.preProcess();
        this.findUI();
        this.initUI();
        this.registerEvent();
        this.postProcess();
        return this.view;
    }

    protected void receiveIntent() {
    }

    protected void preProcess() {
    }

    protected void initUI() {
    }

    protected void postProcess() {
    }

    abstract protected int getLayoutId();

    abstract protected void findUI();

    abstract protected void registerEvent();
}
