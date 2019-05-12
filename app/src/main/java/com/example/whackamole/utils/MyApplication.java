package com.example.whackamole.utils;

import android.app.Application;
import android.content.Context;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/12
 */
public class MyApplication extends Application {
    /**
     * 全局的上下文
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }
}
