package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.MD5Utils;
import com.example.utils.UtilsHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_title, tv_back, tv_register, tv_find_psw;
    private Button btn_login;
    private EditText et_user_name, et_passWord;
    private String userName, passWord, spPsw;
    private boolean isLogin = false;
    private MyInfoView lMyInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = findViewById(R.id.tv_back);
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_passWord = findViewById(R.id.et_psw);

        tv_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_find_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.tv_find_psw) {
            Intent intent = new Intent(LoginActivity.this, FindPswActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.btn_login) {
            handleLogin();
        }
    }

    private void handleLogin() {
        userName = et_user_name.getText().toString().trim();
        passWord = et_passWord.getText().toString().trim();
        spPsw = UtilsHelper.readPsw(this, userName);

        if (validateInput(userName, passWord, spPsw)) {
            loginSuccess();
        }
    }

    private boolean validateInput(String userName, String passWord, String spPsw) {
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(spPsw)) {
            Toast.makeText(this, "此用户名不存在", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(passWord)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!passWord.equals(spPsw)) {
            Toast.makeText(this, passWord+"|"+spPsw, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginSuccess() {
        Toast.makeText(this, "登录成功，欢迎 " + userName + "！", Toast.LENGTH_LONG).show();
        UtilsHelper.saveLoginStatus(this, true, userName);

        Intent data = new Intent();
        data.putExtra("isLogin", true);
        data.putExtra("userName", userName);

        lMyInfoView.updateUI();
        setResult(RESULT_OK, data);
        finish();
    }
}

