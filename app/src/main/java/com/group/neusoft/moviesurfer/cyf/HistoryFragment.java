package com.group.neusoft.moviesurfer.cyf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.R;
import com.group.neusoft.moviesurfer.disordia.util.DetailActivity;
import com.group.neusoft.moviesurfer.disordia.util.LogUtil;
import com.group.neusoft.moviesurfer.ofj.databaseHelper.FilmCollection;
import com.group.neusoft.moviesurfer.ofj.databaseHelper.FilmHistory;

import java.util.List;

/**
 * Created by ttc on 2017/3/14.
 */

public class HistoryFragment extends Fragment {
    RecyclerView mRecyclerView;
    FilmHistory mHistoryCollection;
    public static final String INTENT_TAG="com.group.neusoft.moviesurfer.filmlistintent";

    private void UpdateView(){
        mHistoryCollection=FilmHistory.get(getActivity());
        HistoryAdapter adapter=new HistoryAdapter(mHistoryCollection.getCollections());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateView();
    }

    public static FilmInfo getFilmInfo(Intent i){
        FilmInfo info=new FilmInfo();
        String infoStr=i.getStringExtra(INTENT_TAG);
        //LogUtil.print(infoStr);
        if(infoStr!=null){
            info= JSON.parseObject(infoStr,FilmInfo.class);
        }
        return info;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View historyView=inflater.inflate(R.layout.fragment_history,container,false);
        mRecyclerView= (RecyclerView) historyView.findViewById(R.id.history_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        UpdateView();
        return historyView;
    }


    private class HistoryHolder extends RecyclerView.ViewHolder{
        TextView mTitleView;
        TextView mDetailView;
        FilmInfo mFilmInfo;
        public HistoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),DetailActivity.class);
                    LogUtil.print(JSON.toJSONString(mFilmInfo));
                    intent.putExtra(INTENT_TAG,JSON.toJSONString(mFilmInfo));
                    startActivity(intent);
                }
            });
            mTitleView= (TextView) itemView.findViewById(R.id.history_title);
            mDetailView= (TextView) itemView.findViewById(R.id.history_detail);
        }

        public void OnBind(FilmInfo info){
            mFilmInfo=info;
            mTitleView.setText(info.getTitle()+"\n"+info.getDate()+"\n"+info.getScoreInfo());
            mDetailView.setText(info.getExtra2());
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder>{
        List<FilmInfo> mHistories;

        public HistoryAdapter(List<FilmInfo> infos){
            mHistories=infos;
        }

        @Override
        public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View historyItemView=inflater.inflate(R.layout.fragment_history_item,parent,false);
            return new HistoryHolder(historyItemView);
        }

        @Override
        public void onBindViewHolder(HistoryHolder holder, int position) {
            holder.OnBind(mHistories.get(position));
        }

        @Override
        public int getItemCount() {
            return mHistories.size();
        }
    }

}
