package com.example.application;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.utils.UtilsHelper;

public class MyInfoView implements View.OnClickListener {
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    private boolean isLogin = false;

    private LinearLayout ll_head;
    private ImageView iv_head_icon;
    private RelativeLayout rl_course_history, rl_setting;
    private TextView tv_user_name;

    public MyInfoView(Activity context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mCurrentView = mInflater.inflate(R.layout.main_view_myinfo, null);


    }

    // 初始化界面控件
    private void initView() {
        ll_head = mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon = mCurrentView.findViewById(R.id.iv_head_icon);
        rl_course_history = mCurrentView.findViewById(R.id.rl_course_history);
        rl_setting = mCurrentView.findViewById(R.id.rl_setting);
        tv_user_name = mCurrentView.findViewById(R.id.tv_user_name);

        ll_head.setOnClickListener(this);
        rl_course_history.setOnClickListener(this);
        rl_setting.setOnClickListener(this);

        mCurrentView.setVisibility(View.VISIBLE);
        setLoginParams(isLogin);


    }

    // 设置用户名或提示登录信息
    public void setLoginParams(boolean isLogin) {
        if (isLogin) {
            tv_user_name.setText(UtilsHelper.readLoginUserName(mContext));
        } else {
            tv_user_name.setText("点击登录");
        }
    }

    // 返回视图
    public View getView() {
        isLogin = UtilsHelper.readLoginStatus(mContext);
        if (mCurrentView == null) {
            initView();
        }
        return mCurrentView;
    }

    // 显示视图
    public void showView() {
        if (mCurrentView == null) {
            initView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        updateUI();
        int viewId = view.getId();
        if (viewId == R.id.ll_head) {
            if (UtilsHelper.readLoginStatus(mContext)) {
                // 用户已登录，跳转到个人资料界面
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                mContext.startActivity(intent); // 确保在这里调用 startActivity

            } else {
                // 用户未登录，跳转到登录界面
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivityForResult(intent, 1);
            }
        } else if (viewId == R.id.rl_course_history) {
            if (UtilsHelper.readLoginStatus(mContext)) {

            } else {
                Toast.makeText(mContext, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.rl_setting) {
            if (UtilsHelper.readLoginStatus(mContext)) {
                // 跳转到设置界面
                Intent intent = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "您还未登录，请先登录", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateUI() {
        isLogin = UtilsHelper.readLoginStatus(mContext);
        String userName = isLogin ? UtilsHelper.readLoginUserName(mContext) : "点击登录";
        tv_user_name.setText(userName);
    }



}