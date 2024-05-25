package com.example.application;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class ModifyPswActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_title, tv_back;
    private EditText et_original_psw, et_new_psw, et_new_psw_again;
    private Button btn_save;
    private String originalPsw, newPsw, newPswAgain;
    private String spOriginalPsw, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        init();
        userName = UtilsHelper.readLoginUserName(this);
        spOriginalPsw = UtilsHelper.readPsw(this, userName);
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        tv_back = findViewById(R.id.tv_back);
        et_original_psw = findViewById(R.id.et_original_psw);
        et_new_psw = findViewById(R.id.et_new_psw);
        et_new_psw_again = findViewById(R.id.et_new_psw_again);
        btn_save = findViewById(R.id.btn_save);

        tv_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_back) {
            finish();
        } else if (id == R.id.btn_save) {
            updatePassword();
        }
    }

    private void updatePassword() {
        originalPsw = et_original_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
        newPswAgain = et_new_psw_again.getText().toString().trim();

        if (TextUtils.isEmpty(originalPsw)) {
            Toast.makeText(this, "请输入原始密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!originalPsw.equals(spOriginalPsw)) {
            Toast.makeText(this, "输入的原始密码不正确", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(newPsw) || TextUtils.isEmpty(newPswAgain)) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!newPsw.equals(newPswAgain)) {
            Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        } else if (newPsw.equals(spOriginalPsw)) {
            Toast.makeText(this, "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
            return;
        } else {
            UtilsHelper.updatePsw(this, userName, newPsw,"","","");
            Toast.makeText(this, "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}