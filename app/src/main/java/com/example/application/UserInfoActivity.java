package com.example.application;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.bean.UserBean;

import com.example.utils.DBUtils;
import com.example.utils.UtilsHelper;

public class UserInfoActivity extends AppCompatActivity {
    private TextView tv_nickName, tv_signature, tv_user_name, tv_sex, tv_main_title, tv_back;
    private RelativeLayout rl_nickName, rl_sex, rl_signature;
    private DBUtils dbUtils;
    private String spUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        spUserName = UtilsHelper.readLoginUserName(this);

        init();
        setData();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("个人资料");
        tv_back = findViewById(R.id.tv_back);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_nickName = findViewById(R.id.tv_nickName);
        tv_sex = findViewById(R.id.tv_sex);
        tv_signature = findViewById(R.id.tv_signature);
        rl_nickName = findViewById(R.id.rl_nickName);
        rl_sex = findViewById(R.id.rl_sex);
        rl_signature = findViewById(R.id.rl_signature);

        tv_back.setOnClickListener(v -> onBackPressed());
        rl_nickName.setOnClickListener(v -> editNickName());
        rl_sex.setOnClickListener(v -> editSex());
        rl_signature.setOnClickListener(v -> editSignature());

        dbUtils = DBUtils.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setData() {
        UserBean bean = dbUtils.getUserInfo(spUserName);
        if (bean != null) {
            tv_user_name.setText(bean.getUserName());
            tv_nickName.setText(bean.getNickName());
            tv_sex.setText(bean.getSex());
            tv_signature.setText(bean.getSignature());
        } else {
            Toast.makeText(this, "无法加载用户数据", Toast.LENGTH_SHORT).show();
        }
    }

    private void editNickName() {
        showEditDialog("编辑昵称", tv_nickName, "nickName");
    }

    private void editSignature() {
        showEditDialog("编辑签名", tv_signature, "signature");
    }

    private void editSex() {
        final String[] options = {"男性", "女性", "其他"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择性别");
        builder.setItems(options, (dialog, which) -> {
            tv_sex.setText(options[which]);
            dbUtils.updateUserInfo("sex", options[which], spUserName);
            showToast("性别更新为：" + options[which]);
        });
        builder.show();
    }

    private void showEditDialog(String title, TextView textView, String field) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_text, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogStyle); // 应用自定义样式
        builder.setTitle(title);
        builder.setView(dialogView);

        final EditText input = dialogView.findViewById(R.id.editTextDialogUserInput);
        input.setText(textView.getText());

        builder.setPositiveButton("确定", (dialog, which) -> {
            String newValue = input.getText().toString();
            if (!TextUtils.isEmpty(newValue)) {
                dbUtils.updateUserInfo(field, newValue, spUserName);
                textView.setText(newValue);
                showToast(title + "已更新为：" + newValue);
            }
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
