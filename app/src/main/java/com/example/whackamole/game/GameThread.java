package com.example.whackamole.game;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.whackamole.fragment.GameFragment;
import com.example.whackamole.utils.PhraseUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description : 随机线程
 */
public class GameThread extends Thread {
    // 随机线程
    public GameThread(Context context, Handler handler, HashSet<Integer> container) {
        // 为 Handler赋值
        this.mainHandler = handler;
        this.threadControl = true;
        this.startFlag = false;     // 游戏是否已开始
        this.gameRandom = new Random();     // 随机数生成器
        this.mOccupyHoleSet = container;  // 存放的是 不允许播放动画的 地洞的序号(防止动画重复播放)

        // todo 背景音乐控制
//        mBgMusicManager = new BackgroundMusic(context);

        // 倒计时 计时器
        GameTimer.init(GAME_TIME, mainHandler);
    }

    @Override
    public void run() {
        super.run();
        while (threadControl) {     // 控制游戏是否允许
            if (GameFragment.sCurrentGameState == 0) {
                handleRandom();
                try {
                    // 控制速度             /////////////////////////////////////////////////////////
                    ++speedControl;
                    if (speedControl > 20) {
                        Thread.sleep(400);
                    } else if (speedControl > 15) {
                        Thread.sleep(560);
                    } else if (speedControl > 10) {
                        Thread.sleep(650);
                    } else if (speedControl > 5) {
                        Thread.sleep(850);
                    } else {
                        Thread.sleep(950);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private void handleRandom() {
        //随机出来 接下来要出现 精灵的洞
        // 随机产生一个 {0, 1, 2}  中的数, 表示 红, 黄, 蓝 三种地鼠
        int colorOrPhraseIndex;
        if(GameFragment.isNormalModel){
            colorOrPhraseIndex = gameRandom.nextInt(3);
        }else {
            colorOrPhraseIndex = PhraseUtils.getNextIndex();
        }
        Message.obtain(mainHandler,
                GameFragment.MSG_WHAT_REFRESH,
                randomHole(),   // 随机的洞
                colorOrPhraseIndex)
                .sendToTarget();
    }

    /**
     * 找到一个未播放的洞
     */
    private int randomHole() {
        int primaryHoleIndex;  // 将要播放动画的 洞Index
        do {
            if (mOccupyHoleSet.size() == 12) {  // todo 发现问题....
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 执行随机找洞
            primaryHoleIndex = gameRandom.nextInt(12);
        } while (mOccupyHoleSet.contains(primaryHoleIndex) || isHolePlaying(primaryHoleIndex));
        return primaryHoleIndex;
    }

    /**
     * 检查洞是否有动画在播放, 这个与 mOccupyHoleSet重复了, 仅作为备选
     *
     * @param turnUp 洞ID
     * @return 是否有动画播放
     */
    private boolean isHolePlaying(int turnUp) {
        for (int i = 0; i < mRatAnimationArr.length; i++) {
            if (!GameFragment.isNormalModel && i != 1)
                continue;
            for (int j = 0; j < mRatAnimationArr[i].length; j++) {
                if (mRatAnimationArr[i][j].isRunning())
                    return true;
            }
        }
        return false;
    }

    public void startGame() {
        startFlag = true;
        speedControl = 0;
//        mCurMushroomCount = 0;    //todo del
//        mCountDownTimer.start();
        GameTimer.start();
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
//        mCountDownTimer.cancel();
        GameTimer.cancel();
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
//    private static final long GAME_TIME = 18 * 1000;
    private static final long GAME_TIME = 10 * 1000;     //todo test

//    private static final int MAX_MUSHROOM = 12;

    private boolean threadControl;  // 线程控制..是否开始游戏..
    private boolean startFlag;  // 开始标志

    private Handler mainHandler;    // 传入的Handler
    private Random gameRandom;      // 随机数
    private HashSet<Integer> mOccupyHoleSet;  // 存放的是地洞序号,...
    private AnimationDrawable[][] mRatAnimationArr;

    public void setAnimationArr(AnimationDrawable[][] AnimationArr) {
        mRatAnimationArr = AnimationArr;
    }

    //游戏倒计时,定时发送Message:  MSG_WHAT_INTERVAL
//    public static CountDownTimer mCountDownTimer;// 与 GAME_TIME 配合, Interval为 1000毫秒
    // TODO 背景音乐
//    private BackgroundMusic mBgMusicManager;

    private int speedControl;   // 控制游戏节奏
    private int bgIndex;


}
