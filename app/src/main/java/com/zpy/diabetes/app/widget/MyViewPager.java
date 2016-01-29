package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015/7/30 0030.
 */
public class MyViewPager extends ViewPager {
    public boolean isSwip() {
        return isSwip;
    }

    public void setSwip(boolean isSwip) {
        this.isSwip = isSwip;
    }

    private boolean isSwip;
    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(isSwip) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isSwip) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }
}
