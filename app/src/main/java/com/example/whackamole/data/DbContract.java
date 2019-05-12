package com.example.whackamole.data;

import android.provider.BaseColumns;

public class DbContract {
    // 用户信息
    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USER_ACCOUNT = "userName";
        public static final String COLUMN_USER_PWD = "userPwd";
        public static final String IS_ADMIN = "isAdministrator";
    }

    // 分数表
    public static final class ScoreEntry implements BaseColumns{
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_RECORD_SCORE = "recordScore";
        public static final String COLUMN_RECORD_ACCOUNT = "recordUser";
    }

}
