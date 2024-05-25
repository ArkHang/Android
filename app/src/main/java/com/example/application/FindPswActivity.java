package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.UtilsHelper;

public class FindPswActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_title, tv_back;
    private EditText et_validate_name, et_user_name;
    private Button btn_validate;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        from = getIntent().getStringExtra("from");
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        et_validate_name = findViewById(R.id.et_validate_name);
        et_user_name = findViewById(R.id.et_user_name);
        btn_validate = findViewById(R.id.btn_validate);
        tv_back.setOnClickListener(this);
        btn_validate.setOnClickListener(this);

        if ("security".equals(from)) {
            tv_main_title.setText("设置密保");
            et_user_name.setVisibility(View.GONE);
            btn_validate.setText("保存");
        } else {
            tv_main_title.setText("找回密码");
            et_user_name.setVisibility(View.VISIBLE);
            btn_validate.setText("验证");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.btn_validate) {
            if ("security".equals(from)) {
                saveSecurity();
            } else {
                verifySecurity();
            }
        }
    }


    private void saveSecurity() {
        String validateName = et_validate_name.getText().toString().trim();
        if (TextUtils.isEmpty(validateName)) {
            Toast.makeText(this, "请输入密保姓名", Toast.LENGTH_SHORT).show();
        } else {
            // 保存密保信息
            SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            String userName = UtilsHelper.readLoginUserName(this);
            editor.putString(userName + "_security", validateName);
            editor.commit();
            Toast.makeText(this, "密保设置成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void verifySecurity() {
        String userName = et_user_name.getText().toString().trim();
        String validateName = et_validate_name.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(validateName)) {
            Toast.makeText(this, "请输入密保姓名", Toast.LENGTH_SHORT).show();
        } else {
            String registeredSecurity=UtilsHelper.readSecurity(this,userName);
            if (validateName.equals(registeredSecurity)) {
                // 密保匹配成功，重置密码
                Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
                resetPassword(userName);
            } else {
                Toast.makeText(this, "密保信息不正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetPassword(String userName) {
        UtilsHelper.updatePsw(this, userName, "123456","","","");
   
        Toast.makeText(this, "密码已重置为：123456", Toast.LENGTH_LONG).show();
        // 退出登录状态
        UtilsHelper.clearLoginStatus(this);
        // 返回登录界面
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
