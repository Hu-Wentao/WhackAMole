package com.example.whackamole.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.whackamole.data.DbContract.ScoreEntry;


/**
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/5/11
 */
public class ScoreDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "score.db";
    private static final int DB_VERSION = 1;

    public ScoreDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SCORE_TABLE =
                "CREATE TABLE "+ ScoreEntry.TABLE_NAME+" ("+
                        ScoreEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        ScoreEntry.COLUMN_RECORD_ACCOUNT+" TEXT, "+
                        ScoreEntry.COLUMN_RECORD_SCORE+" INTEGER NOT NULL "+
                        ");";

        db.execSQL(SQL_CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 更新数据库操作, 如果数据库版本更新了, 以下的做法是删除已有的数据库
        db.execSQL("DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME);
        onCreate(db);
    }
}
