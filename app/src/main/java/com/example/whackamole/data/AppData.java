package com.example.whackamole.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class AppData {
    private static final String APP_DATE = "appDate";
    private static SharedPreferences preferences;
    //= SharedPreference key
    public static final String SCORE_ARR_STRING = "top3Score";  // 存放前三的分数, 以空格隔开
    public static final String IS_NORMAL_GAME_MODEL = "currentGameModel"; // 存放当前游戏模式(普通还是挑战)

    //= Tag Key
//    public static final int
//            HOLE_CURRENT_COLOR = 2,
//            HOLE_TYPE_ADD_COLOR = 1,
//            HOLE_ID = 0;


//--------------------------------------------------------------------------------------------------
    private static synchronized SharedPreferences getPreferences(Context c) {
        if (preferences == null)
            preferences = c.getSharedPreferences(APP_DATE, Context.MODE_PRIVATE);
        return preferences;
    }
    public static void setInt(Context c, String key, int val) {
        getPreferences(c).edit().putInt(key, val).apply();
    }
    public static void setString(Context c, String key, String val) {
        getPreferences(c).edit().putString(key, val).apply();
    }
    public static void setBoolean(Context c, String key, Boolean val) {
        getPreferences(c).edit().putBoolean(key, val).apply();
    }
    public static int getInt(Context c, String key, int defaultVal) {
        return getPreferences(c).getInt(key, defaultVal);
    }
    public static String getString(Context c, String key, String defaultVal) {
        return getPreferences(c).getString(key, defaultVal);
    }
    public static boolean getBoolean(Context c, String key, Boolean defaultVal) {
        return getPreferences(c).getBoolean(key, defaultVal);
    }
}
