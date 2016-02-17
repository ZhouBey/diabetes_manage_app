package com.zpy.diabetes.app;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zpy.diabetes.app.widget.MyActionBar;


public class BaseActivity extends AppCompatActivity {
    private App app;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public MyActionBar myActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) this.getApplication();
        AppManager.getAppManager().addActivity(this);
        myActionBar = new MyActionBar(this);
        String localClassName = getLocalClassName();
//        if (localClassName.equals("ui.LoginActivity") ||
//                localClassName.equals("ui.RegisterForSufferActivity") ||
//                localClassName.equals("ui.GestureEditActivity") ||
//                localClassName.equals("ui.GestureChangeVerifyActivity") ||
//                localClassName.equals("ui.GestureStartVerifyActivity")) {
//            initSystemBarTint(true, R.color.transparent00);
//        } else {
            initSystemBarTint(true, R.color.theme_color);
//        }
    }

    public void initSystemBarTint(boolean on, int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(on);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(on);
            tintManager.setStatusBarTintResource(res);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.bindActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.unbindActivity(this);
    }

    public MyActionBar getMyActionBar() {
        return myActionBar;
    }

    public void setMyActionBar(MyActionBar myActionBar) {
        this.myActionBar = myActionBar;
    }
}
