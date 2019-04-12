package com.example.whackamole.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.whackamole.R;

import java.util.Random;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/12
 */
public class PhraseUtils {
    private static final int[] phraseImgXY = {4, 10};   // 切割图片的宽与高1

    private static Drawable[] phraseArr;
    private static int currentIndex = 0;

    // 在游戏重启时调用
    public static void onGameRestart(){
        currentIndex = 0;
    }

    public static Drawable[] getPhraseArr() {

        Bitmap[] b = split(BitmapFactory.decodeResource(ContextImp.getContext().getResources(), R.drawable.img_phrase), phraseImgXY);
        if (phraseArr == null) {
            phraseArr = new Drawable[phraseImgXY[0] * phraseImgXY[1]];
            for (int i = 0; i < phraseArr.length; i++) {
                phraseArr[i] = new BitmapDrawable(ContextImp.getContext().getResources(), b[i]);
            }
        }
        return phraseArr;
    }

    public static int getNextIndex() {
        if (currentIndex == 40) {
            currentIndex = new Random().nextInt(10) * 4;
        }
        return currentIndex++;
    }

    public static Drawable getNextDrawable(Context c){
        return getPhraseArr()[getNextIndex()];
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

}
