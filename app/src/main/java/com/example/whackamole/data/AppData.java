package com.example.whackamole.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.activities.MainActivity;
import com.example.whackamole.utils.MyApplication;

/**
 * @Author: hu.wentao@outlook.om
 * @Date: 2019/4/7
 */
public class AppData {
    //= SharedPreference key
    public static final String IS_NORMAL_GAME_MODEL = "currentGameModel"; // 存放当前游戏模式(普通还是挑战)
    public static final String IS_ALLOW_BACK_MUSIC = "是否允许背景音乐播放"; //
    public static final String IS_ALLOW_SHAKE = "是否允许震动"; //
    //--------------------------------------------------------------------------------------------------
    private static final String CURRENT_ACCOUNT = "current_account"; // 当前用户名的 key
    private static final String CURRENT_ACCOUNT_IS_ADMIN = "isAdmin";    // 当前登录的用户是否是管理员
    //--------------------------------------------------------------------------------------------------
    private static SharedPreferences preferences;   // 请使用get方法操作该变量

    public static String getCurrentAccount() {
        return getPreferences()
                .getString(CURRENT_ACCOUNT, null);
    }

    public static boolean isCurrentAdmin() {
        return AppData.getBoolean(CURRENT_ACCOUNT_IS_ADMIN, false);
    }

    /**
     * 设置当前的用户名, (如果用户成功登陆的话)
     * 如果 userAccount == null 表示注销当前用户
     */
    public static void setCurrentAccount(String userAccount, boolean isAdminLogin) {
        getPreferences()
                .edit()
                .putString(CURRENT_ACCOUNT, userAccount)
                .putBoolean(CURRENT_ACCOUNT_IS_ADMIN, isAdminLogin)
                .apply();
    }

    public static void setCurrentAccount(String userAccount) {
        setCurrentAccount(userAccount, false);
    }


    //--------------------------------------------------------------------------------------------------

    private static synchronized SharedPreferences getPreferences() {
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences;
    }

    public static void setBoolean(String key, Boolean val) {
        getPreferences().edit().putBoolean(key, val).apply();
    }

    public static boolean getBoolean(String key, Boolean defaultVal) {
        return getPreferences().getBoolean(key, defaultVal);
    }
}
