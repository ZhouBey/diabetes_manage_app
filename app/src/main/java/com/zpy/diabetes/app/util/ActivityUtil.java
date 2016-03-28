package com.zpy.diabetes.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zpy.diabetes.app.App;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.my.MyCountDownTimer;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.widget.MyActionBar;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;
import com.zpy.diabetes.app.widget.linechart.FancyChart;
import com.zpy.diabetes.app.widget.linechart.FancyChartStyle;
import com.zpy.diabetes.app.widget.linechart.data.ChartData;


public class ActivityUtil {


    public static void showActionBar(MyActionBar myActionBar, ActionBar actionBar, int leftImageID, Object rightObject, Object titleObject, int background) {
        if (titleObject instanceof Integer) {
            int centerImageID = (int) titleObject;
            if (centerImageID != -1) {
                myActionBar.setImageViewCenter(centerImageID);
            }
        }
        if (titleObject instanceof String) {
            String title = (String) titleObject;
            if (title != null) {
                myActionBar.setActionBarTitle(title);
            }
        }
        if (titleObject instanceof View) {
            View view = (View) titleObject;
            if (view != null) {
                myActionBar.setActionBarCentreView(view);
            }
        }
        if (leftImageID != -1) {
            myActionBar.setImageViewLeft(leftImageID);
        } else {
            myActionBar.getLayout_my_actionbar_left().setVisibility(View.GONE);
        }
        if (rightObject instanceof Integer) {
            int rightImageID = (int) rightObject;
//            if (rightImageID != -1) {
            myActionBar.setImageViewRight(rightImageID);
//            }
        }
        if (rightObject instanceof String) {
            String rigthStr = (String) rightObject;
            if (rigthStr != null) {
                myActionBar.setTextViewRight(rigthStr);
            }
        }
        View customView = myActionBar.getRootView();
        actionBar.setCustomView(customView);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0.0F);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        parent.setTitleTextColor(customView.getResources().getColor(R.color.theme_color));
        if (background != -1) {
            parent.setBackgroundResource(background);
        } else {
            parent.setBackgroundColor(customView.getContext().getResources().getColor(R.color.theme_color));
        }
    }

    public static void showActionBar(MyActionBar myActionBar, ActionBar actionBar, int leftImageID, Object rightObject, Object titleObject) {
        showActionBar(myActionBar, actionBar, leftImageID, rightObject, titleObject, -1);
    }

    public static void switchoverFragment(FragmentActivity activity, Fragment newFragment, int containerID) {
        switchoverFragment(activity, newFragment, containerID, -1, -1);
    }

    public static void switchoverFragment(FragmentActivity activity, Fragment newFragment,
                                          int containerID, int enterAnim, int exitAnim) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (enterAnim != -1 && exitAnim != -1) {
            transaction.setCustomAnimations(enterAnim, exitAnim);
        }
        if (!newFragment.isAdded()) {
            transaction.replace(containerID, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    public static void logout(App app) {
        app.setShareData(AppConfig.TOKEN, null);
        app.setShareData(AppConfig.EXPIRE_IN, null);
        app.setShareData(AppConfig.ROLE_TYPE, -1);
        app.setShareData(AppConfig.PHONE, null);
        app.setShareData(AppConfig.PHOTO, null);
    }

    public static void overdue(Activity activity, ACProgressFlower dialog, boolean isJump) {
        ActivityUtil.logout((App) activity.getApplication());
        if (dialog != null) {
            dialog.dismiss();
        }
        Toast.makeText(activity, "账户已过期或未登录，请先登录", Toast.LENGTH_LONG).show();
        if (isJump) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivityForResult(intent, 101);
        }
    }


    public static void call(Activity activity, boolean isOpenActionDial, String phone) {
        Intent intent = null;
        if (isOpenActionDial) {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        }
        activity.startActivity(intent);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //下划线
    public static void setUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    public static void setSwipeRefreshLayout(Context context, SwipeRefreshLayout refreshLayout) {
        //配置颜色
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light,
                android.R.color.holo_blue_bright);
        //设置偏移量
        refreshLayout.setProgressViewOffset(false, 0, TextUtil.dip2px(context, 24));
    }

    public static MyCountDownTimer getMyCountDownTimer(Context context, TextView textView) {
        return new MyCountDownTimer(context, 60000, 1000, textView);
    }

    public static ACProgressFlower getLoadingDialog(Context context) {
        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(AppConfig.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY)
                .petalThickness(context.getResources().getDimensionPixelSize(R.dimen.loading_petal_thickness))
                .build();
        return dialog;
    }

    public static Button getBtnLoadMore(Context context, Button btn) {
        btn = new Button(context);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextUtil.dip2px(context, 14));
        btn.setTextColor(context.getResources().getColor(R.color.font_gray));
        btn.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = TextUtil.dip2px(context, 40);
        btn.setLayoutParams(params);
        btn.setGravity(Gravity.CENTER);
        return btn;
    }

    public static void loadError(Context context) {
        if (!isNetworkConnected(context)) {
            Toast.makeText(context, AppConfig.NO_INTERNET, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, AppConfig.INTERNET_ERR, Toast.LENGTH_SHORT).show();
        }
    }

    public static void drawLineChart(FancyChart linechart, double[] yValues) {
        linechart.clearValues();
        linechart.invalidate();
        String[] week = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        ChartData data = new ChartData(ChartData.LINE_COLOR);
        FancyChartStyle style = linechart.getChartStyle();
        style.setDrawBackgroundBelowLine(false);
        for (int i = 1; i <= week.length; i++) {
            data.addPoint(i, yValues[i - 1]);
            data.addXValue(i, week[i - 1]);
        }
        linechart.addData(data);
    }

    public static void fitWidth(Context context, Bitmap bitmap, ImageView imageView) {
        fitWidth(context, bitmap, imageView, -1);
    }

    public static void fitWidth(Context context, Bitmap bitmap, ImageView imageView, int defaultHeight) {
        int orgWid = bitmap.getWidth();
        int orgHeight = bitmap.getHeight();

        int screenWidth = ((App) context.getApplicationContext()).getDisplayWidth() - TextUtil.dip2px(context, 40);   // 屏幕宽（像素，如：480px）

        int newWid = screenWidth;
        int newHeight = 0;
        if (defaultHeight == -1) {
            newHeight = (int) ((double) newWid / (double) orgWid * (double) orgHeight);
        } else {
            newHeight = defaultHeight;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWid, newHeight);
        params.setMargins(TextUtil.dip2px(context, 20), TextUtil.dip2px(context, 7), TextUtil.dip2px(context, 20), 0);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
    }
}
