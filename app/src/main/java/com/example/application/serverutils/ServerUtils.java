package com.example.application.serverutils;

import static com.example.application.showModel.content.ServerContent.SERVER_URL;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.application.serverutils.utilsinterface.AsyncCallback;
import com.example.application.showModel.bean.ParamArgu;
import com.example.application.uploadModel.bean.FaBuBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerUtils {

    private OkHttpClient client=new OkHttpClient();
    private Gson gson=new Gson();
    public void performAsyncOperation(final AsyncCallback callback,String method,String param){
        RequestBody body;
        ParamArgu argu = new ParamArgu();
        if (param!=null&&!param.equals("")){

            argu.setParam(param);
            String json = gson.toJson(argu);
            body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        }
      else{
          argu.setParam("");
          String json = gson.toJson(argu);
          body=RequestBody.create(MediaType.parse("application/json;charset=utf-8"),json);
        }
        Request build = new Request.Builder()
                .url(SERVER_URL + method)
                .post(body)
                .build();
        client.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsons = response.body().string();
                    Type type = new TypeToken<List<FaBuBean>>() {}.getType();
                    final List<FaBuBean> faBuBeans = gson.fromJson(jsons, type);
                    callback.onSuccess(faBuBeans);
                } else {
                    // 在这里处理响应错误，例如回调onFailure方法
                }
            }
        });
    }
}
