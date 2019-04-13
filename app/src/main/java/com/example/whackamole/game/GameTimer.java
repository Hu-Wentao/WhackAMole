package com.example.whackamole.game;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import com.example.whackamole.fragment.GameFragment;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/11
 */
public class GameTimer {
    private static CountDownTimer timer;

    private static long saveMillisUntilFinished;
    private static Handler saveHandler;
    private static int saveSpeedControl;

    public static void init(long gameTime, final Handler mHandler){
        saveHandler = mHandler;

        timer = new CountDownTimer(gameTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                saveMillisUntilFinished = millisUntilFinished;
                // 发送一个消息
                Message.obtain(mHandler, GameFragment.MSG_WHAT_INTERVAL, (int) (millisUntilFinished / 1000), 0).sendToTarget();
            }
            @Override
            public void onFinish() {
                mHandler.sendEmptyMessage(GameFragment.MSG_WHAT_END);
            }
        };
    }

    public static void start(){
        timer.start();
    }
    public static void pause(){
        timer.cancel();
        saveSpeedControl = GameThread.speedControl;
    }
    public static void play(){
        init(saveMillisUntilFinished+1000, saveHandler);
        GameThread.speedControl = saveSpeedControl; // 可能没什么用
    }
    public static void cancel(){
        saveMillisUntilFinished = 0;
        saveHandler = null;
        timer.cancel();
    }


}
