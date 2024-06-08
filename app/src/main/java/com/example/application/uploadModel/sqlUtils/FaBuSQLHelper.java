package com.example.application.uploadModel.sqlUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.application.uploadModel.bean.FaBuBean;
import com.example.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FaBuSQLHelper {
    public void saveFaBuInfo(Context context,Integer userID,String title,
                                    String keys,String text,String uri){
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID",userID);
        values.put("title",title);
        values.put("keys",keys);
        values.put("text",text);
        values.put("uri",uri);
        db.insert(SQLiteHelper.U_FABUINFO,null,values);
        db.close();
    }

    public  List<FaBuBean> queryFaBuInfo(Context context,String title,String keys){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        List<String> argsList=new ArrayList<>();
        String selection="1";

        if(title!=null ||!"".equals(title)){
            selection+="AND TITLE LIEK ?";
            argsList.add(title);
        }
        if(keys!=null||!"".equals(keys)){
            String[] s = keys.split(" ");
            List<String> keyList = Arrays.asList(s);
            String joinSelection = TextUtils.join(", ", Collections.nCopies(keyList.size(), "?"));
            selection+="AND KEYS IN ("+joinSelection+")";
            argsList.addAll(keyList);

        }
        String[] selectArgs=new String[argsList.size()];
        selectArgs=argsList.toArray(selectArgs);
        Cursor cursor = db.query(SQLiteHelper.U_FABUINFO, null, selection, selectArgs, null, null, null);
        List<FaBuBean> resultLists = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                FaBuBean faBuBean = new FaBuBean(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("userid")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("keys")),
                        cursor.getString(cursor.getColumnIndexOrThrow("text")),
                        cursor.getString(cursor.getColumnIndexOrThrow("uri")));
                resultLists.add(faBuBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultLists.size()==0?null:resultLists;
    }
}
