package com.group.neusoft.moviesurfer.disordia.util;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.R;

import java.util.List;

/**
 * Created by ttc on 2017/3/12.
 */

public class FilmListFragment extends Fragment {

    private FilmItemAdapter mFilmItemAdapter;
    RecyclerView mFilmRecyclerView;
    public static final String INTENT_TAG="com.group.neusoft.moviesurfer.filmlistintent";

    public static FilmInfo getFilmInfo(Intent i){
        FilmInfo info=new FilmInfo();
        String infoStr=i.getStringExtra(INTENT_TAG);
        //LogUtil.print(infoStr);
        if(infoStr!=null){
            info= JSON.parseObject(infoStr,FilmInfo.class);
        }
        return info;
    }

    public void updateUI(){
        mFilmItemAdapter=new FilmItemAdapter();
        FilmLab.getInstance(getActivity()).GetFilmInfoAsync(mFilmRecyclerView,mFilmItemAdapter);
        //mFilmRecyclerView.setAdapter(mFilmItemAdapter);
    }


    @Override
    public void onAttach(Context context) {
        LogUtil.print("on Attach");
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.print("on create View");
        View filmListView=inflater.inflate(R.layout.fragment_flim_list,container,false);
        mFilmRecyclerView= (RecyclerView) filmListView.findViewById(R.id.film_item_list);
        mFilmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        updateUI();
        return filmListView;
    }


    private class FilmItemHolder extends RecyclerView.ViewHolder{
        TextView mTitleTextView;
        ImageView mCoverImageView;
        CardView mCardView;
        FilmInfo mFilmInfo;
        NetHelper mNetHelper;
        TextView mScoreTextView;
        public FilmItemHolder(View itemView) {
            super(itemView);
            mNetHelper=NetHelper.getInstance();
            mTitleTextView= (TextView) itemView.findViewById(R.id.film_item_title);
            mCardView= (CardView) itemView.findViewById(R.id.film_item_card);
            mCoverImageView= (ImageView) itemView.findViewById(R.id.film_item_cover);
            mScoreTextView= (TextView) itemView.findViewById(R.id.film_item_score);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),DetailActivity.class);
                    LogUtil.print(JSON.toJSONString(mFilmInfo));
                    intent.putExtra(INTENT_TAG,JSON.toJSONString(mFilmInfo));
                    startActivity(intent);
                }
            });
        }


        public void OnBind(FilmInfo filmInfo){
            //mCoverImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_download));
            if(filmInfo.getCoverImgUrl()!=null)
                mNetHelper.FillImageView(filmInfo.getCoverImgUrl(),mCoverImageView);
            if(filmInfo.getTitle()!=null)
                mTitleTextView.setText(filmInfo.getTitle());
            if(filmInfo.getScoreInfo()!=null)
                mScoreTextView.setText(filmInfo.getScoreInfo());
            mFilmInfo=filmInfo;
        }

    }

    public class FilmItemAdapter extends RecyclerView.Adapter<FilmItemHolder>{
        private List<FilmInfo> mFilmInfos;
        public FilmItemAdapter(){

        }

        public void setFilmInfos(List<FilmInfo> filmInfos) {
            mFilmInfos = filmInfos;
        }

        @Override
        public FilmItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.fragment_film_item,parent,false);
            return new FilmItemHolder(view);
        }

        @Override
        public void onBindViewHolder(FilmItemHolder holder, int position) {
            FilmInfo filmInfo=mFilmInfos.get(position);
            holder.OnBind(filmInfo);
        }

        @Override
        public int getItemCount() {
            //LogUtil.print("mFilmInfos.size()"+mFilmInfos.size());
            if(mFilmInfos!=null) {
                return mFilmInfos.size();
            }
            return 0;
        }
    }

}
