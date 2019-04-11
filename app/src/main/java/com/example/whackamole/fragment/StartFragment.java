package com.example.whackamole.fragment;

import android.util.Log;
import android.view.View;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.MainActivity;
import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppData;

import butterknife.OnClick;

/**
 * Created by magical.zhang on 2018/5/21.
 * Description : 游戏开始页
 */
public class StartFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public boolean needHandleBackPress() {
        return true;
    }

    @Override
    protected void doInit() {}

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
                AppData.setBoolean(getContext(), AppData.IS_NORMAL_GAME_MODEL, true);
                ((MainActivity) mContext).changePage(1);
                break;
            case R.id.btn_challenge:
                AppData.setBoolean(getContext(), AppData.IS_NORMAL_GAME_MODEL, false);
                ((MainActivity) mContext).changePage(1);
                break;
            case R.id.btn_rank:
                ((MainActivity) mContext).changePage(2);
                break;
            case R.id.btn_setting:
                ((MainActivity) mContext).changePage(3);
                break;
            default:
                if (BuildConfig.DEBUG) Log.d("swR+StartFragment", "未处理的点击事件...");
        }
    }
}
