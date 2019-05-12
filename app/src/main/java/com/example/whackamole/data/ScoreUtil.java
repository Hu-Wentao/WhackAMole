package com.example.whackamole.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.adapter.Record;
import com.example.whackamole.utils.MyApplication;

import java.util.Arrays;

//import static com.example.whackamole.data.DbContract.ScoreEntry.COLUMN_RECORD_ACCOUNT;
//import static com.example.whackamole.data.DbContract.ScoreEntry.COLUMN_RECORD_SCORE;
import com.example.whackamole.data.DbContract.UserEntry;
import com.example.whackamole.data.DbContract.ScoreEntry;

/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/10
 */
public class ScoreUtil {
    private static ScoreDbHelper mDbHelper;

    private static synchronized ScoreDbHelper getDbHelper() {
        if (mDbHelper == null) {
            mDbHelper = new ScoreDbHelper(MyApplication.getContext());
        }
        return mDbHelper;
    }

    /**
     * 获取 当前用户的所有分数(去重)
     *
     * @return
     */
    public static Record[] getScoreArr(String currentAccount, boolean isAdmin) {
        if (BuildConfig.DEBUG) Log.d("ScoreUtil", "当前登录的账户为:" + currentAccount);

        Record[] records;
        SQLiteDatabase db = getDbHelper().getReadableDatabase();
//        final String SQL_FIND_ALL_SCORE_BY_ACCOUNT =    // 以下静态变量来自 ScoreEntity
//                "SELECT DISTINCT " + ScoreEntry.COLUMN_RECORD_SCORE +" "+
//                        ScoreEntry.COLUMN_RECORD_ACCOUNT +" "+
//                        "FROM " + ScoreEntry.TABLE_NAME +" "+
//                        "WHERE " + ScoreEntry.COLUMN_RECORD_ACCOUNT + "=?" +
//                        " ;";
//
//        Cursor cursor = db.rawQuery(SQL_FIND_ALL_SCORE_BY_ACCOUNT, new String[]{currentAccount});
        Cursor cursor;
        if (isAdmin) {
            cursor = db.query(ScoreEntry.TABLE_NAME, new String[]{ScoreEntry.COLUMN_RECORD_ACCOUNT, ScoreEntry.COLUMN_RECORD_SCORE},
                    null, null, null, null, null, null);
        } else {
            cursor = db.query(ScoreEntry.TABLE_NAME, new String[]{ScoreEntry.COLUMN_RECORD_ACCOUNT, ScoreEntry.COLUMN_RECORD_SCORE},
                    ScoreEntry.COLUMN_RECORD_ACCOUNT + "=?", new String[]{currentAccount}, null, null, null, null);
        }
        records = new Record[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            records[i++] = new Record(cursor.getString(cursor.getColumnIndex(ScoreEntry.COLUMN_RECORD_ACCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(ScoreEntry.COLUMN_RECORD_SCORE)));
        }
        cursor.close();

        if (BuildConfig.DEBUG)
            Log.d("ScoreUtil", "SQL共查询到 " + i + "条结果: " + Arrays.toString(records));
        return records;

//        String[] arr = AppData.getString(AppData.SCORE_ARR_STRING, " ").split(" +");
//
//        int[] r = new int[arr.length];
//        for (int i = 0; i < arr.length; i++) {
//            if(arr[i].equals("")){
//                continue;
//            }
//            r[i] = Integer.parseInt(arr[i]);
//        }
//        Arrays.sort(r);
//        if(r.length>3){
//            int[] t = new int[3];
//            System.arraycopy(r, r.length-3, t, 0, 3);
//            AppData.setString(AppData.SCORE_ARR_STRING, t[0]+" "+t[1]+" "+t[2]+" ");
//            return t;
//        }
//        return r;
    }

    /**
     * 将最新得分添加到当前用户的数据中
     *
     * @param score
     */
    public static void addScore(String currentAccount, int score) {
        SQLiteDatabase db = getDbHelper().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ScoreEntry.COLUMN_RECORD_ACCOUNT, currentAccount);
        cv.put(ScoreEntry.COLUMN_RECORD_SCORE, score);
        db.insert(ScoreEntry.TABLE_NAME, null, cv);
    }


}
