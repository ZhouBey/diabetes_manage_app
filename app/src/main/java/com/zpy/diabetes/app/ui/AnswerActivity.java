package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.AnswerListViewAdapter;
import com.zpy.diabetes.app.bean.QuestionBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.my.MyCommonCallbackForDrawable;
import com.zpy.diabetes.app.util.ActivityUtil;

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
            tv_doctor_answer_and_count;

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
        list = new ArrayList();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            questionBean = (QuestionBean) bundle.getSerializable("question");
        }
    }

    @Override
    public void show() {
        if(questionBean!=null) {
            tv_answer_suffer_phone.setText(questionBean.getSuffererPhone());
            tv_answer_question_title.setText(questionBean.getTitle());
            tv_answer_question_content.setText(questionBean.getContent());
            tv_answer_suffer_time.setText(questionBean.getCreateD());
            x.image().bind(image_answer_suffer_photo, AppConfig.QINIU_IMAGE_URL + questionBean.getSuffererPhoto(), new MyCommonCallbackForDrawable(this, image_answer_suffer_photo, R.mipmap.img_default_photo_gray));
        }
        tv_doctor_answer_and_count.setText("医生回答（2）");

        for (int i = 0; i < 5; i++) {
            Map item = new HashMap();
            item.put("answer_time", "2015-12-22 15:33");
            item.put("answer_content", "是来得及发了圣诞节了房间拉萨的减肥了凯撒的几率咖啡机卢卡斯的肌肤莱卡时间到了快放假了萨克的减肥了凯撒的记录卡附件里是");
            item.put("answer_phone", "138***789");
            list.add(item);
        }
        adapter = new AnswerListViewAdapter(this, R.layout.answer_listview_item, list);
        listview_answer.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            this.finish();
        }
    }
}
