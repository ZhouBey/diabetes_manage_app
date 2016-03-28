package com.zpy.diabetes.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

public class AskActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private FrameLayout layoutLeft;
    private TextView tv_submit_question;
    private EditText et_ask_question_title,
            et_ask_question_content;
    private String title, content;
    private ACProgressFlower loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "提问");
        layoutLeft = myActionBar.getLayout_my_actionbar_left();
        layoutLeft.setOnClickListener(this);
        tv_submit_question = (TextView) findViewById(R.id.tv_submit_question);
        tv_submit_question.setOnClickListener(this);
        et_ask_question_title = (EditText) findViewById(R.id.et_ask_question_title);
        et_ask_question_content = (EditText) findViewById(R.id.et_ask_question_content);
        loadingDialog = ActivityUtil.getLoadingDialog(this);
    }

    @Override
    public void show() {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if (TextUtil.isEmpty(token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layoutLeft) {
            this.finish();
        }
        if (v == tv_submit_question) {
            title = et_ask_question_title.getText().toString();
            if (TextUtil.isEmpty(title)) {
                Toast.makeText(this, "标题不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            content = et_ask_question_content.getText().toString();
            if (TextUtil.isEmpty(content)) {
                Toast.makeText(this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = getApp().getShareDataStr(AppConfig.TOKEN);
            if (!TextUtil.isEmpty(token)) {
                loadingDialog.show();
                getApp().getHttpApi().askQuestion(token, title, content, loadingDialog, new IAppUserTokenBeanHolder() {
                    @Override
                    public void asynHold(AppBean bean) {
                        if (bean != null) {
                            ResultBean resultBean = (ResultBean) bean;
                            if (AppConfig.OK.equals(resultBean.getCode())) {
                                Toast.makeText(AskActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                AskActivity.this.setResult(AppConfig.REFRESH_QA_LIST);
                                AskActivity.this.finish();
                            } else {
                                Toast.makeText(AskActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ActivityUtil.loadError(AskActivity.this);
                        }
                    }

                    @Override
                    public void overDue() {
                        ActivityUtil.overdue(AskActivity.this, loadingDialog, false);
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != AppConfig.LOGIN_OK_RESULT) {
            this.finish();
        }
    }
}
