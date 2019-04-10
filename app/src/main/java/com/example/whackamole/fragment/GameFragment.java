package com.example.whackamole.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.MainActivity;
import com.example.whackamole.R;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppDate;
import com.example.whackamole.game.GameThread;
import com.example.whackamole.utils.AniUtils;

import java.util.HashSet;

import butterknife.OnClick;


/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class GameFragment extends BaseFragment implements View.OnClickListener {
    // 老鼠洞 ImageView list
    private SparseArray<ImageView> mRateHoleArray = new SparseArray<>(12);
    // 正在播放动画的洞的序号
    private HashSet<Integer> mPlayingHole = new HashSet<>(12);
    // 当前游戏模式
    private boolean isNormalModel;
    // 当前游戏状态  -1 未开始, 0 开始, 1 结束
    private byte currentGameState = -1;
    // 当前游戏得分
    private int currentScore;
    // 游戏逻辑线程
    private GameThread mGameThread;
    // 动画数组       3: 3种颜色的地鼠, 12: 12个洞
    AnimationDrawable[][] mRatAnimationArr = new AnimationDrawable[3][12];
    // 被击打动画数组 3: 3种颜色的地鼠, 12: 12个地洞
    AnimationDrawable[][] mRatHitAnimationArr = new AnimationDrawable[3][12];
    // Handler 的what类型
    public static final int MSG_GAME_START = 0;
    public static final int MSG_GAME_REFRESH = 1;
    public static final int MSG_GAME_INTERVAL = 2;
    public static final int MSG_GAME_END = 3;
    public static final int MSG_GAME_ANI_STOP = 4;
    // Handler arg1, 游戏剩余时间...
    // Handler arg2, 选择模型类型 -> 参见 AniUtils -> RAT_COLOR_*


    @Override   // 返回事件
    public boolean needHandleBackPress() {
        return false;
    }

    @Override
    protected void doInit() {
        isNormalModel = AppDate.getBoolean(getContext(), AppDate.IS_NORMAL_GAME_MODEL, true);
        {   // 初始化一些信息 - 不要改动顺序
            // 动态添加地洞 // 考虑根据情况同时添加TextView
            addMiceHole(isNormalModel);
            // 初始化每个地洞对应的动画
            initRatAnimation(isNormalModel);
        }
        {   // 开始游戏逻辑
            // onGameGuide() // todo 1. 建立一个游戏指引(游戏开始前倒计时之类)
            // 倒计时结束, 发送一个Message, 开始游戏
            mGameHandler.sendEmptyMessage(MSG_GAME_START);
        }

    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_game_main;
    }

    private void initRatAnimation(boolean isNormalModel) {
        // 为每个洞添加红地鼠的, 伸头, 缩头, 被打 动画
        for (int i = 0; i < mRatAnimationArr.length; i++) {
            for (int j = 0; j < mRatAnimationArr[i].length; j++) {
                mRatAnimationArr[i][j] = (AnimationDrawable) AniUtils.getAnimationByName(getContext(), AniUtils.RAT_TYPE_NORMAL, i+1).getConstantState().newDrawable();
            }
        }
        for (int i = 0; i < mRatHitAnimationArr.length; i++) {
            for (int j = 0; j < mRatHitAnimationArr[i].length; j++) {
                mRatAnimationArr[i][j] = (AnimationDrawable) AniUtils.getAnimationByName(getContext(), AniUtils.RAT_TYPE_BEATEN, i+1).getConstantState().newDrawable();
            }
        }
    }

    /**
     * 动态添加地洞, 为 地洞view 配置tag(0 - 11)
     * 使用 mRateHoleArray 存储
     * //todo 在此处实现成语打地鼠的 TextVIew, 考虑添加 boolean isNormalModel 参数
     */
    private void addMiceHole(boolean isNormalModel) {
        GridLayout layout = findViewById(R.id.gridlayout_rat_hole);
        // todo 考虑同时添加一个 TextView, 并将GridView 换成 8行 3列的
        for (int i = 0; i < 12; i++) {
            ImageView iv = new ImageView(getContext());

            iv.setBackgroundResource(R.drawable.img_rat_public_0);   // 设置默认图片 todo 考虑删除

            iv.setTag(R.id.hole_id, i);   // 为hole 添加 tag
            // 添加点击事件监听器
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHoleClick(v);
                }
            });
            // 将控件添加到list
            mRateHoleArray.append(i, iv);

            //使用Spec定义子控件的位置和比重
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 3, 1f);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.height = 0;
            layoutParams.width = 0;
            //还可以根据位置动态定义子控件直接的边距，下面的意思是
            //第一行的子控件都有2dp的bottomMargin，中间位置的子控件都有2dp的leftMargin和rightMargin
            if (i / 3 == 0)
                layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
            if (i % 3 == 1) {
                layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
                layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.rat_hole_margin);
            }

            // 将控件添加到GridView
            layout.addView(iv, layoutParams);
        }
    }

    /**
     * 动态添加的 ratHole 的点击事件
     *
     * @param v 传入的是被点击的RatHole,
     */
    private void onHoleClick(View v) {
        final int holeId = (int) v.getTag(R.id.hole_id);
        // 判断是否开始
        if (currentGameState != 0) {
            return;
        }
        // 播放动画


        // 获取该洞的所属的动画
        AnimationDrawable aniDrawable = mRatAnimationArr[(int) v.getTag(R.id.hole_current_color)][holeId];
        // todo 可能BUG 没有添加 v被点击的洞的 Tag - HOLE_CURRENT_COLOR
        // 如果 被点击的洞不在播放动画, 则退出方法 (用户点击了没有播放动画的洞)
        if (aniDrawable == null || !aniDrawable.isRunning()) {
            return;
        }
        mGameHandler.removeMessages(MSG_GAME_ANI_STOP, aniDrawable);
        aniDrawable.stop();
        aniDrawable.selectDrawable(0);  // 将动画设置到第一张(初始状态)
        // 处理得分情况, //todo 可以在此处同时播放音效
        switch ((int) v.getTag(R.id.hole_current_color)) {
            case AniUtils.RAT_COLOR_RED:
                currentScore += 10;
                break;
            case AniUtils.RAT_COLOR_ORANGE:
                currentScore += 20;
                break;
            case AniUtils.RAT_COLOR_BLUE:
                currentScore -= 15;
                break;
        }
        // 处理动画的播放 和在 HashSet mPlayingHole 中标记正在播放动画的洞
        final ImageView view = mRateHoleArray.get(holeId);
        mPlayingHole.add(holeId);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackgroundResource(R.drawable.img_rat_public_0);
                mPlayingHole.remove(holeId);
            }
        }, 1000);
    }

    private void onGameStart(boolean isNormalModel) {
        // 声明游戏开始
        currentGameState = 0;   // 0 : 游戏运行中
        // 初始化游戏数据
        currentScore = 0;

        // 开启线程
        mGameThread = new GameThread(getContext(), mGameHandler, mPlayingHole);
        mGameThread.setAnimationArr(mRatAnimationArr);

        // 开启线轮询
        mGameThread.start();
        mGameThread.startGame();
    }

    private void onGameRefresh(int hole, int aniColor) {
        // 要播放的动画
        AnimationDrawable targetAniDrawable = mRatAnimationArr[aniColor][hole];
        // 获取该洞的view
        ImageView view = mRateHoleArray.get(hole);
        // 清空背景,并设置动画
        view.setBackgroundResource(0);
        view.setBackground(targetAniDrawable);
        // 设置tag, 标记这个view播放的动画的颜色
        view.setTag(R.id.hole_current_color, aniColor);

        // 设置AnimationDrawable可见性, 然后播放动画
        //todo 设置 restart 为 true, 在成语模式下, 应该会 保证游戏运行正常.....
        targetAniDrawable.setVisible(true, true);
        targetAniDrawable.start();


        // 获取该动画的播放时长
        int duration = 0;
        for (int i = 0; i < targetAniDrawable.getNumberOfFrames(); i++) {
            duration += targetAniDrawable.getDuration(i);
        }

        // 发送一个消息, 表示该动画的播放时长, 设置在动画时长结束后,停止播放动画
        Message obtain = Message.obtain();
        obtain.what = MSG_GAME_ANI_STOP;
        obtain.arg1 = hole;
        obtain.arg2 = aniColor;
        obtain.obj = targetAniDrawable;
        mGameHandler.sendMessageDelayed(obtain, duration);
    }

    private void onGameOver() {
        // 显示 下一步 按钮
        findViewById(R.id.constrain_game_result).setVisibility(View.VISIBLE);
        currentScore = 100; // todo fake data
        // 设置分数
        ((TextView) findViewById(R.id.tv_pause_play)).setText(currentScore + " 分");
        // 执行游戏结束
        mGameThread.stopGame();
        // 声明游戏结束
        currentGameState = 1;
        // 发送游戏分数   // todo


    }


    @Override
    @OnClick({R.id.iv_pause_play, R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause_play:
                // todo 游戏暂停 逻辑
                break;
            case R.id.btn_next:
                // 前往排行榜
                ((MainActivity) getActivity()).changePage(2);
                //todo 在AppData 里面写一个操作 分数arr 的方法
//                AppDate.setString(getContext(), AppDate.SCORE_ARR_STRING, "100");   // todo fake data
                break;
            default:
                if (BuildConfig.DEBUG) Log.d("swR+GameFragment", "未处理的点击事件...");
        }
    }

    private Handler mGameHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GAME_START:
                    onGameStart(isNormalModel);
                    break;
                case MSG_GAME_REFRESH:
                    // 刷新洞, 产生随机数(地洞序号)和要播放的model(什么颜色的老鼠)后,进行操作
                    // arg1: 洞序号, arg2: 洞里 地鼠的颜色
                    onGameRefresh(msg.arg1, msg.arg2);
                    break;
                case MSG_GAME_INTERVAL:
                    // 更新当前分数, 更新剩余时间
                    ((TextView)findViewById(R.id.tv_count_down)).setText(String.valueOf(msg.arg1) + "秒");
                    ((TextView)findViewById(R.id.tv_current_score)).setText(String.valueOf(currentScore));  // todo 分数的获取方式...
                    break;
                case MSG_GAME_END:
                    onGameOver();
                    break;
                case MSG_GAME_ANI_STOP:

                    break;
                default:
                    if (BuildConfig.DEBUG) Log.d("swR+GameFragment", "未处理的点击事件...");
            }
            return false;
        }
    });

}
