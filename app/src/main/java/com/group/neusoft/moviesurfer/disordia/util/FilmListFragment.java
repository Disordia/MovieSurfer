package com.group.neusoft.moviesurfer.disordia.util;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private void updateUI(){
        FilmLab filmLab=FilmLab.get(getActivity());
        List<FilmInfo> filmInfos=filmLab.getFilmInfos();
        mFilmItemAdapter=new FilmItemAdapter(filmInfos);
        mFilmRecyclerView.setAdapter(mFilmItemAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        public FilmItemHolder(View itemView) {
            super(itemView);
            mNetHelper=new NetHelper();
            mTitleTextView= (TextView) itemView.findViewById(R.id.film_item_title);
            mCardView= (CardView) itemView.findViewById(R.id.film_item_card);
            mCoverImageView= (ImageView) itemView.findViewById(R.id.film_item_cover);
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
            //mCoverImageView.setImageDrawable(getResources().getDrawable(R.drawable.ass));
            mNetHelper.FillImageView(filmInfo.getCoverImgUrl(),mCoverImageView);
            mTitleTextView.setText(filmInfo.getTitle());
            mFilmInfo=filmInfo;
        }

    }

    private class FilmItemAdapter extends RecyclerView.Adapter<FilmItemHolder>{
        private List<FilmInfo> mFilmInfos;

        public FilmItemAdapter(List<FilmInfo> filmInfos){
            mFilmInfos=filmInfos;
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
            return mFilmInfos.size();
        }
    }

}
