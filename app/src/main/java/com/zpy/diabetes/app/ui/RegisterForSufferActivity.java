package com.zpy.diabetes.app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.my.MyCountDownTimer;
import com.zpy.diabetes.app.my.SmsObserver;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.LineEditText;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterForSufferActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private FrameLayout layoutLeft;
    private LineEditText et_register_suffer_phone,
            et_register_suffer_password,
            et_register_suffer_confirm_password,
            et_register_suffer_message_verify;

    private TextView tv_suffer_click_to_get_verify,
            tv_register_suffer;
    private MyCountDownTimer countDownTimer;
    private String phone,
            password;

    private ACProgressFlower loadingDialog;
    private SmsObserver smsObserver;
    private MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_for_suffer);
        init();
    }

    @Override
    public void init() {
        SMSSDK.initSDK(this, AppConfig.SMS_APPKEY, AppConfig.SMS_APPSECRET);
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "患者注册");
        layoutLeft = myActionBar.getLayout_my_actionbar_left();
        layoutLeft.setOnClickListener(this);
        tv_suffer_click_to_get_verify = (TextView) findViewById(R.id.tv_suffer_click_to_get_verify);
        tv_suffer_click_to_get_verify.setOnClickListener(this);
        tv_register_suffer = (TextView) findViewById(R.id.tv_register_suffer);
        tv_register_suffer.setOnClickListener(this);
        et_register_suffer_phone = (LineEditText) findViewById(R.id.et_register_suffer_phone);
        et_register_suffer_password = (LineEditText) findViewById(R.id.et_register_suffer_password);
        et_register_suffer_confirm_password = (LineEditText) findViewById(R.id.et_register_suffer_confirm_password);
        et_register_suffer_message_verify = (LineEditText) findViewById(R.id.et_register_suffer_message_verify);
        countDownTimer = ActivityUtil.getMyCountDownTimer(this, tv_suffer_click_to_get_verify);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        loadingDialog = ActivityUtil.getLoadingDialog(this);
        smsObserver = new SmsObserver(this, handler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, smsObserver);
    }


    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if (v == layoutLeft) {
            this.finish();
        }
        if (v == tv_suffer_click_to_get_verify) {
            if (prepareForSend()) {
                phone = et_register_suffer_phone.getText().toString();
                SMSSDK.getVerificationCode("86", phone);
                loadingDialog.show();
            }
        }
        if (v == tv_register_suffer) {
            if (prepareForSend()) {
                String code = et_register_suffer_message_verify.getText().toString();
                if (!TextUtil.isEmpty(code)) {
                    phone = et_register_suffer_phone.getText().toString();
                    SMSSDK.submitVerificationCode("86", phone, code);
                    loadingDialog.show();
                }

            }
        }
    }

    //发送短信验证码前的核对
    private boolean prepareForSend() {
        String phone = et_register_suffer_phone.getText().toString();
        if (TextUtil.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtil.phoneCheck(phone)) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        String password = et_register_suffer_password.getText().toString();
        if (TextUtil.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String passwordConfirm = et_register_suffer_confirm_password.getText().toString();
        if (TextUtil.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "请再次输入密码核对", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtil.passwordCheck(password)) {
            Toast.makeText(this, "密码为6到20位数字和字母组合", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AppConfig.READ_MSG) {
                et_register_suffer_message_verify.setText(msg.obj.toString());
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //短信注册成功后，返回MainActivity,然后提示新好友
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        phone = et_register_suffer_phone.getText().toString();
                        password = et_register_suffer_password.getText().toString();
                        getApp().getHttpApi().registerForSufferer(phone, password, loadingDialog, new IAppCommonBeanHolder() {
                            @Override
                            public void asynHold(AppBean appBean) {
                                if (appBean != null) {
                                    ResultBean bean = (ResultBean) appBean;
                                    if (AppConfig.OK.equals(bean.getCode())) {
                                        Toast.makeText(RegisterForSufferActivity.this, "注册成功,开始登录吧", Toast.LENGTH_SHORT).show();
                                        RegisterForSufferActivity.this.finish();
                                    } else {
                                        Toast.makeText(RegisterForSufferActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ActivityUtil.loadError(RegisterForSufferActivity.this);
                                }
                            }
                        });
                    }
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        loadingDialog.dismiss();
                        Toast.makeText(RegisterForSufferActivity.this, "验证码已经发送,请注意查收", Toast.LENGTH_SHORT).show();
                        countDownTimer.start();
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(smsObserver);
    }
}
