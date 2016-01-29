package com.zpy.diabetes.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zpy.diabetes.app.R;

public class ConfirmationDialog extends Dialog {
    private Context context;

    public ConfirmationDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_dialog);
        Window dialogWindow = getWindow();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = wm.getDefaultDisplay().getWidth()/4*3;
    }
}
