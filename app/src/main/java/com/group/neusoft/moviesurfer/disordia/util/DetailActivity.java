package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.R;
import com.group.neusoft.moviesurfer.coco.LoginHelper;

/**
 * Created by ttc on 2017/3/12.
 */

public class DetailActivity extends AppCompatActivity {
    private FilmInfo mFilmInfo;
    private NetHelper mNetHelper;
    private LoginHelper mLoginHelper;

    public void initView(){
        mNetHelper= NetHelper.getInstance();
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

        ((TextView)findViewById(R.id.detail_text)).setText(CreateSpannableString("Detail:\n",mFilmInfo.getExtra2()));
        ((TextView)findViewById(R.id.detail_titlecontent)).setText(CreateSpannableString("Title:\n",mFilmInfo.getTitle()));
        ((TextView)findViewById(R.id.detail_age)).setText(CreateSpannableString("Publish Age:\n",mFilmInfo.getDate()));
        ((TextView)findViewById(R.id.detail_score)).setText(CreateSpannableString("ScoreInfo:\n",mFilmInfo.getScoreInfo()));
    }



    private SpannableString CreateSpannableString(String title,String content) {
        SpannableString contentStr=new SpannableString(title+content);
        ForegroundColorSpan colorSpan=new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        contentStr.setSpan(colorSpan,0,title.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        contentStr.setSpan(sizeSpan,0,title.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return contentStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);
        mLoginHelper=LoginHelper.getLoginHelper(DetailActivity.this);
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
            case  R.id.detail_share:{
                mLoginHelper.ShareinTencent(mFilmInfo.getTitle()+"\nPlease use Thunder to download the movie",mFilmInfo.getUrl(),mFilmInfo.getCoverImgUrl());
                break;
            }
        }
        return false;
    }


}
