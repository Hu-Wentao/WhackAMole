package com.example.whackamole.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.data.DbContract.UserEntry;

public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 4;

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " +  UserEntry.TABLE_NAME + " (" +
                         UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                         UserEntry.IS_ADMIN + " INTEGER, "+                               // 判断用户有没有管理员权限 0无, 1有
                         UserEntry.COLUMN_USER_ACCOUNT + " TEXT NOT NULL UNIQUE, " +      // 用户名 使用 account 不允许重复
                         UserEntry.COLUMN_USER_PWD + " TEXT NOT NULL" +
                        ");";
        // 执行语句
        db.execSQL(SQL_CREATE_USER_TABLE);
        // 添加管理员账户  如要修改, 请升级数据库版本=================================================
        if (BuildConfig.DEBUG) Log.d("UserDbHelper", "已创建管理员账户, 账户名: admin, 密码: admin");
        ContentValues cv = new ContentValues();
        cv.put(UserEntry.COLUMN_USER_ACCOUNT, "admin");
        cv.put(UserEntry.COLUMN_USER_PWD, "admin");
        cv.put(UserEntry.IS_ADMIN, true);
        db.insert(UserEntry.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 更新数据库操作, 如果数据库版本更新了, 以下的做法是删除已有的数据库
        db.execSQL("DROP TABLE IF EXISTS " +  UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
