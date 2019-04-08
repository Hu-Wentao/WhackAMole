package com.example.whackamole.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.GameMainActivity;
import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppDate;

import butterknife.OnClick;


/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class GameFragment extends BaseFragment implements View.OnClickListener {
    // 老鼠洞 ImageView list
    public static SparseArray<ImageView> mRateHoleArray = new SparseArray<>(12);
    // 本局游戏得分
    private static int score;

    @Override
    public boolean needHandleBackPress() {
        return false;
    }

    @Override
    protected void doInit() {
        // 动态添加地洞
        addMiceHole();

        // 开始游戏
        if (AppDate.getBoolean(getContext(), AppDate.IS_NORMAL_GAME_MODEL, true)) {
            startNormalGame();
        } else {
            startChallengeGame();
        }

        // 展示游戏结算页面,
        endGame();
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_game_main;
    }


    /**
     * 动态添加地洞, 为 地洞view 配置tag(0 - 11)
     * 使用 mRateHoleArray 存储
     */
    private void addMiceHole() {
        GridLayout layout = findViewById(R.id.gridlayout_rat_hole);
        for (int i = 0; i < 12; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.drawable.img_rat_public_0);   // 设置默认图片
            // 添加点击事件监听器
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holeClick(v);
                }
            });
            // 将控件添加到list
            iv.setTag(i);   // 为hole 添加 tag
            mRateHoleArray.append(i, iv);

            //使用Spec定义子控件的位置和比重
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 3, 1f);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.height = 0;
            layoutParams.width = 0;
            //还可以根据位置动态定义子控件直接的边距，下面的意思是
            //第一行的子控件都有2dp的bottomMargin，中间位置的子控件都有2dp的leftMargin和rightMargin
            if (i / 3 == 0)
                layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
            if (i % 3 == 1) {
                layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
                layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
            }

            // 将控件添加到GridView
            layout.addView(iv, layoutParams);
        }

    }

    /**
     * 动态添加的 ratHole 的点击事件
     *
     * @param view 传入的是被点击的RatHole,
     */
    private void holeClick(View view) {
        // todo 判断被点击的view 是不是正在播放地鼠动画

    }

    private void startNormalGame() {
        // todo

    }

    private void startChallengeGame() {

    }

    private void endGame() {
        // todo
        findViewById(R.id.constrain_game_result).setVisibility(View.VISIBLE);
        score = 100;
        // 设置分数
        ((TextView) findViewById(R.id.tv_score)).setText(score + " 分");

    }

    @Override
    @OnClick({R.id.iv_pause_play, R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause_play:
                // todo
                break;
            case R.id.btn_next:
                ((GameMainActivity)getActivity()).changePage(2);
                //todo 在AppData 里面写一个操作 分数arr 的方法
                AppDate.setString(getContext(), AppDate.SCORE_ARR_STRING, "100");   // todo fake data
                break;
            default:
                if (BuildConfig.DEBUG) Log.d("swR+GameFragment", "未处理的点击事件...");
        }
    }

//    private AnimationDrawable getAnimationDrawable(){
//        AnimationDrawable animation = new AnimationDrawable();
//
//    }


}
