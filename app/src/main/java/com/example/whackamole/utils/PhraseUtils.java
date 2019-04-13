package com.example.whackamole.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.whackamole.R;

import java.util.Random;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/12
 */
public class PhraseUtils {
    // 缩放图片比例
    private static final float BITMAP_SCALE = 0.5f;
    private static final int[] phraseImgXY = {4, 10};   // 切割图片的宽与高1

    private static Drawable[] phraseArr;
    private static int currentIndex = 0;

    // 在游戏重启时调用
    public static void onGameRestart() {
        currentIndex = 0;
    }

    public static Drawable[] getPhraseArr() {
        Bitmap[] b = split(BitmapFactory.decodeResource(ContextImp.getContext().getResources(), R.drawable.img_phrase4), phraseImgXY);
        if (phraseArr == null) {
            phraseArr = new Drawable[phraseImgXY[0] * phraseImgXY[1]];
            for (int i = 0; i < phraseArr.length; i++) {
//                phraseArr[i] = new BitmapDrawable(ContextImp.getContext().getResources(), zoomImg(b[i], BITMAP_SCALE));   // 没用
//                phraseArr[i] = new BitmapDrawable(ContextImp.getContext().getResources(), getImageToChange(b[i]));
                phraseArr[i] = new BitmapDrawable(ContextImp.getContext().getResources(), b[i]);
            }
        }
        return phraseArr;
    }


    public static Drawable getNextDrawable() {
        if (currentIndex == 40) {
            currentIndex = new Random().nextInt(9) * 4;
        }
        return getPhraseArr()[currentIndex++];
    }

    /**
     * 必须是先调用了 getNextDrawable() 之后, 才能得到正确的currentIndex
     * @return
     */
    public static int getCurrentIndex(){
        return currentIndex-1;
    }


    /**
     * 把白色转换成透明
     * 原理，通过遍历图片上每一个点，得到每个点的ARGB值，通过这个点的RGB的值就可以确定该点的颜色，
     * （255，255，255）白色，（０，０，０）黑色．通过判断颜色设置Ａ的值，（ａ＝０表示完全透明，ａ＝255 表示不透明）。
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap getImageToChange(Bitmap mBitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (mBitmap != null) {
            int mWidth = mBitmap.getWidth();
            int mHeight = mBitmap.getHeight();
            for (int i = 0; i < mHeight; i++) {
                for (int j = 0; j < mWidth; j++) {
                    int color = mBitmap.getPixel(j, i);
                    int g = Color.green(color);
                    int r = Color.red(color);
                    int b = Color.blue(color);
                    int a = Color.alpha(color);
                    if (g >= 250 && r >= 250 && b >= 250) {
                        a = 0;
                    }
                    color = Color.argb(a, r, g, b);
                    createBitmap.setPixel(j, i, color);
                }
            }
        }
        return createBitmap;
    }

    /**
     * 将图片切成 , piece *piece
     *
     * @param bitmap
     * @param piece  [0]作为x, [1] 作为1 图片较窄的部分作为 X
     * @return
     */
    public static Bitmap[] split(Bitmap bitmap, int... piece) {
        Bitmap[] bitmaps = new Bitmap[piece[0] * piece[1]];

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height) / piece[0];
        int pieceHeight = Math.max(width, height) / piece[1];

        int i = 0;
        for (int y = 0; y < piece[1]; y++) {
            for (int x = 0; x < piece[0]; x++) {
                int xValue = x * pieceWidth;
                int yValue = y * pieceHeight;

                bitmaps[i++] = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
            }
        }
        return bitmaps;
    }

    /**
     * 缩放Bitmap
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    private static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片      www.2cto.com
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 按比例缩放图片
     *
     * @param bm
     * @param scale
     * @return
     */
    private static Bitmap zoomImg(Bitmap bm, float scale) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
//         计算缩放比例
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片      www.2cto.com
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }
}
