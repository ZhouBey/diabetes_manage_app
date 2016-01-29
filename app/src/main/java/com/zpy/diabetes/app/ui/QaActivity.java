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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.QaListViewItemAdapter;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.bean.QuestionBean;
import com.zpy.diabetes.app.bean.QuestionPageBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.MyActionBar;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QaActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActionBar actionBar;
    private ListView listview_question;
    private List list;
    private QaListViewItemAdapter adapter;
    private RelativeLayout layout_ask_question;
    private SwipeRefreshLayout refreshLayoutQuestion;
    private Button btnLoadMore;
    private int currentPage;
    private ImageView imageLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_activity);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "问答动态");
        listview_question = (ListView) findViewById(R.id.listview_question);
        list = new ArrayList();
        layout_ask_question = (RelativeLayout) findViewById(R.id.layout_ask_question);
        layout_ask_question.setOnClickListener(this);
        refreshLayoutQuestion = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutQuestion);
        ActivityUtil.setSwipeRefreshLayout(this, refreshLayoutQuestion);
        refreshLayoutQuestion.setOnRefreshListener(this);
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        listview_question.addFooterView(btnLoadMore);
        currentPage = 1;
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
    }

    @Override
    public void show() {
        refreshLayoutQuestion.setRefreshing(true);
        load(1, true);
    }

    private void load(int pageNum, final boolean isClear) {
        getApp().getHttpApi().getQuestionList(pageNum, new IAppCommonBeanHolder() {
            @Override
            public void asynHold(final AppBean bean) {
                refreshLayoutQuestion.setRefreshing(false);
                if (bean != null) {
                    QuestionPageBean questionPageBean = (QuestionPageBean) bean;
                    if (AppConfig.OK.equals(questionPageBean.getCode())) {
                        if (isClear) {
                            list = new ArrayList<Map<String, String>>();
                            adapter = null;
                        }
                        final List<QuestionBean> questionBeanList = questionPageBean.getQuestionBeans();
                        for (int i = 0; i < questionBeanList.size(); i++) {
                            QuestionBean questionBean = questionBeanList.get(i);
                            Map item = new HashMap();
                            item.put("suffer_phone", TextUtil.getencryptPhone(questionBean.getSuffererPhone()));
                            item.put("question_title", questionBean.getTitle());
                            item.put("question_content", questionBean.getContent());
                            item.put("qa_reply_count", String.valueOf(questionBean.getReplyCount()));
                            item.put("question_time", questionBean.getCreateD());
                            list.add(item);
                        }
                        if (adapter == null || isClear) {
                            adapter = new QaListViewItemAdapter(QaActivity.this, R.layout.qa_listview_item, list);
                            listview_question.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        listview_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(QaActivity.this, AnswerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("question", questionBeanList.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        PageInfo pageInfo = questionPageBean.getPageInfo();
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
                    } else {
                        Toast.makeText(QaActivity.this, questionPageBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityUtil.loadError(QaActivity.this);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == imageLeft) {
            this.finish();
        }
        if (v == layout_ask_question) {
            Intent intent = new Intent(QaActivity.this, AskActivity.class);
            startActivityForResult(intent, 22);
        }
        if (v == btnLoadMore) {
            refreshLayoutQuestion.setRefreshing(true);
            load(currentPage, false);
        }
    }

    @Override
    public void onRefresh() {
        load(1, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConfig.REFRESH_QA_LIST == resultCode) {
            show();
        }
    }
}
