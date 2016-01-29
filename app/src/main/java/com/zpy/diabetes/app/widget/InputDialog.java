package com.zpy.diabetes.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zpy.diabetes.app.App;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.util.TextUtil;

public class InputDialog extends Dialog {
    private Context context;

    public InputDialog(Context context) {
        this(context, 0);
    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ((App) context.getApplicationContext()).getDisplayWidth() - TextUtil.dip2px(context, 60);
    }
}
