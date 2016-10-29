package com.xy.hashmaker.activities.base;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xy.hashmaker.R;

/**
 * Created by Xavier Yin on 10/28/16.
 */

public abstract class BaseWithToolBarActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    protected void findUI() {
        this.navigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        this.actionBarDrawerToggle = this.setupDrawerToggle();
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
    }

    @Override
    protected void initUI() {
        super.initUI();

        this.toolbar.setTitle(this.getTitle());
        this.toolbar.setTitleTextColor(this.getResources().getColor(R.color.color_general_white_FFFFFF));
        this.setSupportActionBar(toolbar);
    }

    @Override
    protected void registerEvent() {
        this.drawerLayout.addDrawerListener(this.actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    // Methods
    protected ActionBarDrawerToggle setupDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
                this.toolbar, R.string.custom_string_drawer_open, R.string.custom_string_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        return actionBarDrawerToggle;
    }
}
