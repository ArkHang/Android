package com.example.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.UtilsHelper;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title, tv_back;
    private Button btn_register;
    private EditText et_user_name, et_psw, et_psw_again;
    private String userName, passWord, pswAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);

        tv_main_title.setText("注册");
        tv_back.setOnClickListener(v -> finish());

        btn_register.setOnClickListener(v -> {
            getEditString();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(passWord)) {
                Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pswAgain)) {
                Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            } else if (!passWord.equals(pswAgain)) {
                Toast.makeText(RegisterActivity.this, "输入两次的密码不一致", Toast.LENGTH_SHORT).show();
            } else if (UtilsHelper.isExistUserName(RegisterActivity.this, userName)) {
                Toast.makeText(RegisterActivity.this, "此用户名已经存在", Toast.LENGTH_SHORT).show();
            } else {
                UtilsHelper.saveUserInfo(RegisterActivity.this, userName, passWord,"","","");
                Toast.makeText(RegisterActivity.this, "注册成功"+userName, Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("userName", userName);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        passWord = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();

    }
}
