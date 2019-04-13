package com.example.whackamole.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/13
 */
public class VibratorUtils {
    private static Vibrator vibrator;

    private static synchronized Vibrator getVibrator(){
        if(vibrator == null){
            vibrator = (Vibrator)ContextImp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }

    public static void shake(int ms){
        getVibrator().vibrate(ms);
    }

}
