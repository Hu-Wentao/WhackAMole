package com.example.whackamole.data;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/10
 */
public class Score {
    // 4个空间, 每次都将新添加的放在 [3]中
//    private static String[] scoreArr = new String[4];
    private static LinkedList<Integer> scoreList = new LinkedList<>();
    public static void saveScore(int score){
        scoreList.add(score);

        // 排序
        // todo
    }
}
