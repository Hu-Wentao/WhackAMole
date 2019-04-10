package com.example.whackamole.game;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.whackamole.fragment.GameFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Description : 随机线程
 */
public class RandomThread extends Thread {
    // 随机线程
    public RandomThread(Context context, Handler handler, HashSet<Integer> container) {
        // 为 Handler赋值
        this.mainHandler = handler;
        this.threadControl = true;
        this.startFlag = false;     // 游戏是否已开始
        this.gameRandom = new Random();     // 随机数生成器
        this.mKeepSet = container;  // 存放的是 地洞的序号

        // todo 背景音乐控制
//        mBgMusicManager = new BackgroundMusic(context);

        // 倒计时 计时器
        mCountDownTimer = new CountDownTimer(GAME_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 1300) {
                    isNearEnd = true;
                }
                // 发送一个消息
                Message.obtain(mainHandler, GameFragment.MSG_GAME_INTERVAL, (int) (millisUntilFinished / 1000), 0).sendToTarget();
            }

            @Override
            public void onFinish() {
                mainHandler.sendEmptyMessage(GameFragment.MSG_GAME_END);
            }
        };
    }

    public void setAnimList(ArrayList<AnimationDrawable> ratList,
                            ArrayList<AnimationDrawable> mushList) {
        this.mRatAnimList = ratList;
        this.mMushRoomAnimList = mushList;
    }

    @Override
    public void run() {
        super.run();
        while (threadControl) {     // 控制游戏是否允许
            if (startFlag && !isNearEnd) {
                handleRandom();
                try {
                    // 控制速度             /////////////////////////////////////////////////////////
                    ++speedControl;
                    if (speedControl > 20) {
                        Thread.sleep(300);
                    } else if (speedControl > 15) {
                        Thread.sleep(460);
                    } else if (speedControl > 10) {
                        Thread.sleep(550);
                    } else if (speedControl > 5) {
                        Thread.sleep(750);
                    } else {
                        Thread.sleep(850);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private void handleRandom() {
        //Log.d(TAG, "handleRandom -- 新的循环 找洞start -- ");

        //随机出来 接下来要出现 精灵的洞
        int hole = randomHole();
        //Log.d(TAG, "handleRandom -- 检测到可以出现精灵的洞 random hole : " + hole);
        Message obtain = Message.obtain(mainHandler);
        obtain.what = GameFragment.MSG_GAME_REFRESH;
        obtain.arg1 = hole;

        // 随机产生一个 {0, 1, 2} 中的数, 表示 红, 黄, 蓝 三种地鼠
        obtain.arg2 = gameRandom.nextInt(3);

        obtain.sendToTarget();
    }

    /**
     * 找到一个未播放的洞
     * 检测不符合要求就递归
     */
    private int randomHole() {

        //Log.d(TAG, "randomHole -- 执行随机找洞");

        int turnUp = gameRandom.nextInt(9);
        //Log.d(TAG, " randomHole -- 随机出来的数 : " + turnUp);

        boolean isPlaying = checkPlayingHole(turnUp);
        boolean isKeep = mKeepSet.contains(turnUp);

        if (isPlaying || isKeep) {
            return randomHole();
        } else {
            return turnUp;
        }
    }

    /**
     * 检查洞是否符合条件
     */
    private boolean checkPlayingHole(int turnUp) {

        AnimationDrawable animationDrawable1 = mRatAnimList.get(turnUp);
        AnimationDrawable animationDrawable2 = mMushRoomAnimList.get(turnUp);
        return animationDrawable1.isRunning() || animationDrawable2.isRunning();
    }

    public void startGame() {
        startFlag = true;
        speedControl = 0;
        mCurMushroomCount = 0;
        mCountDownTimer.start();
        // todo 背景音乐
//        mBgMusicManager.playBackgroundMusic(getMusic(), true);
    }

    @NonNull
    private String getMusic() {
        return bgIndex == 0 ? "game_bg.mp3" : "game_bg2.mp3";
    }

    public void changeBGMusic() {
        bgIndex = bgIndex == 0 ? 1 : 0;
    }

    public void stopGame() {
        mCountDownTimer.cancel();
        //todo 背景音乐
//        mBgMusicManager.stopBackgroundMusic();
//        mBgMusicManager.end();
        threadControl = false;
    }

    public void release() {

        // todo 背景音乐
//        if (null != mBgMusicManager) {
//            mBgMusicManager.stopBackgroundMusic();
//            mBgMusicManager.end();
//        }
    }


    ///===参数
    // 游戏时长
    private static final long GAME_TIME = 18 * 1000;

    private static final int MAX_MUSHROOM = 12;

    private boolean threadControl;  // 线程控制..是否开始游戏..
    private boolean startFlag;  // 开始标志

    private Handler mainHandler;    // 传入的Handler
    private Random gameRandom;      // 随机数
    private HashSet<Integer> mKeepSet;
    private ArrayList<AnimationDrawable> mRatAnimList;
    private ArrayList<AnimationDrawable> mMushRoomAnimList;

    //倒计时
    private CountDownTimer mCountDownTimer;// 与 GAME_TIME 配合, Interval为 1000毫秒
    // TODO 背景音乐
//    private BackgroundMusic mBgMusicManager;
    private boolean isNearEnd;  //判断接近尾声 就不在出精灵了
    private int mCurMushroomCount;

    private int speedControl;   // 控制游戏节奏
    private int bgIndex;
}
