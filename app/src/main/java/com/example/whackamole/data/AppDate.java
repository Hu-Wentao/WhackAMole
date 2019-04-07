package com.example.whackamole.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class AppDate {
    public static final String APP_DATE = "appDate";
    private static SharedPreferences preferences;
    //= key
    public static final String SCORE_ARR_STRING = "top3Score";  // 存放前三的分数, 以空格隔开

    private static synchronized SharedPreferences getPreferences(Context c){
        if(preferences == null){
            preferences = c.getSharedPreferences(APP_DATE, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static void saveInt(Context c, String key, int val){
        getPreferences(c).edit().putInt(key, val).apply();
    }
    public static void saveString(Context c, String key, String val){
        getPreferences(c).edit().putString(key, val).apply();
    }
    public static void saveBoolean(Context c, String key, Boolean val){
        getPreferences(c).edit().putBoolean(key, val).apply();
    }

    public static int getInt(Context c, String key, int defaultVal){
        return getPreferences(c).getInt(key, defaultVal);
    }

    public static String getString(Context c, String key, String defaultVal){
        return getPreferences(c).getString(key, defaultVal);
    }
    public static boolean getBoolean(Context c, String key, Boolean defaultVal){
        return getPreferences(c).getBoolean(key, defaultVal);
    }
}
