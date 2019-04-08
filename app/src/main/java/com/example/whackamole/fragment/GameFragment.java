package com.example.whackamole.fragment;

import android.support.v7.widget.GridLayout;
import android.widget.ImageView;

import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;

import java.util.ArrayList;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class GameFragment extends BaseFragment {
    // 老鼠洞 ImageView list
    public static ArrayList<ImageView> mRatHoleList = new ArrayList<>();

    @Override
    protected void doInit() {
        // 动态添加地洞
        addMiceHole();
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_game_main;
    }

    /**
     * 动态添加地洞
     */
    private void addMiceHole() {
        GridLayout layout = findViewById(R.id.gridlayout);
        for (int i = 0; i < 12; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.drawable.img_rat_public_0);   // 设置默认图片
            // 将控件添加到list
            mRatHoleList.add(iv);

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

}
