package com.example.whackamole.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.whackamole.utils.ContextImp;

/**
 * @Author: hu.wentao@outlook.om
 * @Date: 2019/4/7
 */
public class AppData {
    private static final String APP_DATE = "appDate";
    private static SharedPreferences preferenes;
    //= SharedPreference key
    public static final String SCORE_ARR_STRING = "top3Sore";  // 存放前三的分数, 以空格隔开
    public static final String IS_NORMAL_GAME_MODEL = "currentGameModel"; // 存放当前游戏模式(普通还是挑战)
    public static final String IS_ALLOW_BACK_MUSIC = "IS_ALLOW_BACK_MUSIC"; //
    public static final String IS_ALLOW_SHAKE = "IS_ALLOW_SHAKE"; //


//--------------------------------------------------------------------------------------------------
    private static synchronized SharedPreferences getPreferences() {
        if (preferenes == null)
            preferenes = ContextImp.getContext().getSharedPreferences(APP_DATE, Context.MODE_PRIVATE);
        return preferenes;
    }
    public static void setInt( String key, int val) {
        getPreferences().edit().putInt(key, val).apply();
    }
    public static void setString( String key, String val) {
        getPreferences().edit().putString(key, val).apply();
    }
    public static void setBoolean( String key, Boolean val) {
        getPreferences().edit().putBoolean(key, val).apply();
    }
    public static int getInt( String key, int defaultVal) {
        return getPreferences().getInt(key, defaultVal);
    }
    public static String getString( String key, String defaultVal) {
        return getPreferences().getString(key, defaultVal);
    }
    public static boolean getBoolean( String key, Boolean defaultVal) {
        return getPreferences().getBoolean(key, defaultVal);
    }
}
