package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.fragment.DoctorRegisterOneFragment;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.util.ActivityUtil;

import cn.smssdk.SMSSDK;

public class RegisterForDoctorActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private FrameLayout layoutLeft;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_for_doctor);
        init();
    }

    @Override
    public void init() {
        SMSSDK.initSDK(this, AppConfig.SMS_APPKEY, AppConfig.SMS_APPSECRET);
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "医生注册");
        layoutLeft = myActionBar.getLayout_my_actionbar_left();
        layoutLeft.setOnClickListener(this);
        ActivityUtil.switchoverFragment(this, new DoctorRegisterOneFragment(), R.id.contain_register_for_doctor);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if (v == layoutLeft) {
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
