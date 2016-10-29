package com.xy.hashmaker.activities.base;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Xavier Yin on 10/28/16.
 */

public abstract class BaseFbLoginActivity extends BaseActivity {
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    private AccessTokenTracker accessTokenTracker;
    private AccessToken fbAccessToken;

    @Override
    protected void preProcess() {
        super.preProcess();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        this.callbackManager = CallbackManager.Factory.create();
        this.loginManager = LoginManager.getInstance();
        this.loginManager.registerCallback(this.callbackManager, this.facebookCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void determineLoginStatus() {
        logger.d("login status: " + isLogin());

        if (!this.isLogin()) {
            this.invokeFbLoginRequest();
        } else {
            this.fbLogout();
            this.showLoginTextOnMenu();
        }
    }

    protected void invokeFbLoginRequest() {
        this.loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_photos"));
    }

    protected void fbLogout() {
        this.loginManager.logOut();
        this.setLogin(false);
    }

    protected void showUserFbProfile(AccessToken fbAccessToken) {
        if (fbAccessToken != null) {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");

            GraphRequest request = GraphRequest.newMeRequest(
                    fbAccessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            logger.d("LoginActivity Response " + response.toString());

                            try {
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String pic = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                logger.d("name = " + name);
                                logger.d("email = " + email);
                                logger.d("picture = " + pic);

                                showUserDetailOnMenu(name, email, pic);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    protected void trackFbToken() {
        this.accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessTokenTracker.stopTracking();

                if (oldAccessToken != null && currentAccessToken != null) {
                    logger.d("oldAccessToken: " + oldAccessToken.getToken());
                    logger.d("currentAccessToken: " + currentAccessToken.getToken());

                    if (!oldAccessToken.getToken().equals(currentAccessToken.getToken())) {
                        fbLogout();
                    }

                    showUserFbProfile(currentAccessToken);
                } else if (currentAccessToken == null && isLogin()) {
                    fbLogout();
                }

                showLoginTextOnMenu();
            }
        };
        AccessToken.refreshCurrentAccessTokenAsync();
    }

    protected FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            fbAccessToken = loginResult.getAccessToken();
            showUserFbProfile(fbAccessToken);
            setLogin(true);
            showLoginTextOnMenu();
        }

        @Override
        public void onCancel() {
            setLogin(false);
            logger.d("FB" + "CANCEL");
        }

        @Override
        public void onError(FacebookException error) {
            setLogin(false);
            logger.d("FB" + "CANCEL");
        }
    };

    protected abstract void showLoginTextOnMenu();

    protected abstract void showUserDetailOnMenu(String name, String email, String imageUrl);
}
