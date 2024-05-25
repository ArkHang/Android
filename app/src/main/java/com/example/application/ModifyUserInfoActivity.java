package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_title, tv_save;
    private EditText et_content;
    private ImageView iv_delete;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        init();
    }

    private void init() {
        flag = getIntent().getIntExtra("flag", 0);
        et_content = findViewById(R.id.et_content);
        iv_delete = findViewById(R.id.iv_delete);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_save = findViewById(R.id.tv_save);

        iv_delete.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_delete.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Limit the text length
                if (flag == 1 && s.length() > 8) {
                    s.delete(8, s.length());
                } else if (flag == 2 && s.length() > 16) {
                    s.delete(16, s.length());
                }
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.iv_delete) {
            et_content.setText("");
        } else if (id == R.id.tv_save) {
            saveContent();
        }
    }


    private void saveContent() {
        String content = et_content.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(flag == 1 ? "nickName" : "signature", content);
            setResult(RESULT_OK, data);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}