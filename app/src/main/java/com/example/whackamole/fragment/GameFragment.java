package com.example.whackamole.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.whackamole.data.AppData;
import com.example.whackamole.data.Score;
import com.example.whackamole.game.GameThread;
import com.example.whackamole.game.GameTimer;
import com.example.whackamole.utils.AniUtils;
import com.example.whackamole.utils.PhraseUtils;
import com.example.whackamole.utils.VibratorUtils;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/7
 */
public class GameFragment extends BaseFragment implements View.OnClickListener {
    // 老鼠洞 ImageView list
    private static SparseArray<ImageView> sRateHoleArray = new SparseArray<>(12);
    // 正在播放动画的洞的序号
    private static HashSet<Integer> sOccupyHoleSet = new HashSet<>(12);
    // 当前游戏模式
    public static boolean isNormalModel;
    // 当前游戏状态  -1 未开始(无意义), 0 游戏中, 1 暂停 2结束
    public static byte sCurrentGameState = -1;
    // 当前游戏得分
    private int currentScore;
    // 普通模式下, 当前行进到的关卡数
    public static int currentLevel = 0;


    // 游戏逻辑线程
    private GameThread mGameThread;
    // 动画数组       3: 3种颜色的地鼠, 12: 12个洞
    private static AnimationDrawable[][] sRatAnimationArr = new AnimationDrawable[3][12];
    // 被击打动画数组 3: 3种颜色的地鼠, 12: 12个地洞
    private static AnimationDrawable[][] sHitRatAnimationArr = new AnimationDrawable[3][12];

    // 成语图 当前索引 // 使用 PhraseUtils.getIndex() 获取索引
    // Handler 的what类型
    public static final int MSG_WHAT_START = 0;
    public static final int MSG_WHAT_NEXT_LEVEL = 7;    // 普通模式,进入下一关
    public static final int MSG_WHAT_REFRESH = 1;
    public static final int MSG_WHAT_INTERVAL = 2;
    public static final int MSG_WHAT_END = 3;
    public static final int MSG_WHAT_ANIM_STOP = 4;
    public static final int MSG_WHAT_HANDLE_PHRASE = 5;
    public static final int MSG_WHAT_HANDLE_sOccupyHoleSet = 6;
    // Handler arg1, 游戏剩余时间...
    // Handler arg2, 选择模型类型 -> 参见 AniUtils -> RAT_COLOR_*
    //===============挑战模式相关================
    // 成语图 数组
    private static Drawable sPhraseArr[];
    // 挑战模式下, 剩余的机会
    private int remainChance = 3;
    // 挑战模式下 上一个被点击的Phrase图index
    private int savedPhraseIndex = 0;

    // 延后显示成语图/ 提前结束显示成语图 的时长
    private static final int sGapeTime = 200;
    // =============控件


    @Override   // 返回事件
    public boolean needHandleBackPress() {
        return false;
    }

    @Override
    protected void doInit() {
        isNormalModel = AppData.getBoolean(AppData.IS_NORMAL_GAME_MODEL, true);
        if(sCurrentGameState == -1){
            findViewById(R.id.constrainGame).setBackgroundResource(R.drawable.ic_game_bg_0);
        }
        if (sCurrentGameState == 2) { //如果游戏已结束
            if (isNormalModel) { // 普通模式

            } else {    // 挑战模式
                if (mGameThread != null)
                    mGameThread.stopGame();
                PhraseUtils.onGameRestart();
                // 复位当前分数
                currentScore = 0;
                // 复位剩余机会
                remainChance = 3;
                // 复位上一个被点击的Phrase图索引
                savedPhraseIndex = 0;
            }

            // 移除上局的游戏结果提示
            findViewById(R.id.constrain_game_result).setVisibility(View.GONE);
            // 显示 右上角的 分数
            findViewById(R.id.tv_current_score).setVisibility(View.VISIBLE);

            // 复位所有的洞View
            for (int i = 0; i < sRateHoleArray.size(); i++) {
                sRateHoleArray.get(i).setImageResource(0);
                sRateHoleArray.get(i).setBackgroundResource(R.drawable.img_rat_public_0);
            }
        } else if (sCurrentGameState == 1) {   // 如果当前为暂停中
            if (currentLevel >= 3) {   // 如果 普通模式结束
                // 复位当前分数
                currentScore = 0;
                // 复位currentLevel
                currentLevel = 0;
//                复位游戏背景
                findViewById(R.id.constrainGame).setBackgroundResource(R.drawable.ic_game_bg_0);
            } else { // 如果是普通模式进入下一关
                // 移除上局的游戏结果提示
                findViewById(R.id.constrain_game_result).setVisibility(View.GONE);
                // 显示 右上角的 分数
                findViewById(R.id.tv_current_score).setVisibility(View.VISIBLE);
            }
        }
//            sCurrentGameState = 0;  // 无需在此处声明游戏开始, 在Handler的onGameStart处更合适

        {// 初始化一些信息 - 不要改动顺序
            // 动态添加地洞 // 考虑根据情况同时添加TextView
            addRatHole();
            // 初始化每个地洞对应的动画
            initRatAnimation(isNormalModel);
            if (sPhraseArr == null && !isNormalModel) {
                sPhraseArr = PhraseUtils.getPhraseArr();
            }
        }
        {   // 开始游戏逻辑
            // onGameGuide() // todo  建立一个游戏指引(游戏开始前倒计时之类)
            // 倒计时结束, 发送一个Message, 开始游戏
            mGameHandler.sendEmptyMessage(MSG_WHAT_START);
        }
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_game_main;
    }

    private void initRatAnimation(boolean isNormalModel) {
        // 将动画存放到各个数组中 // mRatAni..数组和 mHitRatAni..数组长度是相同的, 所以放在同一个循环中
        if (sRatAnimationArr[0][0] != null && sHitRatAnimationArr[0][0] != null) {
            return;
        }
        for (int i = 0; i < sRatAnimationArr.length; i++) {
            if (!isNormalModel && i != 1) {
                continue;
            }
            for (int j = 0; j < sRatAnimationArr[i].length; j++) {
                sRatAnimationArr[i][j] = (AnimationDrawable) AniUtils.getAnimationByName(AniUtils.RAT_TYPE_NORMAL, i).getConstantState().newDrawable();
                sHitRatAnimationArr[i][j] = (AnimationDrawable) AniUtils.getAnimationByName(AniUtils.RAT_TYPE_BEATEN, i).getConstantState().newDrawable();
            }
        }
    }

    /**
     * 动态添加地洞, 为 地洞view 配置tag(0 - 11)
     * 使用 sRateHoleArray 存储
     */
    private void addRatHole() {
        GridLayout layout = findViewById(R.id.gridlayout_rat_hole);
        for (int i = 0; i < 12; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setBackgroundResource(R.drawable.img_rat_public_0);   // 设置默认图片
            iv.setTag(R.id.hole_index, i);   // 为hole 添加 tag
            // 添加点击事件监听器
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHoleClick(v);
                }
            });
            // 将控件添加到list
            sRateHoleArray.append(i, iv);

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
    private void onHoleClick(final View v) {
        // 判断是否 处于游戏中 // 判断 view是否有效
        if (sCurrentGameState != 0 || v.getTag(R.id.hole_current_color) == null || v.getTag(R.id.hole_index) == null) {
            return;
        }
        if (!isNormalModel && v.getTag(R.id.hole_current_phrase_index) == null) { // 挑战模式下, 如果该控件没有phrase_index,则退出
            return;
        }
        final int aniColor = (int) v.getTag(R.id.hole_current_color);
        final int holeId = (int) v.getTag(R.id.hole_index);
        //-----------------------------震动-------------------------------------------------------------
        if (AppData.getBoolean(AppData.IS_ALLOW_SHAKE, true)) {
            VibratorUtils.shake(150);
        }

        //-----------------------------动画-------------------------------------------------------------
        // 获取该洞的所属的动画
        AnimationDrawable aniDrawable = sRatAnimationArr[aniColor][holeId];
        // 如果 被点击的洞不在播放动画, 则退出方法 (用户点击了没有播放动画的洞)
        if (aniDrawable == null || !aniDrawable.isRunning()) {
            return;
        }
        // 获取该洞
        final ImageView view = sRateHoleArray.get(holeId);
        // 删除洞的ImageDrawable
        view.setImageDrawable(null);

        // 先切换到击打动画, 并播放
        AnimationDrawable hitDrawable = sHitRatAnimationArr[aniColor][holeId];
        view.setBackground(hitDrawable);  // 设置被击打动画
        hitDrawable.setVisible(true, false);    // 似乎没什么用
        hitDrawable.start();

        // 然后处理原来的动画
        mGameHandler.removeMessages(MSG_WHAT_ANIM_STOP, aniDrawable);
        aniDrawable.setVisible(false, false);
        aniDrawable.stop();
        aniDrawable.selectDrawable(0);  // 将动画设置到第一张(初始状态)

        // 在打击动画播放完毕后, 结束动画
        Message m = Message.obtain();
        m.what = MSG_WHAT_ANIM_STOP;
        m.arg1 = holeId;
        m.obj = hitDrawable;
        mGameHandler.sendMessageDelayed(m, AniUtils.getAniDuration(true));

        // 处理动画的播放, 并在mOccupyHoleSet 中标记正在播放动画的洞
        sOccupyHoleSet.add(holeId);
        Message m1 = Message.obtain();
        m1.what = MSG_WHAT_HANDLE_sOccupyHoleSet;
        m1.arg1 = holeId;
        mGameHandler.sendMessageDelayed(m1, 1500);

        // 处理得分情况, // 可以在此处同时播放击打音效
        String showScore = "";
        if (!isNormalModel) {
            // 判分
            // 获取被点击的洞的phrase索引
            int phraseIndex = (int) v.getTag(R.id.hole_current_phrase_index);
            // 如果该索引与保存的索引不连续
            if (++savedPhraseIndex != phraseIndex) {
                currentScore -= (phraseIndex / 4) * 20; // 20是黄老鼠的分值
                remainChance--;
            }

            if (remainChance < 0) {
                onGameOver();
            }
            showScore = "还有" + remainChance + "次机会 ";
        }
        switch (aniColor) {
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
        showScore += (String.valueOf(currentScore) + " 分");
        // 更新分数
        ((TextView) findViewById(R.id.tv_current_score)).setText(showScore);
    }

    private void onGameStart() {
        // 声明游戏开始
        sCurrentGameState = 0;   // 0 : 游戏运行中
        // 初始化游戏数据 在 doInit()

        // 开启线程
        mGameThread = new GameThread(mGameHandler, sOccupyHoleSet);
        mGameThread.setAnimationArr(sRatAnimationArr);

        // 开启线轮询
        mGameThread.start();
        mGameThread.startGame();
    }


    // 0 show, 1 hide
    private void processPhraseDisplay(int showOrHide, Object holeView) {
        if (showOrHide == 0) {
            ((ImageView) holeView).setImageDrawable(PhraseUtils.getNextDrawable());
            ((ImageView) holeView).setTag(R.id.hole_current_phrase_index, PhraseUtils.getCurrentIndex()); //为洞配置成语图Index
        } else {
            ((ImageView) holeView).setImageDrawable(null);
        }
    }

    private void onGameRefresh(int hole, int aniColor) {
        // 获取该洞的view
        final ImageView view = sRateHoleArray.get(hole);
        if (!isNormalModel) {// 如果是挑战模式, 则延时 sGapeTime展示成语图
            Message mShow = Message.obtain();
            mShow.what = MSG_WHAT_HANDLE_PHRASE;
            mShow.arg1 = 0;
            mShow.obj = view;
            mGameHandler.sendMessageDelayed(mShow, sGapeTime);

            Message mHide = Message.obtain();
            mHide.what = MSG_WHAT_HANDLE_PHRASE;
            mHide.arg1 = 1;
            mHide.obj = view;
            mGameHandler.sendMessageDelayed(mHide, AniUtils.getAniDuration(false) - sGapeTime);
        }
        //------------------------------------------------------------------------------------------
        // 要播放的动画
        AnimationDrawable targetAniDrawable = sRatAnimationArr[aniColor][hole];
        // 清空背景,并设置动画
        view.setBackgroundResource(0);
        view.setBackground(targetAniDrawable);
        view.setTag(R.id.hole_current_color, aniColor);        // 设置tag, 标记这个view播放的动画的颜色
        // 设置AnimationDrawable可见性, 然后播放动画
        targetAniDrawable.setVisible(true, true);
        targetAniDrawable.start();

        //------------------------------------------------------------------------------------------
        // 发送一个消息, 表示该动画的播放时长, 设置在动画时长结束后,停止播放动画
        Message obtain = Message.obtain();
        obtain.what = MSG_WHAT_ANIM_STOP;
        obtain.arg1 = hole;     // 有点必要
        obtain.obj = targetAniDrawable;
        mGameHandler.sendMessageDelayed(obtain, AniUtils.getAniDuration(false));
    }

    private void onGameOver() {
        // 显示 下一步 面板
        findViewById(R.id.constrain_game_result).setVisibility(View.VISIBLE);
        // 隐藏 右上角的 分数
        findViewById(R.id.tv_current_score).setVisibility(View.INVISIBLE);
        // 设置面板上的 分数
        ((TextView) findViewById(R.id.tv_show_score)).setText(currentScore + " 分");
        // 执行游戏结束
        mGameThread.stopGame();
        // 声明游戏结束
        sCurrentGameState = 2;
        if (isNormalModel && currentLevel < 3) {    // 处于普通模式 并且当前关卡为 {0,1,2}
            sCurrentGameState = 1;
        }

    }

    private void notifyAniStop(Message msg) {
        // arg1: hole序号,  obj 目标Drawable
        ((AnimationDrawable) msg.obj).stop();
        ((AnimationDrawable) msg.obj).selectDrawable(0); // 复位动画, 在下次使用时保证显示正常
        sRateHoleArray.get(msg.arg1).setBackgroundResource(R.drawable.img_rat_public_0);    // 复位
    }


    @Override
    @OnClick({R.id.iv_pause_play, R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause_play:
                setPauseOrPlay(sCurrentGameState);
                break;
            case R.id.btn_next:
                currentLevel++;
                if (isNormalModel && currentLevel < 3) {   // 前往下一关
                    // 切换背景
                    ((TextView)findViewById(R.id.btn_next)).setText("下一关");
                    findViewById(R.id.constrainGame).setBackgroundResource(this.getResources().getIdentifier("ic_game_bg_" + currentLevel, "drawable", getContext().getPackageName()));
                    doInit();
                } else {    // 游戏结算
                    ((TextView)findViewById(R.id.btn_next)).setText("下一步");
                    // 复位参数
                    sCurrentGameState = 2;
                    currentLevel = 0;
                    Score.addScore(String.valueOf(currentScore));
                    currentScore = 0;
                    ((TextView) findViewById(R.id.tv_current_score)).setText("0 分");
                    findViewById(R.id.constrainGame).setBackgroundResource(R.drawable.ic_game_bg_0);
                    // 销毁当前Fragment, 前往排行榜
                    GameFragment.this.onDestroy();
                    ((MainActivity) getActivity()).changePage(2);
                }
                break;
            default:
                if (BuildConfig.DEBUG) Log.d("swR+GameFragment", "未处理的点击事件...");
        }
    }

    /**
     * @param gameState true: 显示 "游戏暂停" 提示
     */
    private void setPauseOrPlay(int gameState) {
        if (gameState == 0) {
            sCurrentGameState = 1;   //暂停
            GameTimer.pause();
            ((ImageView) findViewById(R.id.iv_pause_play)).setImageResource(android.R.drawable.ic_media_play);
            findViewById(R.id.tv_pause_tips).setVisibility(View.VISIBLE);
        } else if (gameState == 1) {
            sCurrentGameState = 0;   // 游戏中
            GameTimer.play(mGameHandler);
            ((ImageView) findViewById(R.id.iv_pause_play)).setImageResource(android.R.drawable.ic_media_pause);
            findViewById(R.id.tv_pause_tips).setVisibility(View.GONE);
            // 显示 右上角的 分数
            findViewById(R.id.tv_current_score).setVisibility(View.VISIBLE);
        }
    }

    private Handler mGameHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_START:
                    onGameStart();
                    break;
                case MSG_WHAT_REFRESH:
                    // 刷新洞, 产生随机数(地洞序号)和要播放的model(什么颜色的老鼠)后,进行操作
                    // arg1: 洞序号, arg2: 洞里 地鼠的颜色
                    onGameRefresh(msg.arg1, msg.arg2);
                    break;
                case MSG_WHAT_INTERVAL:
                    // 更新当前分数, 更新剩余时间
                    // arg1 : 剩余秒数, arg2: 无意义
                    ((TextView) findViewById(R.id.tv_count_down)).setText(String.valueOf(msg.arg1) + "秒");
                    break;
                case MSG_WHAT_END:
                    onGameOver();
                    break;
                case MSG_WHAT_ANIM_STOP:    // 停止动画
                    notifyAniStop(msg);
                    break;
                case MSG_WHAT_HANDLE_PHRASE:  // 挑战模式下, 处理成语图的延迟出现于提前消失;
                    processPhraseDisplay(msg.arg1, msg.obj);
                    break;
                case MSG_WHAT_HANDLE_sOccupyHoleSet:
                    // arg1: 要从Set中移除的ViewID的HoleId
                    sOccupyHoleSet.remove(msg.arg1);
                    break;
//                case MSG_WHAT_NEXT_LEVEL: // 弃用
//                    onGameStart(true);
//                    break;
                default:
                    if (BuildConfig.DEBUG) Log.d("swR+GameFragment", "未处理的点击事件...");
            }
            return false;
        }
    });

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sCurrentGameState == 2) {
            doInit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setPauseOrPlay(sCurrentGameState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGameThread != null) {
            mGameThread.release();
        }
        mGameHandler.removeCallbacksAndMessages(null);
    }
}
