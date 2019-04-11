package com.example.whackamole.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/11
 */
public class ImgSpliter {
    /**
     * 将图片切成 , piece *piece
     *
     * @param bitmap
     * @param pieceX 图片较窄的部分作为 X
     * @return
     */
    public static List<ImagePiece> split(Bitmap bitmap, int pieceX, int pieceY) {
        List<ImagePiece> piecesList = new ArrayList<>(pieceX * pieceY);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height) / pieceX;
        int pieceHeight = Math.max(width, height)/ pieceY;

        for (int i = 0; i < pieceY; i++) {
            for (int j = 0; j < pieceX; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.index = i + j * pieceX;

                Log.e("TAG", "ImagePiece.index" + (i + j * pieceX));
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;

                imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                piecesList.add(imagePiece);
            }
        }
        return piecesList;
    }
}
