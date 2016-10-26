package com.xy.hashmaker.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseActivity;
import com.xy.hashmaker.fragments.HashtagFragment;
import com.xy.hashmaker.fragments.KeywordFragment;

/**
 * Created by Xavier Yin on 10/26/16.
 */

public class MainDrawerStyleActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_drawer_style;
    }

    @Override
    protected void findUI() {
        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.actionBarDrawerToggle = this.setupDrawerToggle();
    }

    @Override
    protected void initUI() {
        super.initUI();

        this.toolbar.setTitle(this.getTitle());
        this.toolbar.setTitleTextColor(this.getResources().getColor(R.color.color_general_white_FFFFFF));
        this.setSupportActionBar(toolbar);

        this.drawerLayout.setDrawerListener(actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();
    }

    @Override
    protected void registerEvent() {
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_item_service_group_hashtag:
                this.replaceFragment(R.id.drawer_item_service_group_hashtag);
                this.checkItem(item);
                this.closeDrawer();
                break;

            case R.id.drawer_item_service_group_keyword:
                this.replaceFragment(R.id.drawer_item_service_group_keyword);
                this.checkItem(item);
                this.closeDrawer();
                break;

            case R.id.drawer_item_note_group_version:
                // Do nothing
                break;

            default:
                Toast.makeText(this, item.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                this.closeDrawer();
                break;
        }

        return true;
    }

    // Methods
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.custom_string_drawer_open, R.string.custom_string_drawer_close);
    }

    private void replaceFragment(int menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (menuItem) {
            case R.id.drawer_item_service_group_hashtag:
                fragmentClass = HashtagFragment.class;
                break;

            case R.id.drawer_item_service_group_keyword:
                fragmentClass = KeywordFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        this.drawerLayout.closeDrawers();
    }

    private void checkItem(MenuItem item) {
        item.setChecked(true);
    }

    private void closeDrawer() {
        this.drawerLayout.closeDrawers();
    }
}
