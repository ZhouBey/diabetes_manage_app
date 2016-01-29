package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.util.ActivityUtil;

public class ForgetPasswordActivity extends BaseActivity implements BaseUIInterf,View.OnClickListener {

    private ActionBar actionBar;
    private ImageView imageLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        init();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar,actionBar, R.mipmap.back,-1,"找回密码");
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if(v == imageLeft) {
            this.finish();
        }
    }
}
