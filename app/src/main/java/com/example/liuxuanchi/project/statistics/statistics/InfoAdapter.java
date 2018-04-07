package com.example.liuxuanchi.project.statistics.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.ReachedInfoLitepal;


import java.util.List;

/**
 * Created by xiaozhu on 2018/3/10.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<ReachedInfoLitepal> infoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView info_name;
        TextView info_reach_time;

        public ViewHolder(View view){
            super(view);
            info_name = (TextView)view.findViewById(R.id.info_name);
            info_reach_time = (TextView)view.findViewById(R.id.info_reachTime);
        }
    }

    public InfoAdapter(List<ReachedInfoLitepal> list){
        infoList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ReachedInfoLitepal info = infoList.get(position);
        holder.info_name.setText(info.getName());
        holder.info_reach_time.setText(info.getReachedTime());
    }

    @Override
    public int getItemCount(){
        return infoList.size();
    }

}
