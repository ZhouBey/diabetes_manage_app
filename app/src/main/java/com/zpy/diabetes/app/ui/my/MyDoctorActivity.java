package com.zpy.diabetes.app.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.DoctorListViewAdapter;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.ui.DoctorInfoActivity;
import com.zpy.diabetes.app.util.ActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDoctorActivity extends BaseActivity implements BaseUIInterf,View.OnClickListener {

    private ActionBar actionBar;
    private ImageView imageLeft;
    private ListView listview_my_doctors;
    private List list;
    private DoctorListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_doctor);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar,actionBar,R.mipmap.back,-1,"我的医生");
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        listview_my_doctors = (ListView) findViewById(R.id.listview_my_doctors);
        list = new ArrayList();
    }

    @Override
    public void show() {
        for (int i = 0; i < 10; i++) {
            Map item = new HashMap();
            item.put("doctor_name","周培源");
            item.put("doctor_position","院长大大大");
            item.put("doctor_for_hospital","河南中医学院第一附属医院");
            item.put("doctor_info","河南中医学院第一附属医院卢卡斯的浪费了落实到市领导放假了圣诞节福利及撒的了分解落实到吉林市的家乐福见识到了解放路口了解到了房间落实到家乐福会计师的浪费了可视对讲来电了深咖啡吉林市的房间了可适当记录卡是点击李开复见识到了快放假了可适当极乐空间乐居");
            list.add(item);
        }
        adapter = new DoctorListViewAdapter(this,R.layout.doctors_listview_item,list);
        listview_my_doctors.setAdapter(adapter);
        listview_my_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyDoctorActivity.this,DoctorInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == imageLeft) {
            this.finish();
        }
    }
}
