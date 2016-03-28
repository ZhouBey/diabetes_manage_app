package com.zpy.diabetes.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zpy.diabetes.app.R;


public class UploadPhotoDialog extends Dialog {
    public UploadPhotoDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo_dialog);
        Window dialogWindow = getWindow();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = wm.getDefaultDisplay().getWidth()/15*14;
        lp.gravity = Gravity.BOTTOM;
    }
}
