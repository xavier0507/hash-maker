package com.xy.hashmaker.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Xavier Yin on 10/26/16.
 */

public class FbTestingActivity extends BaseActivity {
    private LoginButton btn;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode, resultCode, data);

        logger.d("fb token: " + AccessToken.getCurrentAccessToken().getToken());

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        logger.d("content: " + content.getContentUrl());

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content);
    }

    @Override
    protected void preProcess() {
        super.preProcess();

        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        logger.d("fb register: " + FacebookSdk.getSdkVersion());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fb_testing;
    }

    @Override
    protected void findUI() {
        this.btn = (LoginButton) this.findViewById(R.id.button_fb);
    }

    @Override
    protected void registerEvent() {
        this.btn.setReadPermissions("email");
        this.btn.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void postProcess() {
        super.postProcess();

        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo("com.xy.hashmaker", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                logger.d("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
