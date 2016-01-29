package com.zpy.diabetes.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.DoctorListViewAdapter;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.DoctorBean;
import com.zpy.diabetes.app.bean.DoctorPageBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.widget.MyActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorsActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActionBar actionBar;
    private ImageView leftImage;
    private ListView listview_doctors;
    private List list;
    private DoctorListViewAdapter adapter;
    private SwipeRefreshLayout refreshLayoutDoctorList;
    private Button btnLoadMore;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors);
        init();
        show();
    }


    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "医生团队");
        leftImage = myActionBar.getImageViewLeft();
        leftImage.setOnClickListener(this);
        listview_doctors = (ListView) findViewById(R.id.listview_doctors);
        list = new ArrayList();
        refreshLayoutDoctorList = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutDoctorList);
        ActivityUtil.setSwipeRefreshLayout(this, refreshLayoutDoctorList);
        refreshLayoutDoctorList.setOnRefreshListener(this);
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        listview_doctors.addFooterView(btnLoadMore);
        currentPage = 1;
    }

    @Override
    public void show() {
        refreshLayoutDoctorList.setRefreshing(true);
        load(currentPage, true);
    }

    private void load(int pageNum, final boolean isClear) {
        getApp().getHttpApi().getAllDoctorList(pageNum, refreshLayoutDoctorList, new IAppCommonBeanHolder() {
            @Override
            public void asynHold(AppBean bean) {
                if (bean != null) {
                    DoctorPageBean doctorPageBean = (DoctorPageBean) bean;
                    if (AppConfig.OK.equals(doctorPageBean.getCode())) {
                        if (isClear) {
                            list = new ArrayList();
                            adapter = null;
                        }
                        final List<DoctorBean> doctorBeanList = doctorPageBean.getDoctorBeans();
                        for (int i = 0; i < doctorBeanList.size(); i++) {
                            DoctorBean doctorBean = doctorBeanList.get(i);
                            Map item = new HashMap();
                            item.put("doctor_name", doctorBean.getName());
                            item.put("doctor_position", doctorBean.getPost());
                            item.put("doctor_for_hospital", doctorBean.getHospital());
                            item.put("doctor_info", doctorBean.getInfo());
                            list.add(item);
                        }
                        if (adapter == null || isClear) {
                            adapter = new DoctorListViewAdapter(DoctorsActivity.this, R.layout.doctors_listview_item, list);
                            listview_doctors.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        PageInfo pageInfo = doctorPageBean.getPageInfo();
                        if (pageInfo.getTotalPage() != 0) {
                            btnLoadMore.setVisibility(View.VISIBLE);
                            if (pageInfo.getCurrentPage() < pageInfo.getTotalPage()) {
                                btnLoadMore.setText("加载更多");
                                btnLoadMore.setClickable(true);
                                currentPage++;
                            } else {
                                btnLoadMore.setText("加载完毕");
                                btnLoadMore.setClickable(false);
                            }
                        } else {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        listview_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(DoctorsActivity.this, DoctorInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("doctor", doctorBeanList.get(position));
                                bundle.putBoolean("isLogout",false);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(DoctorsActivity.this, doctorPageBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityUtil.loadError(DoctorsActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            this.finish();
        }
        if (v == btnLoadMore) {
            refreshLayoutDoctorList.setRefreshing(true);
            load(currentPage, false);
        }
    }

    @Override
    public void onRefresh() {
        load(1, true);
    }
}
