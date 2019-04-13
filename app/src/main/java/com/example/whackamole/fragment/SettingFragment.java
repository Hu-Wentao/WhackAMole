package com.example.whackamole.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppData;
import com.example.whackamole.utils.ContextImp;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class SettingFragment extends BaseFragment {
    @BindView(R.id.sw_backMusic)
    Switch swBackMusic;
    @BindView(R.id.sw_Shake)
    Switch swShake;




    @Override
    public boolean needHandleBackPress() {
        return true;
    }

    @Override
    protected void doInit() {
        swBackMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppData.setBoolean(AppData.IS_ALLOW_BACK_MUSIC, isChecked);
            }
        });
        swShake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppData.setBoolean(AppData.IS_ALLOW_SHAKE, isChecked);
            }
        });


    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_setting;
    }
}
