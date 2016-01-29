package com.zpy.diabetes.app.my;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.abst.CountDownTimer;
import com.zpy.diabetes.app.util.TextUtil;

public class MyCountDownTimer extends CountDownTimer {
    private TextView textView;

    private Context context;

    public long getMillisInfuture() {
        return millisInfuture;
    }

    public void setMillisInfuture(long millisInfuture) {
        this.millisInfuture = millisInfuture;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public long getCountDownInterval() {
        return countDownInterval;
    }

    public void setCountDownInterval(long countDownInterval) {
        this.countDownInterval = countDownInterval;
    }

    private long millisInfuture;    //倒计时时间
    private long countDownInterval;     //倒计时间隔时间
    public MyCountDownTimer(Context context, long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.countDownInterval = countDownInterval;
        this.millisInfuture = millisInFuture;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(millisUntilFinished / 1000 + context.getString(R.string.send_msg_count_down));
        textView.setClickable(false);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(TextUtil.dip2px(context, 3));
        drawable.setColor(context.getResources().getColor(R.color.btn_not_click));
        textView.setBackgroundDrawable(drawable);
    }

    @Override
    public void onFinish() {
        textView.setText(context.getString(R.string.send_message_again));
        textView.setClickable(true);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(TextUtil.dip2px(context,3));
        drawable.setColor(context.getResources().getColor(R.color.theme_color));
        textView.setBackgroundDrawable(drawable);    }

}

