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
import com.zpy.diabetes.app.adapter.HealthInfoListViewAdapter;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.HealthInfoBean;
import com.zpy.diabetes.app.bean.HealthInfoPageBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthInfoCenterActivity extends BaseActivity implements BaseUIInterf,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActionBar actionBar;
    private ImageView leftImage;
    private ListView listview_health_info;
    private List list;
    private HealthInfoListViewAdapter adapter;
    private SwipeRefreshLayout refreshLayoutHealthInfoCenter;
    private Button btnLoadMore;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_info_center);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "资讯中心");
        leftImage = myActionBar.getImageViewLeft();
        leftImage.setOnClickListener(this);
        listview_health_info = (ListView) findViewById(R.id.listview_health_info);
        list = new ArrayList();
        refreshLayoutHealthInfoCenter = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutHealthInfoCenter);
        ActivityUtil.setSwipeRefreshLayout(this, refreshLayoutHealthInfoCenter);
        refreshLayoutHealthInfoCenter.setOnRefreshListener(this);
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        listview_health_info.addFooterView(btnLoadMore);
        currentPage = 1;
    }


    @Override
    public void show() {
        refreshLayoutHealthInfoCenter.setRefreshing(true);
        load(currentPage, true);
    }

    private void load(int pageNum, final boolean isClear) {
        getApp().getHttpApi().getHealthInfoList(pageNum, refreshLayoutHealthInfoCenter,new IAppCommonBeanHolder() {
            @Override
            public void asynHold(AppBean bean) {
                if (bean != null) {
                    HealthInfoPageBean healthInfoPageBean = (HealthInfoPageBean) bean;
                    if (AppConfig.OK.equals(healthInfoPageBean.getCode())) {
                        if (isClear) {
                            list = new ArrayList<Map<String, String>>();
                            adapter = null;
                        }
                        final List<HealthInfoBean> healthInfoBeanList = healthInfoPageBean.getHealthInfoBeans();
                        for (int i = 0; i < healthInfoBeanList.size(); i++) {
                            Map<String, Object> item = new HashMap<>();
                            HealthInfoBean healthInfoBean = healthInfoBeanList.get(i);
                            item.put("info_title", healthInfoBean.getTitle());
                            item.put("info_time", healthInfoBean.getCreateD());
                            item.put("info_image", AppConfig.QINIU_IMAGE_URL + healthInfoBean.getInfoImage());
                            if (i < 3) {
                                item.put("info_tuijian", "1");
                            }
                            list.add(item);
                        }
                        if (adapter == null || isClear) {
                            adapter = new HealthInfoListViewAdapter(HealthInfoCenterActivity.this, R.layout.health_info_listview_item, list);
                            listview_health_info.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        PageInfo pageInfo = healthInfoPageBean.getPageInfo();
                        if (pageInfo.getTotalPage() != 0) {
                            btnLoadMore.setVisibility(View.VISIBLE);
                            if (pageInfo.getCurrentPage() < pageInfo.getTotalPage()) {
                                btnLoadMore.setVisibility(View.VISIBLE);
                                btnLoadMore.setText("加载更多");
                                btnLoadMore.setClickable(true);
                                currentPage++;
                            } else {
                                btnLoadMore.setVisibility(View.GONE);
                            }
                        } else {
                            btnLoadMore.setVisibility(View.GONE);
                        }
                        listview_health_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HealthInfoBean healthInfoBean = healthInfoBeanList.get(position);
                                Intent intent = new Intent(HealthInfoCenterActivity.this, HealthInfoDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("healthInfo", healthInfoBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(HealthInfoCenterActivity.this, healthInfoPageBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityUtil.loadError(HealthInfoCenterActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            this.finish();
        }
        if(v == btnLoadMore) {
            refreshLayoutHealthInfoCenter.setRefreshing(true);
            load(currentPage, false);
        }
    }

    @Override
    public void onRefresh() {
        load(1, true);
    }
}
