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
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));

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
            UtilsHelper.clearLoginStatus(this);
            Intent data = new Intent();
            setResult(RESULT_OK, data);

            data.putExtra("isLogin", false);
            sMyInfoView.updateUI();
            finish();
        }
    }
}
