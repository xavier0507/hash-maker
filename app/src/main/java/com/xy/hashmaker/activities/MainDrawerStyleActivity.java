package com.xy.hashmaker.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.xy.hashmaker.BuildConfig;
import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseWithToolBarActivity;
import com.xy.hashmaker.fragments.HashtagFragment;
import com.xy.hashmaker.utils.FragmentUtil;

/**
 * Created by Xavier Yin on 10/26/16.
 */

public class MainDrawerStyleActivity extends BaseWithToolBarActivity {
    private MenuItem currentMenuItem;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initUI() {
        super.initUI();
        this.currentMenuItem = this.navigationView.getMenu().findItem(R.id.drawer_item_note_group_version);
        this.currentMenuItem.setTitle(this.getString(R.string.custom_string_menu_version) + ": V " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_drawer_style;
    }

    @Override
    protected void arrangeFragment(Bundle savedInstanceState) {
        super.arrangeFragment(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentUtil.replace(this, R.id.layout_container, new HashtagFragment(), null, false);
        }
    }
}
