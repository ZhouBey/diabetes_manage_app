package com.zpy.diabetes.app.ui.my;

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
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.ui.DoctorInfoActivity;
import com.zpy.diabetes.app.ui.DoctorsActivity;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDoctorAttentionActivity extends BaseActivity implements BaseUIInterf,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActionBar actionBar;
    private ImageView imageLeft;
    private ListView listview_my_doctors;
    private List list;
    private Button btnLoadMore;
    private DoctorListViewAdapter adapter;
    private SwipeRefreshLayout refreshLayoutMyAttention;
    private int currentPage;

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
        refreshLayoutMyAttention = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutMyAttention);
        ActivityUtil.setSwipeRefreshLayout(this, refreshLayoutMyAttention);
        refreshLayoutMyAttention.setOnRefreshListener(this);
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        listview_my_doctors.addFooterView(btnLoadMore);
        currentPage = 1;
    }

    @Override
    public void show() {
        refreshLayoutMyAttention.setRefreshing(true);
        load(currentPage, true);
    }

    @Override
    public void onClick(View v) {
        if (v == imageLeft) {
            this.finish();
        }
        if (v == btnLoadMore) {
            refreshLayoutMyAttention.setRefreshing(true);
            load(currentPage, false);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        load(currentPage, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConfig.LOGIN_OK_RESULT == resultCode) {
            currentPage = 1;
            show();
        } else {
            this.finish();
        }
    }

    private void load(int pageNum, final boolean isClear) {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if (!TextUtil.isEmpty(token)) {
            refreshLayoutMyAttention.setRefreshing(true);
            getApp().getHttpApi().getDoctorAttentionForSuffer(token, pageNum, refreshLayoutMyAttention, new IAppUserTokenBeanHolder() {
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
                                adapter = new DoctorListViewAdapter(MyDoctorAttentionActivity.this, R.layout.doctors_listview_item, list);
                                listview_my_doctors.setAdapter(adapter);
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
                            listview_my_doctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MyDoctorAttentionActivity.this, DoctorInfoActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("doctor", doctorBeanList.get(position));
                                    bundle.putInt("is_mime",0);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Toast.makeText(MyDoctorAttentionActivity.this, doctorPageBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityUtil.loadError(MyDoctorAttentionActivity.this);
                    }
                }

                @Override
                public void overDue() {
                    refreshLayoutMyAttention.setRefreshing(false);
                    ActivityUtil.overdue(MyDoctorAttentionActivity.this, null, true);
                }
            });
        } else {
            Intent intent = new Intent(MyDoctorAttentionActivity.this, LoginActivity.class);
            startActivityForResult(intent, 101);
        }
    }
}
