package com.example.application.showModel.sqlUtils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContentInfo;

import androidx.core.app.NavUtils;

import com.example.application.showModel.bean.PingLunBean;
import com.example.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class PLSQLHelper {

    public boolean savePLInfo(Context context,Integer userID,Integer fabuID,String content,String rq){
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERID",userID);
        values.put("FABUID",fabuID);
        values.put("CONTENT",content);
        values.put("RQ",rq);
        long l = db.insert(SQLiteHelper.U_PINGLUN, null, values);
        db.close();
        return l>0?true:false;
    }

    public List<PingLunBean> queryPingLun(Context context,Integer fabuId){
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection=" FABUID=?";
        Cursor cursor = db.query(SQLiteHelper.U_PINGLUN, null, selection, new String[]{String.valueOf(fabuId)}, null, null, null);
        List<PingLunBean> results=new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                @SuppressLint("Range") PingLunBean pingLunBean = new PingLunBean(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex("USERID")),
                        cursor.getInt(cursor.getColumnIndex("FABUID")),
                        cursor.getString(cursor.getColumnIndex("CONTENT")),
                        cursor.getString(cursor.getColumnIndex("RQ")));
                results.add(pingLunBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results.size()==0?new ArrayList<>():results;
    }
}
