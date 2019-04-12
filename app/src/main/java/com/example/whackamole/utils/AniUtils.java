package com.example.whackamole.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;


/**
 * 获取动画
 *
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/9
 */
public class AniUtils {
    // 动画存储, key 的值通过: RAT_TYPE_ * 10 + RAT_COLOR_ 得到
    private static SparseArray<AnimationDrawable> mRatAnimation = new SparseArray<>(6);
    // 公共动画部分的缓存, KEY 通过:
    private static SparseArray<AnimationDrawable> mPublicAniArr = new SparseArray<>(6);
    // getAnimationByName的参数, 不要改变数值的大小! 个位会存储老鼠动画是向上还是向下
    private static final int RAT_TYPE_PUBLIC = 10;  // 不要改变参数值, TYPE 参数值关联 getAniDuration(), 等方法
    public static final int RAT_TYPE_NORMAL = 20;
    public static final int RAT_TYPE_BEATEN = 30;

    public static final int RAT_COLOR_RED = 0;
    public static final int RAT_COLOR_ORANGE = 1;
    public static final int RAT_COLOR_BLUE = 2;

    // 动画的播放时长: Normal 和 Beaten
    private static int[] aniTime = new int[2];

    /**
     * 获取动画的播放时长
     *
     * @param isBeaten
     * @return
     */
    public static long getAniDuration(boolean isBeaten) {
        int index = isBeaten ? 1 : 0;   // 此处是 -2, Normal为0, beaten为
        if (aniTime[index] == 0) {
            AnimationDrawable drawable = getAnimationByName(isBeaten ? RAT_TYPE_BEATEN : RAT_TYPE_NORMAL, 1);
            int t = 0;
            for (int i = 0; i < drawable.getNumberOfFrames(); i++) {
                t += drawable.getDuration(i);
            }
            aniTime[index] = t;
        }
        return aniTime[index];
    }

    /**
     * 获取指定类型的动画
     *
     * @param aniType  类型, 是normal还是beaten
     * @param aniColor 颜色, 什么颜色的老鼠
     * @return 动画
     */
    public static AnimationDrawable getAnimationByName(int aniType, int aniColor) {
        Context c = ContextImp.getContext();
        if (mRatAnimation.get(aniType + aniColor) == null) {
            // 判断是什么Type
            mRatAnimation.put(aniType + aniColor,    // 开始产生需要的动画
                    (aniType == RAT_TYPE_NORMAL) ?  // 根据类型进行拼装
                            (AnimationDrawable) c.getDrawable(c.getResources().getIdentifier("img_rat_normal_" + switchAniColor(aniColor), "drawable", c.getPackageName()))
//                            productAnimationByName(      // 加上 公共down 动画
//                                    RAT_TYPE_PUBLIC, aniColor, 2,
//                                    productAnimationByName(  // 加上 老鼠down 动画
//                                            aniType, aniColor, 2,
//                                            productAnimationByName(  // 在公共up 动画基础上加上老鼠up 动画
//                                                    aniType, aniColor, 1,
//                                                    prodPubAnimation(aniColor, 1, new AnimationDrawable())// 一个向上的 颜色为aniColor的公共动画
//                                            )
//                                    )
//                            )
                            :
                            (AnimationDrawable) c.getDrawable(c.getResources().getIdentifier("img_rat_beaten_" + switchAniColor(aniColor), "drawable", c.getPackageName()))


            );
//            if (mRatAnimation.size() == 9) {  // 当所有的动画都已经生产完毕, 则清空缓存
//                mPublicAniArr.clear();
//            }
        }
        return mRatAnimation.get(aniType + aniColor);
    }


    private static AnimationDrawable prodPubAnimation(int aniColor, int aniUpOrDown, AnimationDrawable oldAniDrawable) {
        if (mPublicAniArr.get(aniColor + aniUpOrDown) == null) {
            if (oldAniDrawable == null)
                oldAniDrawable = new AnimationDrawable();
            mPublicAniArr.put(aniColor + aniUpOrDown, productAnimationByName(RAT_TYPE_PUBLIC, aniColor, aniUpOrDown, oldAniDrawable));
        }
        return mPublicAniArr.get(aniColor + aniUpOrDown);
    }

    /**
     * 生产 AnimationDrawable
     *
     * @param oldAniDrawable 原有的AnimationDrawable, 可以填 new AnimationDrawable()
     * @param aniType        类型
     * @param aniColor       动画颜色
     * @param aniUpOrDown    动画中的老鼠是向上的, 还是向下的
     * @return 动画
     */
    private static AnimationDrawable productAnimationByName(int aniType, int aniColor, int aniUpOrDown, AnimationDrawable oldAniDrawable) {
        String param = "";
        int picNum = 6; // 默认6张图
        boolean isUp = aniUpOrDown == 1;
        switch (aniType) {
            case RAT_TYPE_PUBLIC:
                param = "public";
                picNum = 3;
                break;
            case RAT_TYPE_NORMAL:
                param = "normal";
                break;
            case RAT_TYPE_BEATEN:
                param = "beaten";
                break;
        }
        return getRatAnimation(oldAniDrawable, param, switchAniColor(aniColor), picNum, isUp);
    }

    // 添加颜色参数
    private static String switchAniColor(int aniColor) {
        switch (aniColor) {
            case RAT_COLOR_RED:
                return "red";
            case RAT_COLOR_ORANGE:
                return "orange";
            case RAT_COLOR_BLUE:
                return "blue";
            default:
                return "";
        }
    }

    /**
     * @param oldAniDrawable     原有的动画(可以填 new AnimationDrawable() )
     *                           //     * @param param              动画名中的 类型_颜色
     * @param picNum             图片张数
     * @param isPositiveSequence 帧动画的帧序号是从小到大, 还是从大到小
     * @return 动画
     */
    private static AnimationDrawable getRatAnimation(AnimationDrawable oldAniDrawable, String aniType, String aniColor, int picNum, boolean isPositiveSequence) {
        Context c = ContextImp.getContext();
        for (int i = 0; i < picNum; i++) { // 图片资源序号从1开始到picNum
            Drawable drawable =
                    c.getDrawable(c.getResources().getIdentifier(
                            "img_rat_" + aniType + "_" + aniColor + "_" + ((isPositiveSequence ? i + 1 : picNum - i))
                            , "drawable"
                            , c.getPackageName()
                            )
                    );
            if (drawable == null) {         // 如果picNum == 6, 而实际上只有3张图, 则在i==4 时, 会跳出循环
                return oldAniDrawable;
            }
            //--------------------------------------------------------------------------------------

            oldAniDrawable.addFrame(drawable,
                    setDuration(i, isPositiveSequence, aniType));  // 帧间隔
        }
        return oldAniDrawable;
    }

    //配置动画帧间隔
    private static int setDuration(int loopIndex, boolean isPositiveSequence, String aniType) {
        // 默认间隔
        int duration = 25;

        // 如果是 被击败 的动画, 则间隔增加_倍
//        if ("beaten".equals(aniType) || ("public").equals(aniType) && !isPositiveSequence) {
//            duration *= 1.5;
//        } else {
        if (loopIndex == 5 && isPositiveSequence) {  // 表示, 如果当前帧以 6 结尾, 则修改该帧间隔
            duration = 1500;
        }
        return duration;
    }
}
