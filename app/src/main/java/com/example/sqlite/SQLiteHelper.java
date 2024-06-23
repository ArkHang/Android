package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "onlinecourse.db"; // 数据库名称
    public static int DB_VERSION = 10; // 增加版本号
    public static final String U_USERINFO = "userinfo"; // 表名称

    public static final String U_FABUINFO="fabuinfo";

    public static final String U_NOTIFICATION="notification";
    public static final String U_PINGLUN="pingluninfo";
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

        if(oldVersion<4){
            String CREATE_TABLE_PL="CREATE TABLE "+U_PINGLUN+"("
                    +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"USERID INTEGER,"
                    +"FABUID INTEGER,"
                    +"CONTENT VARCHAR,"
                    +"FOREIGN KEY (USERID) REFERENCES "+U_USERINFO+" (_id),"
                    +"FOREIGN KEY (FABUID) REFERENCES "+U_FABUINFO+" (_id)"
                    +")";
            db.execSQL(CREATE_TABLE_PL);

        }

        if (oldVersion < 5) {
            // 仅当从版本 1 升级到版本 2 时执行

            db.execSQL("ALTER TABLE " + U_PINGLUN + " ADD COLUMN RQ VARCHAR");
        }
        if (oldVersion<10){
            String CREATE_TABLE_NOTIFICATION="CREATE TABLE "+U_NOTIFICATION+"("
                    +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"AUTHORID INTEGER,"
                    +"CONTENT VARCHAR,"
                    +"RQ VARCHAR,"
                    +"FOREIGN KEY (AUTHORID) REFERENCES "+U_USERINFO+" (_id)"
                    +")";
            db.execSQL(CREATE_TABLE_NOTIFICATION);

        }
    }
}
