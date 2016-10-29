package com.xy.hashmaker.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Xavier Yin on 10/27/16.
 */

public class FragmentUtil {

    public static boolean containFragment(FragmentActivity fragmentActivity, int viewId) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        return fragmentManager.findFragmentById(viewId) != null;
    }

    public static void replace(FragmentActivity fragmentActivity, int viewId, Fragment fragment, Intent intent, boolean addToBackStack) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (intent != null) {
            fragment.setArguments(intent.getExtras());
        }

        fragmentTransaction.replace(viewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }
}
