package com.zpy.diabetes.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.bean.TokenBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.LineEditText;
import com.zpy.diabetes.app.widget.SelectDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class LoginActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private TextView tv_login_register,
            tv_login_forget_password,
            tv_login;
    private ImageView imageLeft;
    private RadioButton radio_suffer,
            radio_doctor;
    private String phone,
            password;

    private LineEditText et_login_phone,
            et_login_password;

    private ACProgressFlower loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "登录");
        tv_login_register = (TextView) findViewById(R.id.tv_login_register);
        tv_login_register.setOnClickListener(this);
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        radio_suffer = (RadioButton) findViewById(R.id.radio_suffer);
        radio_doctor = (RadioButton) findViewById(R.id.radio_doctor);
        tv_login_forget_password = (TextView) findViewById(R.id.tv_login_forget_password);
        tv_login_forget_password.setOnClickListener(this);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        et_login_phone = (LineEditText) findViewById(R.id.et_login_phone);
        et_login_password = (LineEditText) findViewById(R.id.et_login_password);
        loadingDialog = ActivityUtil.getLoadingDialog(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == tv_login_register) {
            showSelectRole();
        }
        if (v == imageLeft) {
            this.finish();
        }
        if (v == tv_login_forget_password) {
            intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
        if (v == tv_login) {
            if (prepareForLogin()) {
                loadingDialog.show();
                if (radio_suffer.isChecked()) {
                    getApp().getHttpApi().loginForSufferer(phone, password, loadingDialog, new IAppCommonBeanHolder() {
                        @Override
                        public void asynHold(AppBean bean) {
                            if (bean != null) {
                                TokenBean tokenBean = (TokenBean) bean;
                                if (AppConfig.OK.equals(tokenBean.getCode())) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    getApp().setShareData(AppConfig.EXPIRE_IN, tokenBean.getExpireIn());
                                    getApp().setShareData(AppConfig.TOKEN, tokenBean.getToken());
                                    getApp().setShareData(AppConfig.ROLE_TYPE, tokenBean.getRoleType());
                                    Intent intent_refresh_account = new Intent(AppConfig.REFRESH_ACCOUNT_ACTION);
                                    sendBroadcast(intent_refresh_account);
                                    setResult(AppConfig.LOGIN_OK_RESULT);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, tokenBean.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ActivityUtil.loadError(LoginActivity.this);
                            }
                        }
                    });
                } else {
                    getApp().getHttpApi().loginForDoctor(phone, password, loadingDialog, new IAppCommonBeanHolder() {
                        @Override
                        public void asynHold(AppBean bean) {
                            if (bean != null) {
                                TokenBean tokenBean = (TokenBean) bean;
                                if (AppConfig.OK.equals(tokenBean.getCode())) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    getApp().setShareData(AppConfig.EXPIRE_IN, tokenBean.getExpireIn());
                                    getApp().setShareData(AppConfig.TOKEN, tokenBean.getToken());
                                    getApp().setShareData(AppConfig.ROLE_TYPE, tokenBean.getRoleType());
                                    Intent intent_refresh_account = new Intent(AppConfig.REFRESH_ACCOUNT_ACTION);
                                    sendBroadcast(intent_refresh_account);
                                    setResult(AppConfig.LOGIN_OK_RESULT);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, tokenBean.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ActivityUtil.loadError(LoginActivity.this);
                            }
                        }
                    });
                }
            }

        }
    }

    //登录前的核对
    private boolean prepareForLogin() {
        phone = et_login_phone.getText().toString();
        if (TextUtil.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtil.phoneCheck(phone)) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = et_login_password.getText().toString();
        if (TextUtil.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtil.passwordCheck(password)) {
            Toast.makeText(this, "密码为6到20位数字和字母组合", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void showSelectRole() {
        final SelectDialog dialog = new SelectDialog(this, R.style.CustomDialog);
        dialog.show();
        TextView tv_select_title = (TextView) dialog.findViewById(R.id.tv_select_title);
        ListView listview_select_content = (ListView) dialog.findViewById(R.id.listview_select_content);
        tv_select_title.setText("您是");
        final List<String> list = new ArrayList<>();
        list.add("患者");
        list.add("医生");
        ArrayAdapter adapterBank = new ArrayAdapter(this, R.layout.select_list_item, R.id.tv_select_item_content, list);
        listview_select_content.setAdapter(adapterBank);
        listview_select_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                Intent intent = null;
                if (0 == position) {
                    intent = new Intent(LoginActivity.this, RegisterForSufferActivity.class);
                    startActivity(intent);
                }
                if (1 == position) {
                    intent = new Intent(LoginActivity.this, RegisterForDoctorActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
