package com.example.whackamole.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;

import com.example.whackamole.BuildConfig;

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
    private static SparseArray<AnimationDrawable> mPublicAniCache = new SparseArray<>(6);
    // getAnimationByName的参数, 不要改变数值的大小! 个位会存储老鼠动画是向上还是向下
    private static final int RAT_TYPE_PUBLIC = 10;

    public static final int RAT_TYPE_NORMAL = 20;
    public static final int RAT_TYPE_BEATEN = 30;

    public static final int RAT_COLOR_RED = 1;
    public static final int RAT_COLOR_ORANGE = 2;
    public static final int RAT_COLOR_BLUE = 3;


    // type+color type是两位数, color是一位数
//    public static Drawable getAnimationByName(Context c, int typeAddColor){
    public static AnimationDrawable getAnimationByName(Context c, int typeAddColor){
        return getAnimationByName(c, (typeAddColor/10)*10, typeAddColor%10);
    }
    /**
     * 获取指定类型的动画
     *
     * @param c        上下文
     * @param aniType  类型, 是normal还是beaten
     * @param aniColor 颜色, 什么颜色的老鼠
     * @return 动画
     */
//    public static Drawable getAnimationByName(Context c, int aniType, int aniColor) {
    public static AnimationDrawable getAnimationByName(Context c, int aniType, int aniColor) {
        if (mRatAnimation.get(aniType + aniColor) == null) {
            // 开始产生需要的动画
            mRatAnimation.put(aniType + aniColor,
                    productAnimationByName(c,   // 加上 公共down 动画
                            productAnimationByName(c,   // 加上 老鼠down 动画
                                    productAnimationByName(c,   // 在公共up 动画基础上加上老鼠up 动画
                                            getPublicAniCache(c, new AnimationDrawable(), aniColor, 1), // 一个向上的 颜色为aniColor的公共动画
                                            aniType, aniColor, 1)
                                    , aniType,
                                    aniColor,
                                    2
                            ),
                            RAT_TYPE_PUBLIC,
                            aniColor,
                            2
                    )
            );
            if (mRatAnimation.size() == 9) {  // 当所有的动画都已经生产完毕, 则清空缓存
                mPublicAniCache.clear();    // todo 如果没问题的话,可以让其等于 null, 让gc回收资源
            }
            if (BuildConfig.DEBUG)
                Log.d("AniUtils", "mRatAnimation.size():" + mRatAnimation.size()); //todo 确保它的大小不会超过9, 否则需要修改代码
        }

//        return mRatAnimation.get(aniType + aniColor).getConstantState().newDrawable(); // todo 每次都复制, 可能带来性能问题
        return mRatAnimation.get(aniType + aniColor);
    }

    private static AnimationDrawable getPublicAniCache(Context c, AnimationDrawable oldAniDrawable, int aniColor, int aniUpOrDown) {
        if (mPublicAniCache.get(aniColor + aniUpOrDown) == null) {
            if (oldAniDrawable == null)
                oldAniDrawable = new AnimationDrawable();
            mPublicAniCache.put(aniColor + aniUpOrDown, productAnimationByName(c, oldAniDrawable, RAT_TYPE_PUBLIC, aniColor, aniUpOrDown));
        }
        return mPublicAniCache.get(aniColor + aniUpOrDown);
    }

    /**
     * 生产 AnimationDrawable
     *
     * @param c              上下文
     * @param oldAniDrawable 原有的AnimationDrawable, 可以填 new AnimationDrawable()
     * @param aniType        类型
     * @param aniColor       动画颜色
     * @param aniUpOrDown    动画中的老鼠是向上的, 还是向下的
     * @return 动画
     */
    private static AnimationDrawable productAnimationByName(Context c, AnimationDrawable oldAniDrawable, int aniType, int aniColor, int aniUpOrDown) {
        String param = "";
        int picNum = 6; // 默认6张图
        boolean isUp = aniUpOrDown == 1;
        switch (aniType) {
            case RAT_TYPE_PUBLIC:
                param = "public_";
                picNum = 3;
                break;
            case RAT_TYPE_NORMAL:
                param = "normal_";
                break;
            case RAT_TYPE_BEATEN:
                param = "beaten_";
                break;
        }
        param += switchAniColor(aniColor);

        return getRatAnimation(c, oldAniDrawable, param, picNum, isUp);
    }

    // 添加颜色参数
    private static String switchAniColor(int aniColor) {
        switch (aniColor) {
            case RAT_COLOR_RED:
                return "red_";
            case RAT_COLOR_ORANGE:
                return "orange_";
            case RAT_COLOR_BLUE:
                return "blue_";
            default:
                return "";
        }
    }

    /**
     * @param c                  上下文
     * @param oldAniDrawable     原有的动画(可以填 new AnimationDrawable() )
     * @param param              动画名中的 类型_颜色
     * @param picNum             图片张数
     * @param isPositiveSequence 帧动画的帧序号是从小到大, 还是从大到小
     * @return 动画
     */
    private static AnimationDrawable getRatAnimation(Context c, AnimationDrawable oldAniDrawable, String param, int picNum, boolean isPositiveSequence) {
        for (int i = 0; i < picNum; i++) { // 图片资源序号从1开始到picNum
            Drawable drawable =
                    c.getDrawable(
                            c.getResources()
                                    .getIdentifier(
                                            "img_rat_" + param + ((isPositiveSequence ? i+1 : picNum - i))
                                            , "drawable", c.getPackageName()
                                    )
                    );
            if (drawable == null) {         // 如果picNum == 6, 而实际上只有3张图, 则在i==4 时, 会跳出循环
                return oldAniDrawable;
            }
            //--------------------------------------------------------------------------------------
            int duration = 60;
            if(i == 5)  // 表示, 如果当前帧以 6 结尾, 则修改该帧间隔
               duration = 1000;
            oldAniDrawable.addFrame(drawable, duration);  // 帧间隔
        }
        return oldAniDrawable;
    }
}
