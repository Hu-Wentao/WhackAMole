package com.example.whackamole.data;

import android.content.Context;
import android.content.Intent;

import java.util.Arrays;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/10
 */
public class Score {
    // 4个空间, 每次都将新添加的放在 [3]中
//    private static String[] scoreArr = new String[4];

    public static int[] getScoreArr(Context c) {
        String[] arr = AppData.getString(c, AppData.SCORE_ARR_STRING, " ").split(" +");

        int[] r = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if(arr[i].equals("")){
                continue;
            }
            r[i] = Integer.parseInt(arr[i]);
        }
        Arrays.sort(r);
        if(r.length>3){
            int[] t = new int[3];
            System.arraycopy(r, r.length-3, t, 0, 3);
            AppData.setString(c, AppData.SCORE_ARR_STRING, t[0]+" "+t[1]+" "+t[2]+" ");

            System.out.println(Arrays.toString(r) +"\n"+Arrays.toString(r)); //todo
            return t;
        }
        return r;
    }

    public static void addScore(String score, Context c) {
        AppData.setString(c, AppData.SCORE_ARR_STRING, AppData.getString(c, AppData.SCORE_ARR_STRING, "")+score+" ");  // 存入的是无序数组
    }




}
