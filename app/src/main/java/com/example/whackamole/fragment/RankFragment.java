package com.example.whackamole.fragment;

import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.TextView;

import com.example.whackamole.BuildConfig;
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

//        scoreArr = new String[]{"12", "343", "01"};   //fake info
        if(scoreArr.length == 0){
            setVisibilityScoreTips(true);
        }else{
            setVisibilityScoreTips(false);
            Arrays.sort(scoreArr);
            for (int i = 0; i < scoreArr.length; i++) {
                switch (i) {
                    case 0:
                        findViewById(R.id.tvScore_1).setVisibility(View.VISIBLE);
                        findViewById(R.id.tvShow1).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.tvScore_1)).setText(scoreArr[i]);
                        break;
                    case 1:
                        findViewById(R.id.tvScore_2).setVisibility(View.VISIBLE);
                        findViewById(R.id.tvShow2).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.tvScore_2)).setText(scoreArr[i]);
                        break;
                    case 2:
                        findViewById(R.id.tvScore_3).setVisibility(View.VISIBLE);
                        findViewById(R.id.tvShow3).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.tvScore_3)).setText(scoreArr[i]);
                        break;
                    default:
                        if (BuildConfig.DEBUG) Log.d("swR+RankFragment", "未处理的点击事件...");
                }
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
