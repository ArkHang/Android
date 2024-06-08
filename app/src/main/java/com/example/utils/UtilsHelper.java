package com.example.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.application.RegisterActivity;
import com.example.sqlite.SQLiteHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilsHelper {
    public static void saveUserInfo(Context context, String userName,String passWord, String nickName, String sex, String signature) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("passWord",passWord);
        values.put("nickName", nickName);
        values.put("sex", sex);
        values.put("signature", signature);
        
        db.insert(SQLiteHelper.U_USERINFO, null, values);
        db.close();
    }
    public static void setSecurity(Context context, String userName, String security) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("security", security);

        String whereClause = "userName=?";
        String[] whereArgs = new String[] { userName };
        int rowsAffected = db.update(SQLiteHelper.U_USERINFO, values, whereClause, whereArgs);
        db.close();

        if (rowsAffected == 0) {
            // 没有更新任何行，可能是因为用户名不存在
        }
    }


    public static void updatePsw(Context context, String userName, String passWord, String nickName, String sex, String signature) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("passWord", passWord);
        String whereClause = "userName=?";
        String[] whereArgs = new String[] { userName };
        db.update(SQLiteHelper.U_USERINFO, values, whereClause, whereArgs);
        db.close();
    }
    public static boolean isExistUserName(Context context, String userName) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(query, new String[]{userName});

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;
    }
    public static String readPsw(Context context, String userName) {
        SQLiteOpenHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.U_USERINFO, new String[]{"passWord"}, "userName=?", new String[]{userName}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("passWord"));
            cursor.close();
            return password;
        }
        cursor.close();
        return null; // 如果没有找到用户，返回 null
    }
    public static String readSecurity(Context context, String userName) {
        SQLiteOpenHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(SQLiteHelper.U_USERINFO, new String[]{"security"}, "userName=?", new String[]{userName}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String security = cursor.getString(cursor.getColumnIndex("security"));
            cursor.close();
            return security;
        }
        cursor.close();
        return null; // 如果没有找到用户，返回 null
    }

    public static void saveLoginStatus(Context context, boolean status, String userName) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.U_USERINFO, new String[]{"_id"}, "userName=?", new String[]{userName}, null, null, null);
        editor.putInt("id",cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", userName);
        editor.commit();
    }

    public static Integer readUserId(Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        return sp.getInt("id",0);
    }
    public static boolean readLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }


    public static void clearLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("loginUserName", "");
        editor.commit();
    }

    public static String readLoginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        return sp.getString("loginUserName", "");
    }

    public static String md5(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 should be supported?", e);
        }
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }



}
