package com.example.application.uploadModel.sqlUtils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.application.uploadModel.bean.FaBuBean;
import com.example.sqlite.SQLiteHelper;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FaBuSQLHelper {
    public boolean saveFaBuInfo(Context context,Integer userID,String title,
                                    String keys,String text,String uri){
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID",userID);
        values.put("title",title);
        values.put("keys",keys);
        values.put("atext",text);
        values.put("uri",uri);

        long l = db.insert(SQLiteHelper.U_FABUINFO, null, values);
        db.close();
        return l>0?true:false;
    }

    public  List<FaBuBean> queryFaBuInfo(Context context,String title,String keys){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        List<String> argsList=new ArrayList<>();
        String selection="1 ";

        if(title!=null &&!"".equals(title)){
            selection += "AND TITLE LIKE ?";
            argsList.add("%"+title+"%");
        }
        if(keys!=null&&!"".equals(keys)){
            String[] s = keys.split(" ");
            List<String> keyList = Arrays.asList(s);
            for(int i=0;i<keyList.size();i++){
                selection+="AND KEYS LIKE ? ";
                argsList.add("%"+keyList.get(i)+"%");
            }
//            String joinSelection = TextUtils.join(", ", Collections.nCopies(keyList.size(), "?"));
//            selection+="AND KEYS IN ("+joinSelection+")";
//            argsList.addAll(keyList);

        }
//        selection+="AND KEYS IN (?)";
//        argsList.add("学习 鬼畜");
        String[] selectArgs=new String[argsList.size()];
        selectArgs=argsList.toArray(selectArgs);
        Cursor cursor = db.query(SQLiteHelper.U_FABUINFO, null, selection, selectArgs, null, null, null);
        List<FaBuBean> resultLists = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                FaBuBean faBuBean = new FaBuBean(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("USERID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TITLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("KEYS")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ATEXT")),
                        cursor.getString(cursor.getColumnIndexOrThrow("URI")));
                resultLists.add(faBuBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultLists.size()==0?new ArrayList<>():resultLists;
    }

    @SuppressLint("Range")
    public String getUserName(Context context,Integer userid){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        String userName="";
        Cursor cursor = db.query(SQLiteHelper.U_USERINFO, new String[]{"userName"}, "_ID=? ", new String[]{userid + ""}, null, null, null);
        if(cursor!=null && cursor.moveToFirst()){
             userName=cursor.getString(cursor.getColumnIndex("userName"));
        }
        return userName;

    }
    
    public FaBuBean findFaBuInfoByID(Context context,Integer id){
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        
        String selection=" _id=?";
        FaBuBean faBuBean=null;
        Cursor cursor = db.query(SQLiteHelper.U_FABUINFO, null, selection, new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.moveToFirst()){
            faBuBean = new FaBuBean(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("USERID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("TITLE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("KEYS")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ATEXT")),
                    cursor.getString(cursor.getColumnIndexOrThrow("URI")));
        }

        return faBuBean;
    }
}
