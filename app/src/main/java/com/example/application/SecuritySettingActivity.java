package com.example.application;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.UtilsHelper;

public class SecuritySettingActivity extends AppCompatActivity {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private EditText etValidateName;
    private Button btnSave;
    private String spUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security);
        spUserName = UtilsHelper.readLoginUserName(this);

        init();
        initializeViews();
    }
    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置密保");
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));

        tv_back = findViewById(R.id.tv_back);  // 确保你的布局文件中有这个ID对应的返回按钮
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // 结束当前活动，返回上一个活动
            }
        });
    }

    private void initializeViews() {
        etValidateName = findViewById(R.id.et_validate_name);
        btnSave = findViewById(R.id.btn_validate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSecurityQuestion();
            }
        });
    }

    private void saveSecurityQuestion() {

        String securityAnswer = etValidateName.getText().toString().trim();
        if (!securityAnswer.isEmpty()) {
            UtilsHelper.setSecurity(this, spUserName, securityAnswer);

            Toast.makeText(this, "密保答案已保存！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请输入密保答案！", Toast.LENGTH_SHORT).show();
        }
    }
}