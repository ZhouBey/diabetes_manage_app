package com.zpy.diabetes.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.my.MyCountDownTimer;
import com.zpy.diabetes.app.ui.RegisterForDoctorActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.LineEditText;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class DoctorRegisterOneFragment extends Fragment implements BaseUIInterf, View.OnClickListener {

    private View rootView;

    private TextView tv_register_doctor_next,
            tv_doctor_click_to_get_verify;

    private RegisterForDoctorActivity activity;

    private MyCountDownTimer countDownTimer;

    private LineEditText et_register_doctor_phone,
            et_register_doctor_message_verify;
    private ACProgressFlower loadingDialog;
    private MyHandler handler = new MyHandler();
    private String phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.doctor_register_fragment_one, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }
        init();
        show();
        return rootView;
    }

    @Override
    public void init() {
        activity = (RegisterForDoctorActivity) getActivity();
        tv_register_doctor_next = (TextView) rootView.findViewById(R.id.tv_register_doctor_next);
        tv_register_doctor_next.setOnClickListener(this);
        tv_doctor_click_to_get_verify = (TextView) rootView.findViewById(R.id.tv_doctor_click_to_get_verify);
        tv_doctor_click_to_get_verify.setOnClickListener(this);
        countDownTimer = ActivityUtil.getMyCountDownTimer(activity, tv_doctor_click_to_get_verify);
        et_register_doctor_phone = (LineEditText) rootView.findViewById(R.id.et_register_doctor_phone);
        et_register_doctor_message_verify = (LineEditText) rootView.findViewById(R.id.et_register_doctor_message_verify);
        loadingDialog = ActivityUtil.getLoadingDialog(activity);
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

    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if (v == tv_doctor_click_to_get_verify) {
            String phone = et_register_doctor_phone.getText().toString();
            if (TextUtil.isEmpty(phone)) {
                Toast.makeText(activity, "请填写您的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtil.phoneCheck(phone)) {
                Toast.makeText(activity, "您填写的手机号格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.getVerificationCode("86", phone);
            loadingDialog.show();
        }
        if (v == tv_register_doctor_next) {
            String code = et_register_doctor_message_verify.getText().toString();
            String phone = et_register_doctor_phone.getText().toString();
            if (TextUtil.isEmpty(phone)) {
                Toast.makeText(activity, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtil.phoneCheck(phone)) {
                Toast.makeText(activity, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtil.isEmpty(code)) {
                Toast.makeText(activity, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.submitVerificationCode("86", phone, code);
            loadingDialog.show();

        }

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//验证码核对成功
                    loadingDialog.dismiss();
                    activity.setPhone(et_register_doctor_phone.getText().toString());
                    ActivityUtil.switchoverFragment(activity, new DoctorRegisterTwoFragment(), R.id.contain_register_for_doctor);
                }
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    loadingDialog.dismiss();
                    Toast.makeText(activity, "验证码已经发送,请注意查收", Toast.LENGTH_SHORT).show();
                    countDownTimer.start();
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }

    }

}
