package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.R;

/**
 * Created by ttc on 2017/3/12.
 */

public class DetailActivity extends AppCompatActivity {
    FilmInfo mFilmInfo;
    NetHelper mNetHelper;
    public void initView(){
        mNetHelper=new NetHelper();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.dld_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this,"btn pushed",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFilmInfo.getDownloadUrls()));
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
            }
        });
        mFilmInfo=FilmListFragment.getFilmInfo(getIntent());
        ((CollapsingToolbarLayout)findViewById(R.id.detail_title)).setTitle(mFilmInfo.getTitle());
        mNetHelper.FillImageView(mFilmInfo.getCoverImgUrl(),(ImageView)findViewById(R.id.detail_cover));
        String detailString=mFilmInfo.getTitle()+"\n"+mFilmInfo.getDate()+"\n"+mFilmInfo.getScoreInfo()+"\n";
        ((TextView)findViewById(R.id.detail_text)).setText(detailString);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detail_redirect:{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFilmInfo.getUrl()));
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
                break;
            }
        }
        return false;
    }


}
