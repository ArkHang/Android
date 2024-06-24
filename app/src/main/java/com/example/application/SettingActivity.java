package com.example.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.utils.UtilsHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_modify_psw, rl_security_setting, rl_exit_login;

    private MyInfoView sMyInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back = findViewById(R.id.tv_back);
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#00000000"));


        rl_modify_psw = findViewById(R.id.rl_modify_psw);
        rl_security_setting = findViewById(R.id.rl_security_setting);
        rl_exit_login = findViewById(R.id.rl_exit_login);

        tv_back.setOnClickListener(this);
        rl_modify_psw.setOnClickListener(this);
        rl_security_setting.setOnClickListener(this);
        rl_exit_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.rl_modify_psw) {
            Intent intent = new Intent(this, ModifyPswActivity.class);
            startActivity(intent);
        } else if (id == R.id.rl_security_setting) {
            Intent intent = new Intent(this, SecuritySettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.rl_exit_login) {
            UtilsHelper.clearLoginStatus(this); // 清除登录状态

            // 创建 Intent 来启动 MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 清除此任务栈并启动新任务
            intent.putExtra("isLogin", false); // 传递登录状态
            startActivity(intent); // 启动 MainActivity
            finish(); // 结束当前 Activity
        }
    }

}
