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
import com.zpy.diabetes.app.adapter.QaListViewItemAdapter;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.bean.QuestionBean;
import com.zpy.diabetes.app.bean.QuestionPageBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.ui.AnswerActivity;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的参与
 */
public class MyParticipateActivity extends BaseActivity implements View.OnClickListener, BaseUIInterf, SwipeRefreshLayout.OnRefreshListener {

    private ActionBar actionBar;
    private ImageView imageLeft;

    private ListView listview_my_question;
    private List list;
    private QaListViewItemAdapter adapter;
    private SwipeRefreshLayout refreshLayoutMyQuestion;
    private Button btnLoadMore;
    private int currentPage;
    int role_type;
    String requestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_question);
        init();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        role_type = getApp().getShareDataInt(AppConfig.ROLE_TYPE);
        if (AppConfig.ROLE_TYPE_FOR_DOCTOR == role_type) {
            ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "我的回复");
            requestUrl = AppConfig.GET_MY_REPLY_LIST;
        } else {
            ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "我的提问");
            requestUrl = AppConfig.GET_MY_QUESTION_LIST;
        }
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        listview_my_question = (ListView) findViewById(R.id.listview_my_question);
        refreshLayoutMyQuestion = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutMyQuestion);
        ActivityUtil.setSwipeRefreshLayout(this, refreshLayoutMyQuestion);
        refreshLayoutMyQuestion.setOnRefreshListener(this);
        list = new ArrayList();
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        listview_my_question.addFooterView(btnLoadMore);
        currentPage = 1;
    }

    @Override
    public void show() {
        refreshLayoutMyQuestion.setRefreshing(true);
        load(currentPage, true);
    }

    private void load(int pageNum, final boolean isClear) {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if (!TextUtil.isEmpty(token)) {
            getApp().getHttpApi().getMyQuestionList(requestUrl, token, pageNum, refreshLayoutMyQuestion, new IAppUserTokenBeanHolder() {
                @Override
                public void asynHold(AppBean bean) {
                    if (bean != null) {
                        QuestionPageBean questionPageBean = (QuestionPageBean) bean;
                        if (AppConfig.OK.equals(questionPageBean.getCode())) {
                            if (isClear) {
                                list = new ArrayList<Map<String, String>>();
                                adapter = null;
                            }
                            final List<QuestionBean> questionBeans = questionPageBean.getQuestionBeans();
                            for (int i = 0; i < questionBeans.size(); i++) {
                                QuestionBean questionBean = questionBeans.get(i);
                                Map item = new HashMap();
                                item.put("suffer_phone", TextUtil.getencryptPhone(getApp().getShareDataStr(AppConfig.PHONE)));
                                item.put("question_title", questionBean.getTitle());
                                item.put("question_content", questionBean.getContent());
                                item.put("qa_reply_count", String.valueOf(questionBean.getReplyCount()));
                                item.put("question_time", questionBean.getCreateD());
                                list.add(item);
                            }
                            if (adapter == null || isClear) {
                                adapter = new QaListViewItemAdapter(MyParticipateActivity.this, R.layout.qa_listview_item, list);
                                listview_my_question.setAdapter(adapter);
                            }
                            adapter.notifyDataSetChanged();
                            listview_my_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MyParticipateActivity.this, AnswerActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("question", questionBeans.get(position));
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
                            Toast.makeText(MyParticipateActivity.this, questionPageBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityUtil.loadError(MyParticipateActivity.this);
                    }
                }

                @Override
                public void overDue() {

                }
            });
        } else {
            Intent intent = new Intent(MyParticipateActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageLeft) {
            this.finish();
        }
        if (v == btnLoadMore) {
            refreshLayoutMyQuestion.setRefreshing(true);
            load(currentPage, false);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        load(currentPage, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        show();
    }
}
