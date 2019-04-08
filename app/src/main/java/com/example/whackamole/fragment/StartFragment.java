package com.example.whackamole.fragment;

import android.util.Log;
import android.view.View;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.GameMainActivity;
import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppDate;

import butterknife.OnClick;

/**
 * Created by magical.zhang on 2018/5/21.
 * Description : 游戏开始页
 */
public class StartFragment extends BaseFragment implements View.OnClickListener {

    // 绑定View
//    @BindView(R.id.btn_normal) Button mBtnNormalModel;
//    @BindView(R.id.btn_challenge) Button mBtnChallengeModel;
//    @BindView(R.id.btn_rank) Button mBtnRank;
//    @BindView(R.id.btn_setting) Button mBtnSetting;

    @Override
    public boolean needHandleBackPress() {
        return true;
    }

    @Override
    protected void doInit() {
//        mBtnNormalModel.setSelected(true);
//        mBtnNormalModel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((GameMainActivity) mContext).changePage(1);
//            }
//        });

    }



    @Override
    protected int getLayoutName() {
        return R.layout.fragment_start;
    }

    @Override
    @OnClick({R.id.btn_normal, R.id.btn_challenge, R.id.btn_rank, R.id.btn_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                // 设置当前游戏模式
                AppDate.setBoolean(getContext(), AppDate.IS_NORMAL_GAME_MODEL, true);
                ((GameMainActivity) mContext).changePage(1);
                break;
            case R.id.btn_challenge:
                AppDate.setBoolean(getContext(), AppDate.IS_NORMAL_GAME_MODEL, false);
                ((GameMainActivity) mContext).changePage(1);
                break;
            case R.id.btn_rank:
                ((GameMainActivity) mContext).changePage(2);
                break;
            case R.id.btn_setting:
                ((GameMainActivity) mContext).changePage(3);
                break;
            default:
                if (BuildConfig.DEBUG) Log.d("swR+StartFragment", "未处理的点击事件...");
        }
    }
}
