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
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyQuestionActivity extends BaseActivity implements View.OnClickListener,BaseUIInterf,SwipeRefreshLayout.OnRefreshListener {

    private ActionBar actionBar;
    private ImageView imageLeft;

    private ListView listview_my_question;
    private List list;
    private QaListViewItemAdapter adapter;
    private SwipeRefreshLayout refreshLayoutMyQuestion;
    private Button btnLoadMore;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_question);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar,actionBar, R.mipmap.back,-1,"我的提问");
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
        load(currentPage,true);

    }

    private void load(int pageNum, final boolean isClear) {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if(!TextUtil.isEmpty(token)) {
            getApp().getHttpApi().getMyQuestionList(token, pageNum,refreshLayoutMyQuestion, new IAppUserTokenBeanHolder() {
                @Override
                public void asynHold(AppBean bean) {
                    if(bean!=null) {
                        QuestionPageBean questionPageBean = (QuestionPageBean) bean;
                        if(AppConfig.OK.equals(questionPageBean.getCode())) {
                            if (isClear) {
                                list = new ArrayList<Map<String, String>>();
                                adapter = null;
                            }
                            List<QuestionBean> questionBeans = questionPageBean.getQuestionBeans();
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
                                adapter = new QaListViewItemAdapter(MyQuestionActivity.this, R.layout.qa_listview_item, list);
                                listview_my_question.setAdapter(adapter);
                            }
                            adapter.notifyDataSetChanged();
                            listview_my_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MyQuestionActivity.this, AnswerActivity.class);
                                    startActivity(intent);
                                }
                            });
                            PageInfo pageInfo = questionPageBean.getPageInfo();
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
                        } else {
                            Toast.makeText(MyQuestionActivity.this,questionPageBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityUtil.loadError(MyQuestionActivity.this);
                    }
                }

                @Override
                public void overDue() {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v == imageLeft) {
            this.finish();
        }
        if(v == btnLoadMore) {
            refreshLayoutMyQuestion.setRefreshing(true);
            load(currentPage,false);
        }
    }

    @Override
    public void onRefresh() {
        load(1,true);
    }

}
