package com.zpy.diabetes.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zpy.diabetes.app.R;

public class SelectDialog extends Dialog {
    private Context context;

    public SelectDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.select);
        Window dialogWindow = getWindow();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = wm.getDefaultDisplay().getWidth() / 3 * 2;
    }
}
