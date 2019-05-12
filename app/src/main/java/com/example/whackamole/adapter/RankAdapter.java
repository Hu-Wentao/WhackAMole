package com.example.whackamole.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.R;

import java.util.Arrays;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private Record[] recordArr;
    private boolean isAdmin;

    public void setData(Record[] recordArr, boolean isAdmin){
        Arrays.sort(recordArr);
        this.recordArr = recordArr;
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
        if (BuildConfig.DEBUG) Log.d("RankAdapter", "Rank列表已排序并更新");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rank_item, viewGroup, false);
        return new RankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvIndex.setText(String.valueOf(i+1));
        if(isAdmin) {
            viewHolder.tvRecordAccount.setVisibility(View.VISIBLE);
            viewHolder.tvRecordAccount.setText(String.valueOf(recordArr[i].getRecordAccount()));
        }else {
            viewHolder.tvRecordAccount.setVisibility(View.INVISIBLE);
        }
        viewHolder.getTvRecordScore.setText((recordArr[i].getRecordScore()+"分"));
    }

    @Override
    public int getItemCount() {
        return recordArr.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvIndex;
        final TextView tvRecordAccount;
        final TextView getTvRecordScore;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
            tvRecordAccount = itemView.findViewById(R.id.tv_recordAccount);
            getTvRecordScore = itemView.findViewById(R.id.tv_recordScore);
        }
    }
}
