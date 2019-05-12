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
    public static final String SCORE_ARR_STRING = "top3Sore";  // 存放前三的分数, 以空格隔开    // todo
    public static final String IS_NORMAL_GAME_MODEL = "currentGameModel"; // 存放当前游戏模式(普通还是挑战)
    public static final String IS_ALLOW_BACK_MUSIC = "IS_ALLOW_BACK_MUSIC"; //
    public static final String IS_ALLOW_SHAKE = "IS_ALLOW_SHAKE"; //
    //--------------------------------------------------------------------------------------------------
    private static final String CURRENT_ACCOUNT = "current_account"; // 当前用户名的 key
    private static final String CURRENT_ACCOUNT_IS_ADMIN = "isAdmin";    // 当前登录的用户是否是管理员
    //--------------------------------------------------------------------------------------------------

    public static String getCurrentAccount() {
        return AppData.getString(CURRENT_ACCOUNT, null);
    }
    public static boolean isCurrentAdmin(){
        return AppData.getBoolean(CURRENT_ACCOUNT_IS_ADMIN, false);
    }
    /**
     * 设置当前的用户名, (如果用户成功登陆的话)
     * 如果 userAccount == null 表示注销当前用户
     */
    public static boolean setCurrentAccount(String userAccount, boolean isAdminLogin) {
        SharedPreferences sharePreference = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if (userAccount == null) {    // 注销登录
            return sharePreference.edit()
                    .putString(CURRENT_ACCOUNT, null)
                    .putBoolean(CURRENT_ACCOUNT_IS_ADMIN, false)
                    .commit();
        }
        return sharePreference.edit()
                .putString(CURRENT_ACCOUNT, userAccount)
                .putBoolean(CURRENT_ACCOUNT_IS_ADMIN, isAdminLogin)
                .commit();
    }

    public static boolean setCurrentAccount(String userAccount) {
        return setCurrentAccount(userAccount, false);
    }


    //--------------------------------------------------------------------------------------------------
    private static SharedPreferences preferences;

    private static synchronized SharedPreferences getPreferences() {
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences;
    }

    public static void setInt(String key, int val) {
        getPreferences().edit().putInt(key, val).apply();
    }

    public static int getInt(String key, int defaultVal) {
        return getPreferences().getInt(key, defaultVal);
    }

    public static void setString(String key, String val) {
        getPreferences().edit().putString(key, val).apply();
    }

    public static void setBoolean(String key, Boolean val) {
        getPreferences().edit().putBoolean(key, val).apply();
    }

    public static String getString(String key, String defaultVal) {
        return getPreferences().getString(key, defaultVal);
    }

    public static boolean getBoolean(String key, Boolean defaultVal) {
        return getPreferences().getBoolean(key, defaultVal);
    }
}
