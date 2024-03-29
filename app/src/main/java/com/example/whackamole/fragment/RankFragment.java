package com.example.whackamole.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.whackamole.R;
import com.example.whackamole.adapter.RankAdapter;
import com.example.whackamole.adapter.Record;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppData;
import com.example.whackamole.data.ScoreUtil;

import butterknife.BindView;


/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class RankFragment extends BaseFragment {
    //    @BindView(R.id.linear_rank) LinearLayout layoutRank;
//    @BindView(R.id.linear_left) LinearLayout layoutLeft;
//    @BindView(R.id.linear_right)LinearLayout layoutRight;
    @BindView(R.id.rv_rank)
    RecyclerView mRankRecycler;
    RankAdapter mRankAdapter;

    @Override
    public boolean needHandleBackPress() {
        return true;    // 如果处理返回事件, 则返回true
    }

    @Override
    protected void doInit() {
        mRankAdapter = new RankAdapter();
        mRankRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRankRecycler.setHasFixedSize(true);
        mRankRecycler.setAdapter(mRankAdapter);
    }

    private void setRankVisibility(boolean showRank) {
        if (showRank) {
            mRankRecycler.setVisibility(View.VISIBLE);
            findViewById(R.id.tvTips).setVisibility(View.INVISIBLE);
        } else {
            mRankRecycler.setVisibility(View.INVISIBLE);
            findViewById(R.id.tvTips).setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        Record[] scoreArr = ScoreUtil.getScoreArr(AppData.getCurrentAccount(), AppData.isCurrentAdmin());
        mRankAdapter.setData(scoreArr, AppData.isCurrentAdmin());

        setRankVisibility(scoreArr.length!=0);

//        if(scoreArr.length == 0){
//            setRankVisibility(false);
//        }else{
//            setRankVisibility(true);
//            layoutLeft.removeAllViews();
//            layoutRight.removeAllViews();
//
//            String[] s = "一二三".split("");
//            for (int i = 0; i < scoreArr.length && i<3; i++) {
//                TextView tvLeft = new TextView(getContext());
//                tvLeft.setTextSize(28);
//                tvLeft.setTextColor(getResources().getColor(R.color.c1));
//                tvLeft.setText( ("第"+s[i+1]+"名") );
//                tvLeft.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                layoutLeft.addView(tvLeft);
//            }
//            for (int i = scoreArr.length - 1; i >= 0; i--) {
//                TextView tvRight = new TextView(getContext());
//                tvRight.setTextSize(28);
//                tvRight.setTextColor(getResources().getColor(R.color.c1));
//                tvRight.setText(String.valueOf(scoreArr[i]));
//                tvRight.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//                layoutRight.addView(tvRight);
//            }
//        }
    }



    @Override
    protected int getLayoutName() {
        return R.layout.fragment_rank;
    }
}
