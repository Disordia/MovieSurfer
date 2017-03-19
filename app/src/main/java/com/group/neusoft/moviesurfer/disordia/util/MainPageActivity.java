package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.group.neusoft.moviesurfer.R;
import com.group.neusoft.moviesurfer.coco.LoginHelper;
import com.group.neusoft.moviesurfer.coco.SettingsActivity;
import com.group.neusoft.moviesurfer.cyf.NewFilmIntentService;

/**
 * Created by ttc on 2017/3/12.
 */

public class MainPageActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mActionBarToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchView mSearchView;
    private LoginHelper mLoginHelper;
    private NavigationView mNavigationView;
    public LocalBroadcastManager mLocalBroadcastManager;
    private RefreshBroadCastRecivever mRefreshBroadCastRecivever;
    private IntentFilter mIntentFilter;
    private FilmFragmentPagerAdapter mFilmFragmentPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static final String BroadCastAction="com.neusoft.group.disordia.refresh_main_view";
    public static final String TabLayoutClickIndex="com.neusoft.group.disordia.click_index";

    private void initView(){
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mNavigationView= (NavigationView) findViewById(R.id.main_nav);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_navigation_home: {
                        mViewPager.setCurrentItem(0,true);
                        break;
                    }
                    case R.id.main_navigation_collect:{
                        mViewPager.setCurrentItem(1,true);
                        break;
                    }
                    case R.id.main_navigation_history:{
                        mViewPager.setCurrentItem(2,true);
                        break;
                    }
                    case R.id.main_navigation_settings:{
                        Intent i=new Intent(MainPageActivity.this, SettingsActivity.class);
                        startActivity(i);
                        break;
                    }
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });


        mActionBarToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mActionBarToolbar);
        //bind drawer with toobar:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mActionBarToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //init login helper:
        registerLocalBroadcastReceiver();
        mLoginHelper=LoginHelper.getLoginHelper(MainPageActivity.this);
        UpdateUserInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginHelper.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main_page);
        mFilmFragmentPagerAdapter = new FilmFragmentPagerAdapter(getSupportFragmentManager(), this);
        mTabLayout= (TabLayout) findViewById(R.id.main_tablayout);
        mViewPager= (ViewPager) findViewById(R.id.main_viewpager);
        mViewPager.setAdapter(mFilmFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        NewFilmIntentService.setServiceAlarm(getApplicationContext(), true);
        initView();
        if(savedInstanceState!=null) {
            int tabPosition = savedInstanceState.getInt(TabLayoutClickIndex);
            mViewPager.setCurrentItem(tabPosition, false);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        UpdateUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_page_toolbar_menu, menu);
        final MenuItem item = menu.findItem(R.id.app_bar_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchItems(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    searchItems(null);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_toobar_refresh:{
                searchItems("");
                break;
            }
        }
        return false;
    }

    private void searchItems(String s) {
        mViewPager.setCurrentItem(0,true);
        ((FilmListFragment) mFilmFragmentPagerAdapter.getItem(0)).SearchItem(s);
        LogUtil.print("searching items:"+s);
    }


    public void LoginClick(View view) {
        if(!mLoginHelper.IsUserLogin()) {
            mLoginHelper.QQlogin();
        }else {
            mLoginHelper.LogOut();
            if(mNavigationView.getHeaderView(0).findViewById(R.id.user_name)!=null)
                ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name)).setText("login");
            if(mNavigationView.getHeaderView(0).findViewById(R.id.user_header)!=null)
                ((ImageView)mNavigationView.getHeaderView(0).findViewById(R.id.user_header)).setImageDrawable(getResources().getDrawable(R.drawable.head));
        }

    }

    public void UpdateUserInfo(){
        if(mLoginHelper.IsUserLogin()) {
            if(mNavigationView.getHeaderView(0).findViewById(R.id.user_name)!=null)
            ((TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(mLoginHelper.getUserInformation().getName());
            if(mNavigationView.getHeaderView(0).findViewById(R.id.user_header)!=null)
            ((ImageView)mNavigationView.getHeaderView(0).findViewById(R.id.user_header)).setImageBitmap(mLoginHelper.getUserInformation().getPicture());
        }
    }

    private void registerLocalBroadcastReceiver(){
        mIntentFilter=new IntentFilter(BroadCastAction);
        mRefreshBroadCastRecivever=new RefreshBroadCastRecivever(new RefreshBroadCastRecivever.RefreshCallBack() {
            @Override
            public void OnRefresh() {
                LogUtil.print("broadcast Recieved");
                UpdateUserInfo();
                unRegisterLocalBroadcastReceiver();
            }
        });
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(MainPageActivity.this);
        mLocalBroadcastManager.registerReceiver(mRefreshBroadCastRecivever, mIntentFilter);
    }

    private void unRegisterLocalBroadcastReceiver(){
        if (mLocalBroadcastManager!=null) {
            if (mRefreshBroadCastRecivever!=null) {
                mLocalBroadcastManager.unregisterReceiver(mRefreshBroadCastRecivever);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(TabLayoutClickIndex,mTabLayout.getSelectedTabPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int tabPosition=savedInstanceState.getInt(TabLayoutClickIndex);
        mViewPager.setCurrentItem(tabPosition,false);
    }
}
