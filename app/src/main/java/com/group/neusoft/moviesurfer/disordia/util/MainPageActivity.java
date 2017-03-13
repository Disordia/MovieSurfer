package com.group.neusoft.moviesurfer.disordia.util;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.group.neusoft.moviesurfer.R;

/**
 * Created by ttc on 2017/3/12.
 */

public class MainPageActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;


    private void initView(){
        FragmentManager fragmentManager=getFragmentManager();
        FilmListFragment filmListFragment= (FilmListFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if(filmListFragment==null){
            //LogUtil.print("add Fragment");
            filmListFragment=new FilmListFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,filmListFragment).commit();
        }
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ((NavigationView)findViewById(R.id.main_navigation)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main_page);
        initView();
    }
}
