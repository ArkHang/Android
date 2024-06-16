package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "onlinecourse.db"; // 数据库名称
    public static int DB_VERSION = 3; // 增加版本号
    public static final String U_USERINFO = "userinfo"; // 表名称

    public static final String U_FABUINFO="fabuinfo";
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USERINFO = "CREATE TABLE " + U_USERINFO + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userName VARCHAR, "
                + "passWord VARCHAR,"
                + "nickName VARCHAR, "
                + "sex VARCHAR, "
                + "signature VARCHAR,"  // 保持之前字段不变
                + "security VARCHAR"    // 添加新的列
                + ")";
        db.execSQL(CREATE_TABLE_USERINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 仅当从版本 1 升级到版本 2 时执行
            db.execSQL("ALTER TABLE " + U_USERINFO + " ADD COLUMN security VARCHAR");
            DB_VERSION++;
        }
        // 为未来可能的版本升级做准备
        if(oldVersion<3){
            String CREATE_TABLE_FABUINFO="CREATE TABLE "+U_FABUINFO+"("
                    +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"USERID INTEGER,"
                    +"TITLE VARCHAR,"
                    +"KEYS  VARCHAR,"
                    +"ATEXT VARCHAR,"
                    +"URI VARCHAR,"
                    +"FOREIGN KEY (USERID) REFERENCES "+U_USERINFO+" (_id)"
                    +")";
            db.execSQL(CREATE_TABLE_FABUINFO);
        }
    }
}
