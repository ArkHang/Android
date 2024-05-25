package com.example.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bean.UserBean;
import com.example.sqlite.SQLiteHelper;

public class DBUtils {
    private SQLiteDatabase db;
    private static DBUtils instance = null;

    public static DBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }

    private DBUtils(Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();
    }

    // 保存用户信息
    public void saveUserInfo(UserBean bean) {
        ContentValues cv = new ContentValues();
        cv.put("userName", bean.getUserName());
        cv.put("nickName", bean.getNickName());
        cv.put("sex", bean.getSex());
        cv.put("signature", bean.getSignature());
        db.insert(SQLiteHelper.U_USERINFO, null, cv);
    }

    // 获取用户信息
    @SuppressLint("Range")
    public UserBean getUserInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        UserBean bean = null;
        if (cursor.moveToNext()) {
            bean = new UserBean();
            bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            bean.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            bean.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            bean.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
        }
        cursor.close();
        return bean;
    }

    // 更新用户信息
    public void updateUserInfo(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_USERINFO, cv, "userName=?", new String[]{userName});
    }

    // 更新用户密码
    public void updateUserPsw(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_USERINFO, cv, "userName=?", new String[]{userName});
    }


}