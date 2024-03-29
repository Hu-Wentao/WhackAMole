package com.example.whackamole.game;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.whackamole.data.AppData;
import com.example.whackamole.fragment.GameFragment;
import com.example.whackamole.utils.AniUtils;
import com.example.whackamole.utils.MyApplication;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description : 随机线程
 */
public class GameThread extends Thread {
    ///===快速调整参数
    // 游戏时长
//    private static final long GAME_TIME = 60 * 1000;
    private static final long GAME_TIME = 10 * 1000;


    // 随机线程
    public GameThread(Handler handler) {
        // 为 Handler赋值
        this.mainHandler = handler;
        this.threadControl = true;
        this.gameRandom = new Random();     // 随机数生成器

        // 背景音乐控制
        mBgMusicManager = new BackgroundMusic(MyApplication.getContext());

        // 倒计时 计时器
        GameTimer.init(GAME_TIME, mainHandler);
    }

    @Override
    public void run() {
        super.run();
        while (threadControl) {     // 控制游戏是否允许
            // 控制速度             /////////////////////////////////////////////////////////

            int t = 0;  // 出现老鼠的间隔时间
            if (GameFragment.sCurrentGameState == 0) {
                if (GameFragment.isNormalModel) {                     // 普通模式 不同的关卡不同的速度
                    handleRandom();
                    switch (GameFragment.currentLevel) {
                        case 0:
                            t = 800;
                            break;
                        case 1:
                            t = 600;
                            break;
                        case 2:
                            t = 400;
                            break;
                    }
                } else {                                                // 挑战模式
                    handleRandom(4);    // 同时出现4只老鼠
                    t = 3000;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleRandom() {
        //随机出来 接下来要出现 地鼠的洞
        // 随机产生一个 {0, 1, 2}  中的数, 表示 红, 黄, 蓝 三种地鼠
        int i = randomHole();
        if (i == -1) {
            return;
        }
        Message.obtain(mainHandler,
                GameFragment.MSG_WHAT_REFRESH,
                i,   // 随机的洞
                // 如果不是普通模式, 则仅出现黄色地鼠
                GameFragment.isNormalModel ? gameRandom.nextInt(3) : AniUtils.RAT_COLOR_ORANGE)
                .sendToTarget();
    }
    private void handleRandom(int repeat){
        while (repeat-->0){
            handleRandom();
        }
    }

    /**
     * 找到一个未播放的洞
     */
    private int randomHole() {
        int primaryHoleIndex;  // 将要播放动画的 洞Index
        do {
            if (GameFragment.sOccupyHoleSet.size() == 12) {
                return -1;
            }
            // 执行随机找洞
            primaryHoleIndex = gameRandom.nextInt(12);
        }
        while (GameFragment.sOccupyHoleSet.contains(primaryHoleIndex) || isHolePlaying(primaryHoleIndex));
        GameFragment.sOccupyHoleSet.add(primaryHoleIndex);
        // 延迟移出
        Message m = Message.obtain();
        m.what = GameFragment.MSG_WHAT_REOME_ITEM_sOccupyHoleSet;
        m.arg1 = primaryHoleIndex;
        mainHandler.sendMessageDelayed(m, AniUtils.getAniDuration(false) + 200);

        return primaryHoleIndex;
    }

    /**
     * 检查洞是否有动画在播放, 这个与 mOccupyHoleSet重复了, 仅作为备选
     *
     * @param index 洞ID
     * @return 是否有动画播放
     */
    private boolean isHolePlaying(int index) {
        for (int i = 0; i < mRatAnimationArr.length; i++) {
            if (!GameFragment.isNormalModel && i != 1)
                continue;
            if (mRatAnimationArr[i][index].isRunning())
                return true;
        }
        return false;
    }

    public void startGame() {
        GameTimer.start();
        if (AppData.getBoolean(AppData.IS_ALLOW_BACK_MUSIC, true)) {
            mBgMusicManager.playBackgroundMusic(getMusic(), true);
        }
    }

    public void stopGame() {
//        mCountDownTimer.cancel();
        GameTimer.cancel();
        // 背景音乐
        mBgMusicManager.stopBackgroundMusic();
        mBgMusicManager.end();
        threadControl = false;
    }

    public void release() {
        if (null != mBgMusicManager) {
            mBgMusicManager.stopBackgroundMusic();
            mBgMusicManager.end();
        }
    }

    @NonNull
    private String getMusic() {
        return "game_bg.mp3";
    }

    private boolean threadControl;  // 线程控制..是否开始游戏..

    private Handler mainHandler;    // 传入的Handler
    private Random gameRandom;      // 随机数

    private AnimationDrawable[][] mRatAnimationArr = GameFragment.sRatAnimationArr;

    private BackgroundMusic mBgMusicManager;


}
