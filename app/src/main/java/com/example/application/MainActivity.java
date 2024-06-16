package com.example.application;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;

import com.example.application.showModel.application.ShowView;
import com.example.application.uploadModel.application.UpLoadView;
import com.example.application.uploadModel.intentutils.ActivityResultCallback;
import com.example.utils.UtilsHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityResultCallback {
    private FrameLayout mBodyLayout;
    private LinearLayout mBottomLayout;
    private View mCourseBtn, mExercisesBtn, mMyInfoBtn,uploadBtn;
    private TextView tv_course, tv_exercises, tv_myInfo,tv_upload;
    private ImageView iv_course, iv_exercises, iv_myInfo;
    private MyInfoView mMyInfoView;

    private ShowView showView;
    private UpLoadView upLoadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListener();
        selectDisplayView(0);
    }

    private void init() {
        mBodyLayout = findViewById(R.id.main_body);
        mBottomLayout = findViewById(R.id.main_bottom_bar);
        mCourseBtn = findViewById(R.id.bottom_bar_course_btn);
        mExercisesBtn = findViewById(R.id.bottom_bar_exercises_btn);
        mMyInfoBtn = findViewById(R.id.bottom_bar_myinfo_btn);
        uploadBtn=findViewById(R.id.bottom_bar_upload);

        tv_course = findViewById(R.id.bottom_bar_text_course);
        tv_exercises = findViewById(R.id.bottom_bar_text_exercises);
        tv_myInfo = findViewById(R.id.bottom_bar_text_myinfo);
        tv_upload=findViewById(R.id.bottom_bar_upload1);

        iv_course = findViewById(R.id.bottom_bar_image_course);
        iv_exercises = findViewById(R.id.bottom_bar_image_exercises);
        iv_myInfo = findViewById(R.id.bottom_bar_image_myinfo);

        // Default to not selected status
        setNotSelectedStatus();
    }

    private void setListener() {
        // 此处代码确保正确无误，例子如下：
        mCourseBtn.setOnClickListener(this);
        mExercisesBtn.setOnClickListener(this);
        mMyInfoBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
    }

    private void setNotSelectedStatus() {
        tv_course.setTextColor(getColor(R.color.text_color_deselected));
        tv_exercises.setTextColor(getColor(R.color.text_color_deselected));
        tv_myInfo.setTextColor(getColor(R.color.text_color_deselected));

        iv_course.setImageResource(R.drawable.main_course_icon);
        iv_exercises.setImageResource(R.drawable.main_exercises_icon);
        iv_myInfo.setImageResource(R.drawable.main_my_icon);

        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setSelected(false);
        }
    }

    private void setSelectedStatus(int index) {
        setNotSelectedStatus();
        switch (index) {
            case 0:
                mCourseBtn.setSelected(true);
                iv_course.setImageResource(R.drawable.main_course_icon_selected);
                tv_course.setTextColor(getColor(R.color.text_color_selected));
                break;
            case 1:
                mExercisesBtn.setSelected(true);
                iv_exercises.setImageResource(R.drawable.main_exercises_icon_selected);
                tv_exercises.setTextColor(getColor(R.color.text_color_selected));
                break;
            case 2:
                mMyInfoBtn.setSelected(true);
                iv_myInfo.setImageResource(R.drawable.main_my_icon_selected);
                tv_myInfo.setTextColor(getColor(R.color.text_color_selected));
                break;
            case 3:
                uploadBtn.setSelected(true);
                tv_upload.setTextColor(R.drawable.main_course_icon_selected);
                break;
        }
    }

    private void selectDisplayView(int index) {
        hideAllView();
        createView(index);
        setSelectedStatus(index);
    }

    private void hideAllView() {
        for (int i = 0; i < mBodyLayout.getChildCount(); i++) {
            mBodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void createView(int viewIndex) {
        switch (viewIndex) {
            case 0:
                // Load Course View
                if(showView==null){
                    showView=new ShowView(this);
                    mBodyLayout.addView(showView.getView());
                }
                showView.showView();
                break;
            case 1:
                // Load Exercises View
                break;
            case 2:
                // Load My Info View
                if (mMyInfoView == null) {
                    mMyInfoView = new MyInfoView(this);
                    mBodyLayout.addView(mMyInfoView.getView());
                }
                mMyInfoView.showView();
                break;
            case 3:
                if(upLoadView==null){
                    upLoadView=new UpLoadView(this,this);
                    mBodyLayout.addView(upLoadView.getView());
                }
                upLoadView.showView();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bottom_bar_course_btn) {
            selectDisplayView(0);
        } else if (id == R.id.bottom_bar_exercises_btn) {
            selectDisplayView(1);
        } else if (id == R.id.bottom_bar_myinfo_btn) {
            selectDisplayView(2);
        } else if (id==R.id.bottom_bar_upload) {
            selectDisplayView(3);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                if (UtilsHelper.readLoginStatus(this)) {
                    UtilsHelper.clearLoginStatus(this);
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0; // For exit timing

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (upLoadView != null) {
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
            upLoadView.handleActivityResult(requestCode, resultCode, data);
        }
    }
}
