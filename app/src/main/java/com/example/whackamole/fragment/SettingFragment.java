package com.example.whackamole.fragment;

import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class SettingFragment extends BaseFragment {
    @Override
    public boolean needHandleBackPress() {
        return true;
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_setting;
    }
}
