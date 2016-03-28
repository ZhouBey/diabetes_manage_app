package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

public class FeedBackActivity extends BaseActivity implements BaseUIInterf,View.OnClickListener {

    private ActionBar actionBar;
    private FrameLayout layoutLeft;
    private EditText et_opinion_content;
    private Button btn_feed_back_submit;
    private ACProgressFlower loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        init();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar,actionBar, R.mipmap.back,-1,"意见反馈");
        layoutLeft = myActionBar.getLayout_my_actionbar_left();
        layoutLeft.setOnClickListener(this);
        et_opinion_content = (EditText) findViewById(R.id.et_opinion_content);
        btn_feed_back_submit = (Button) findViewById(R.id.btn_feed_back_submit);
        btn_feed_back_submit.setOnClickListener(this);
        loadingDialog = ActivityUtil.getLoadingDialog(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if(v == layoutLeft) {
            this.finish();
        }
        if(v == btn_feed_back_submit) {
            String content = et_opinion_content.getText().toString();
            if(TextUtil.isEmpty(content)) {
                Toast.makeText(this,"请输入您的意见或建议",Toast.LENGTH_SHORT).show();
                return;
            }
            loadingDialog.show();
            String token = getApp().getShareDataStr(AppConfig.TOKEN);
            getApp().getHttpApi().feedBack(token, content, loadingDialog, new IAppCommonBeanHolder() {
                @Override
                public void asynHold(AppBean bean) {
                    if(bean!=null) {
                        ResultBean resultBean = (ResultBean) bean;
                        if(AppConfig.OK.equals(resultBean.getCode())) {
                            Toast.makeText(FeedBackActivity.this,"反馈成功！",Toast.LENGTH_SHORT).show();
                            FeedBackActivity.this.finish();
                        } else {
                            Toast.makeText(FeedBackActivity.this,resultBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityUtil.loadError(FeedBackActivity.this);
                    }
                }
            });
        }
    }
}
