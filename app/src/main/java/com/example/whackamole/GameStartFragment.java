package com.example.whackamole;

import android.view.View;
import android.widget.Button;

import com.example.whackamole.base.BaseFragment;
import butterknife.BindView;

/**
 * Created by magical.zhang on 2018/5/21.
 * Description : 游戏开始页
 */
public class GameStartFragment extends BaseFragment {

    @BindView(R.id.btn_normal) Button mNormalGame;

    @Override
    protected void doLoadData() {
    }

    @Override
    protected void doInit() {

        mNormalGame.setSelected(true);
        mNormalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameMainActivity) mContext).changePage(1);
            }
        });
    }

//    @Override
//    protected boolean isNeedShowLoadingView() {
//        return false;
//    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_start;
    }
}
