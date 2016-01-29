package com.zpy.diabetes.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.qiniu.android.storage.UploadManager;
import com.zpy.diabetes.app.api.HttpApi;
import com.zpy.diabetes.app.util.TextUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static final String PERSONAL_DATA = "personal_data";
    private static SharedPreferences personData;
    private UploadManager uploadManager;

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    public HttpApi getHttpApi() {
        return httpApi;
    }

    public void setHttpApi(HttpApi httpApi) {
        this.httpApi = httpApi;
    }

    private HttpApi httpApi;
    private List<Activity> activities = new ArrayList<>();

    public int getDisplayWidth() {
        return metrics.widthPixels;
    }

    private int displayWidth;

    public int getDisplayHeight() {
        return metrics.heightPixels;
    }

    private int displayHeight;

    private DisplayMetrics metrics;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        personData = getSharedPreferences(PERSONAL_DATA, MODE_PRIVATE);
        httpApi = new HttpApi(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        uploadManager = new UploadManager();
    }

    public String getHandPassword() {
        return personData.getString("handPassword", null);
    }

    public void setHandPassword(String value) {
        if (value != null) {
            personData.edit().putString("handPassword", TextUtil.md5(value)).apply();
        } else {
            personData.edit().putString("handPassword", null).apply();
        }
    }

    public void setShareData(String name, String value) {
        personData.edit().putString(name, value).apply();
    }

    public void setShareData(String name, boolean bo) {
        personData.edit().putBoolean(name, bo).apply();
    }

    public void setShareData(String name, int value) {
        personData.edit().putInt(name, value).apply();
    }

    public boolean getShareDataBoo(String name, boolean defValue) {
        return personData.getBoolean(name, defValue);
    }

    public String getShareDataStr(String name) {
        return personData.getString(name, null);
    }

    public int getShareDataInt(String name) {
        return this.getShareDataInt(name, -1);
    }
    public int getShareDataInt(String name,int defValue) {
        return personData.getInt(name, defValue);
    }

    public boolean comparePassword(String value) {
        return TextUtil.md5(value).equals(getHandPassword());
    }

    public void bindActivity(Activity activity) {
        activities.add(activity);
    }

    public void unbindActivity(Activity activity) {
        activities.remove(activity);
    }

}
