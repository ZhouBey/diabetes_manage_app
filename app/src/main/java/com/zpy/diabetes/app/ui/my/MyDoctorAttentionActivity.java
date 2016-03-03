package com.zpy.diabetes.app.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.DoctorListViewAdapter;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class MyDoctorAttentionActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView imageLeft;
    private ListView listview_my_doctors;
    private List list;
    private DoctorListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_doctor_attention);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "我的关注");
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        listview_my_doctors = (ListView) findViewById(R.id.listview_my_doctors);
        list = new ArrayList();
    }

    @Override
    public void show() {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if (!TextUtil.isEmpty(token)) {

        } else {
            Intent intent = new Intent(MyDoctorAttentionActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageLeft) {
            this.finish();
        }
    }
}
