package com.example.whackamole.fragment;

import android.view.View;

import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppDate;

import java.util.Arrays;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class RankFragment extends BaseFragment {
    @Override
    protected void doInit() {
        // 查询AppDate, defaultVal 必须为 " " 才能正确的显示结果
        String[] scoreArr = AppDate.getString(getContext(), AppDate.SCORE_ARR_STRING, " ").split(" ");
        if(scoreArr.length == 0){
            setVisibilityScoreTips(true);
        }else{
            setVisibilityScoreTips(false);
            Arrays.sort(scoreArr);
            for (int i = 0; i < scoreArr.length; i++) {

            }
        }
    }

    /**
     * 设置是否显示分数的 textView
     * @param show
     */
    private void setVisibilityScoreTips(boolean show){
        if (show){
            findViewById(R.id.tvTips).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tvTips).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_rank;
    }
}
