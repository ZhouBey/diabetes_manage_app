package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.zpy.diabetes.app.interf.ChangePage;


public class IconButtonContainer extends LinearLayout {
    private int currentIndex = -1;
    private ChangePage pages;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        if (currentIndex == this.currentIndex) {
            return;
        }
        IconButton old = (IconButton) this.getChildAt(this.currentIndex);
        if (old != null) {
            old.setBtnSelected(false);
        }
        this.currentIndex = currentIndex;
        IconButton current = (IconButton) this.getChildAt(currentIndex);
        current.setBtnSelected(true);
        pages.onPageChanged(currentIndex);
    }

    public IconButtonContainer(Context context) {
        this(context, null);
    }

    public IconButtonContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("context", context.getClass().getName());
        if (context instanceof ChangePage) {
            pages = (ChangePage) context;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            if (this.getChildAt(i).getClass() == IconButton.class) {
                final IconButton iconButton = (IconButton) this.getChildAt(i);
                iconButton.setIndex(i);

                iconButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IconButtonContainer.this.setCurrentIndex(iconButton.getIndex());
                    }
                });
            }
        }
        this.setCurrentIndex(0);
    }
}
