package com.example.application.uploadModel.application;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import static com.example.application.showModel.content.ServerContent.SERVER_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.uploadModel.bean.FaBuBean;
import com.example.application.uploadModel.intentutils.ActivityResultCallback;
import com.example.application.uploadModel.sqlUtils.FaBuSQLHelper;
import com.example.sqlite.SQLiteHelper;
import com.example.utils.UtilsHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpLoadView extends AppCompatActivity implements View.OnClickListener{

    private static Activity mContext;


    private static final int REQUEST_CODE_VIDEO = 1;
    private View mCurrentView;
    
    private boolean isLogin = false;

    private ToggleButton[] tbs=new ToggleButton[8];

    private EditText ed_title,et_js;
    private TextView tv_key;
    private LayoutInflater layoutInflater;

    private Button uploadbtn,commitBtn;

    private boolean[] flags=new boolean[8];

    private ActivityResultCallback callback;

    private Uri uri;

    private FaBuSQLHelper sqlHelper=new FaBuSQLHelper();
    private ImageView iv;
    public UpLoadView(Activity context,ActivityResultCallback call){
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        mContext = context;

        this.callback=call;
        layoutInflater=LayoutInflater.from(mContext);
    }

    private void initView() {
        mCurrentView = layoutInflater.inflate(R.layout.activity_upload, null);
        tbs[0] = mCurrentView.findViewById(R.id.tb_btn1);
        tbs[1] = mCurrentView.findViewById(R.id.tb_btn2);
        tbs[2] = mCurrentView.findViewById(R.id.tb_btn3);
        tbs[3] = mCurrentView.findViewById(R.id.tb_btn4);
        tbs[4] = mCurrentView.findViewById(R.id.tb_btn5);
        tbs[5] = mCurrentView.findViewById(R.id.tb_btn6);
        tbs[6] = mCurrentView.findViewById(R.id.tb_btn7);
        tbs[7] = mCurrentView.findViewById(R.id.tb_btn8);
        ed_title=mCurrentView.findViewById(R.id.video_name);
        tv_key = mCurrentView.findViewById(R.id.tv_keySelected);
        et_js=mCurrentView.findViewById(R.id.et_js);
        uploadbtn=mCurrentView.findViewById(R.id.upload_vedio);
        iv=mCurrentView.findViewById(R.id.iv);
        commitBtn=mCurrentView.findViewById(R.id.commit);
        setListener();
        mCurrentView.setVisibility(View.VISIBLE);
//        setLoginParams(isLogin);
    }

    private void setListener(){
        for (int i=0;i<tbs.length;i++){
            tbs[i].setOnClickListener(this);
        }
        uploadbtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

    }
    public View getView() {
        isLogin = UtilsHelper.readLoginStatus(mContext);

        if (mCurrentView == null) {
            initView();
        }
        return mCurrentView;
    }

    public void showView() {
        if (mCurrentView == null) {
            initView();
        }
        mCurrentView.setVisibility(View.VISIBLE);


    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        for (int i = 0; i < tbs.length; i++) {
            if(id==tbs[i].getId()){
                showKey(tbs[i],i);
                break;
            }
        }
        if (id==R.id.upload_vedio){
            selectVideo();
        }
        if (id==R.id.commit){
            uploadInfo();
        }
    }

    private void uploadInfo() {
        String title=ed_title.getText().toString();
        String keys = tv_key.getText().toString();
        String text = et_js.getText().toString();
        String str_uri=uri.toString();
        long currentTimeMillis = System.currentTimeMillis();
        int id= (int) (currentTimeMillis / 1000);
        try{
            boolean b = sqlHelper.saveFaBuInfo(mContext, UtilsHelper.readUserId(mContext), title, keys, text, str_uri,id);
            if (b){
                ed_title.setText("");
                tv_key.setText("");
                et_js.setText("");
                uri=null;
                iv.setImageURI(null);
                for (int j=0;j< flags.length;j++){
                    flags[j]=false;
                }

                FaBuBean faBuBean = sqlHelper.findFaBuInfoByID(mContext, id);
                Gson gson = new Gson();
                String json = gson.toJson(faBuBean);
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
                Request build = new Request.Builder()
                        .url(SERVER_URL+"insert")
                        .post(body)
                        .build();

                client.newCall(build).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String s = response.body().toString();
//                Toast.makeText(UpLoadView.this,s,Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(mContext,"发布成功",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(mContext,"发布失败",Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void selectVideo() {
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent1.setType("*/*");
        mContext.startActivityForResult(intent1, REQUEST_CODE_VIDEO);
//
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }


    @SuppressLint("WrongConstant")
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_VIDEO&&resultCode==RESULT_OK){
            uri = data.getData();
            iv.setImageURI(uri);
        }

    }
    private void showKey(ToggleButton button,int index){
        StringBuilder sb=new StringBuilder(tv_key.getText().toString());
       if(!flags[index]){
           sb.append(button.getTextOn()+"  ");
           flags[index]=true;
       }
       else {
           String string = button.getTextOn().toString();
           int i = sb.indexOf(string);
           if(i!=-1){
               sb.delete(i,i+string.length()+1);
               flags[index]=false;
           }
       }
        tv_key.setText(sb.toString());
    }


}
