package com.zpy.diabetes.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.AnswerListViewAdapter;
import com.zpy.diabetes.app.bean.AnswerBean;
import com.zpy.diabetes.app.bean.AnswerPageBean;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.bean.QuestionBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView leftImage;
    private ListView listview_answer;
    private View headView;
    private AnswerListViewAdapter adapter;
    private List list;
    private Bundle bundle;
    private QuestionBean questionBean;
    private CircularImageView image_answer_suffer_photo;
    private TextView tv_answer_suffer_phone,
            tv_answer_suffer_time,
            tv_answer_question_title,
            tv_answer_question_content,
            tv_doctor_answer_and_count,
            tv_doctor_submit_reply;

    private RelativeLayout layout_doctor_reply_content;
    private EditText et_doctor_reply_content;
    private ACProgressFlower loadingDialog;
    private Button btnLoadMore;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "问题详情");
        leftImage = myActionBar.getImageViewLeft();
        leftImage.setOnClickListener(this);
        listview_answer = (ListView) findViewById(R.id.listview_answer);
        headView = View.inflate(this, R.layout.answer_head_view, null);
        listview_answer.addHeaderView(headView);
        image_answer_suffer_photo = (CircularImageView) headView.findViewById(R.id.image_answer_suffer_photo);
        tv_answer_suffer_phone = (TextView) headView.findViewById(R.id.tv_answer_suffer_phone);
        tv_answer_suffer_time = (TextView) headView.findViewById(R.id.tv_answer_suffer_time);
        tv_answer_question_title = (TextView) headView.findViewById(R.id.tv_answer_question_title);
        tv_answer_question_content = (TextView) headView.findViewById(R.id.tv_answer_question_content);
        tv_doctor_answer_and_count = (TextView) headView.findViewById(R.id.tv_doctor_answer_and_count);
        layout_doctor_reply_content = (RelativeLayout) findViewById(R.id.layout_doctor_reply_content);
        et_doctor_reply_content = (EditText) findViewById(R.id.et_doctor_reply_content);
        tv_doctor_submit_reply = (TextView) findViewById(R.id.tv_doctor_submit_reply);
        tv_doctor_submit_reply.setOnClickListener(this);
        et_doctor_reply_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.isEmpty(String.valueOf(s))) {
                    tv_doctor_submit_reply.setClickable(false);
                } else {
                    tv_doctor_submit_reply.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        list = new ArrayList();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            questionBean = (QuestionBean) bundle.getSerializable("question");
        }
        int role_type = getApp().getShareDataInt(AppConfig.ROLE_TYPE);
        if (AppConfig.ROLE_TYPE_FOR_DOCTOR == role_type) {
            layout_doctor_reply_content.setVisibility(View.VISIBLE);
        } else {
            layout_doctor_reply_content.setVisibility(View.GONE);
        }
        loadingDialog = ActivityUtil.getLoadingDialog(this);
        btnLoadMore = ActivityUtil.getBtnLoadMore(this, btnLoadMore);
        btnLoadMore.setOnClickListener(this);
        currentPage = 1;
    }

    @Override
    public void show() {
        if (questionBean != null) {
            tv_answer_suffer_phone.setText(questionBean.getSuffererPhone());
            tv_answer_question_title.setText(questionBean.getTitle());
            tv_answer_question_content.setText(questionBean.getContent());
            tv_answer_suffer_time.setText(questionBean.getCreateD());
            x.image().bind(image_answer_suffer_photo, AppConfig.QINIU_IMAGE_URL + questionBean.getSuffererPhoto(), new ImageOptions.Builder().setFailureDrawableId(R.mipmap.img_default_photo_gray).setLoadingDrawableId(R.mipmap.img_default_photo_gray).build());
        }
        load(1, true);
    }

    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            this.finish();
        }
        if (v == tv_doctor_submit_reply) {
            String token = getApp().getShareDataStr(AppConfig.TOKEN);
            if (!TextUtil.isEmpty(token)) {
                submitMyAnswer(token);
            } else {
                Intent intent = new Intent(AnswerActivity.this, LoginActivity.class);
                startActivityForResult(intent, 400);
            }
        }
        if (v == btnLoadMore) {
            load(currentPage, false);
        }
    }

    private void submitMyAnswer(String token) {
        String answer = et_doctor_reply_content.getText().toString();
        getApp().getHttpApi().replyQuestion(token, questionBean.getId(), answer, loadingDialog, new IAppUserTokenBeanHolder() {
            @Override
            public void asynHold(AppBean bean) {
                if (bean != null) {
                    ResultBean resultBean = (ResultBean) bean;
                    if (AppConfig.OK.equals(resultBean.getCode())) {
                        Toast.makeText(AnswerActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                        et_doctor_reply_content.setText("");
                        //刷新回答列表
                        load(1, true);
                    }
                } else {
                    ActivityUtil.loadError(AnswerActivity.this);
                }
            }

            @Override
            public void overDue() {
                ActivityUtil.overdue(AnswerActivity.this, loadingDialog, false);
            }
        });
    }

    private void load(int pageNum, final boolean isClear) {
        getApp().getHttpApi().getAnswersForOneQuestion(questionBean.getId(), pageNum, loadingDialog, new IAppCommonBeanHolder() {
            @Override
            public void asynHold(AppBean bean) {
                if (bean != null) {
                    AnswerPageBean pageBean = (AnswerPageBean) bean;
                    if (AppConfig.OK.equals(pageBean.getCode())) {

                        if (isClear) {
                            list = new ArrayList<Map<String, String>>();
                            adapter = null;
                        }

                        List<AnswerBean> answerBeanList = pageBean.getAnswerBeanList();
                        tv_doctor_answer_and_count.setText("医生回答（" + String.valueOf(pageBean.getReplyCount()) + "）");
                        for (int i = 0; i < answerBeanList.size(); i++) {
                            Map item = new HashMap();
                            AnswerBean answerBean = answerBeanList.get(i);
                            item.put("answer_time", answerBean.getAnswerTime());
                            item.put("answer_content", answerBean.getAnswerContent());
                            item.put("answer_phone", answerBean.getAnswerPhone());
                            item.put("answer_photo", AppConfig.QINIU_IMAGE_URL + answerBean.getAnswerPhoto());
                            list.add(item);
                        }
                        if (adapter == null || isClear) {
                            adapter = new AnswerListViewAdapter(AnswerActivity.this, R.layout.answer_listview_item, list);
                            listview_answer.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        PageInfo pageInfo = pageBean.getPageInfo();
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
                            listview_answer.removeFooterView(btnLoadMore);
                        }
                    } else {
                        Toast.makeText(AnswerActivity.this, pageBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityUtil.loadError(AnswerActivity.this);
                }
            }
        });
    }
}
